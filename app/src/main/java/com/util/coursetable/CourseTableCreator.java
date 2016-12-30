package com.util.coursetable;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;
import java.util.concurrent.*;

import com.util.Constant.*;
import com.util.network.HttpRequest._METHOD;
import com.util.network.*;
import com.util.sql.StudentQuery;

/**
 * 用于获取课表
 */
public class CourseTableCreator implements Callable<CourseTable>{
    private Context context;
    /**
     * 网页参数
     */
    private String returnStr;
    private String queryStudentId;
    private String queryAcademicYear;
    /**
     * 解析网页需要的RegEx
     */
    private final static String courseNameRegex="<td height=\"34\" class=\"line_topleft\" width=\"35%\" align=\"center\"><font class=\"style8\">(.*?)</font></td>";
    private final static String TeacherRegex="<td height=\"34\"  class=\"line_topleft\" width=\"20%\" align=\"center\"><font class=\"style8\">&nbsp;(.*?)</font></td>";
    private final static String CreditRegex="<td height=\"34\" class=\"line_topleft\" width=\"15%\" align=\"center\">&nbsp;([0-9.]*)</td>";
    private final static String WeekRegex="<td height=\"34\" class=\"line_topleft\" width=\"20%\" align=\"center\">&nbsp;([0-9]+)-([0-9]+)</td>";
    private final static String WeekdayNightRegex="<td class=\"line_topleft\" rowspan=\"2\"   align=\"center\">(.*?)</td>";
    private final static String WeekdayDaytimeRegex="<td rowspan=\"5\" class=\"line_topleft\"\"? align=\"center\">(.*?)</td>";
    private final static String SaturdayRegex="<td class=\"line_topleft\" rowspan=\"2\"  colspan=\"2\" align=\"center\">周六</td>\\s*?<td class=\"line_topleft\" rowspan=\"2\"  colspan=\"5\" align=\"center\">(.*?)</td>\\s*?</tr>";
    private final static String SundayRegex="<td class=\"line_topleft\" rowspan=\"2\"  colspan=\"2\" align=\"center\">周日</td>\\s*?<td class=\"line_topleft\" rowspan=\"2\"  colspan=\"5\" align=\"center\">(.*?)</td>\\s*?</tr>";
    private final static String TableInfoRegex="(.*?)<br>\\[[0-9]+-[0-9]+周\\]([0-9]+)-([0-9]+)节<br>(.*?)<br>";

    public CourseTableCreator(Context pContext){
        context=pContext;
    }
    public CourseTableCreator(Context pContext,String pId,String pYear){
        this(pContext);
        setStudentId(pId);
        setYear(pYear);
    }
    public void setReturnStr(String pStr){
        returnStr=pStr;
    }
    public void setStudentId(String pId){
        queryStudentId=pId;
    }
    public void setYear(String pYear){
        queryAcademicYear=pYear;
    }

    /**
     * TODO 发送请求
     * @return 课表网页
     */
    public String sendRequest(){
        String page;
        HttpRequest request=new HttpRequest();
        request.setUrl("http://xk.urp.seu.edu.cn/jw_service/service/stuCurriculum.action");
        request.setParam("returnStr="+returnStr+"&queryStudentId="+queryStudentId+"&queryAcademicYear="+queryAcademicYear);
        request.setMethod(_METHOD.POST);
        ExecutorService exec=Executors.newCachedThreadPool();
        Future<String> result=exec.submit(new HttpRequestThread(request));
        while(!result.isDone());
        try{
            page=result.get();
        }catch(InterruptedException e){
            System.out.println(e);
            return null;
        }catch(ExecutionException e){
            System.out.println(e);
            return null;
        }finally{
            exec.shutdown();
        }
        return page;
    }

    /**
     * TODO 解析网页，获取课程信息
     */
    public Map<String,Course> getCourses(String page){
        /**
         * 获取课程名和总的课程数
         */
        Matcher mcher=Pattern.compile(courseNameRegex).matcher(page);
        List<Course> courses=new ArrayList<Course>();
        while(mcher.find()){
            String courseName=mcher.group(1);
            if(courseName.equals("&nbsp;")||courseName.equals("合计")) break;
            courses.add(new Course(courseName));
            System.out.println(courseName);
        }
        /**
         * 获取教师
         */
        mcher=Pattern.compile(TeacherRegex).matcher(page);
        for(Course course:courses) {
            if(mcher.find()) {
                String teacher = mcher.group(1);
                if (teacher.equals("&nbsp;")) break;
                course.teacher=teacher;
            }
        }
        /**
         * 获取学分
         */
        mcher=Pattern.compile(CreditRegex).matcher(page);
        for(Course course:courses) {
            if (mcher.find() )
                try{
                    course.credit = Double.parseDouble(mcher.group(1));
                }catch(Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
        }
        /**
         * 获取开始周和结束周
         */
        mcher=Pattern.compile(WeekRegex).matcher(page);
        for(Course course:courses) {
            if (mcher.find()) {
                String wS = mcher.group(1);
                String wE = mcher.group(2);
                try {
                    course.setWeek(Integer.parseInt(wS), Integer.parseInt(wE));
                }catch(Exception e){
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }
        /**
         * 构造一个课程名和课程一一对应的map
         */
        Map<String,Course> coursemap=new HashMap<String,Course>();
        for(Course course:courses){
            coursemap.put(course.coursename,course);
        }
        return coursemap;
    }

    /**
     * TODO 获取课程安排
     */
    public Map<Arrangement,String> getArrangement(String page){
        Map<Arrangement,String> Arr2Name=new HashMap<Arrangement,String>();
        Matcher mcher;
        /**
         * 先获取工作日白天的部分
         */
        mcher=Pattern.compile(WeekdayDaytimeRegex).matcher(page);
        Matcher infomcher;
        /**
         * 表示周几的序号
         */
        int start=0;
        int end=4;
        /**
         * i==0  上午
         * i==1  下午
         * i==2  晚上
         * i==3  周六
         * i==4  周日
         */
        for(int i=0;i<5;i++){
            if(i==1){
                /**
                 * mcher会匹配到"下午"，调用一次find()跳过
                 */
                mcher.find();
            }
            /**
             * 获取工作日晚上的部分
             */
            else if(i==2){
                mcher=Pattern.compile(WeekdayNightRegex).matcher(page);
            }
            /**
             * 获取周六的部分
             */
            else if(i==3){
                mcher=Pattern.compile(SaturdayRegex).matcher(page);
                start=end=5;
            }
            /**
             * 获取周日的部分
             */
            else if(i==4){
                mcher=Pattern.compile(SundayRegex).matcher(page);
                start=end=6;
            }
            for(int j=start;j<=end;j++){
                /**
                 * 当前序号对应wd
                 */
                _WEEKDAY wd=_WEEKDAY.values()[j];
                if(mcher.find()){
                    /**
                     * 从已获取的网页部分中匹配课程安排信息
                     */
                    infomcher=Pattern.compile(TableInfoRegex).matcher(mcher.group(1));
                    /**
                     * 每个时间块可能不止一节课
                     */
                    while(infomcher.find())
                        try{
                            String courseName=infomcher.group(1);
                            int segStart=Integer.parseInt(infomcher.group(2));
                            int segEnd=Integer.parseInt(infomcher.group(3));
                            String classroom=infomcher.group(4);
                            Arr2Name.put(new Arrangement(wd,segStart,segEnd,classroom),courseName);
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                }
            }
        }
        return Arr2Name;
    }

    /**
     * TODO 根据课程名将所有课程和安排结合得到课程实例以创建课表
     */
    public CourseTable CreateTable(Map<Arrangement,String> arr2name,Map<String,Course> name2course){
        CourseTable ct=new CourseTable();
        for(Arrangement arr:arr2name.keySet()){
            String coursename=arr2name.get(arr);
            Course course=name2course.get(coursename);
            ct.add(new CourseInstance(course,arr));
        }
        ct.setStuID(queryStudentId);
        ct.setAcaYear(queryAcademicYear);
        return ct;
    }

    /**
     * TODO 爬取课表，存入数据库并返回
     * @return
     */
    public CourseTable obtainCourseTable(){
        if(queryStudentId==null||queryStudentId.isEmpty()||queryAcademicYear==null||queryAcademicYear.isEmpty())
            return null;
        CourseTable ct=null;
        StudentQuery sq=new StudentQuery(context);
        String page=sendRequest();
        if(page!=null&&!page.isEmpty()){
            ct=CreateTable(getArrangement(page),getCourses(page));
            sq.SaveCourseTable(ct);
            return ct;
        }
        else return null;
    }

    /**
     * TODO 先从数据库里获取课表，若失败则爬取课表
     * @return
     */
    @Override
    public CourseTable call(){
        if(queryStudentId==null||queryStudentId.isEmpty()||queryAcademicYear==null||queryAcademicYear.isEmpty())
            return null;
        CourseTable ct=null;
        StudentQuery sq=new StudentQuery(context);
        ct=sq.getCourseTable(queryStudentId,queryAcademicYear);
        if(ct!=null) {
            return ct;
        }
        else return obtainCourseTable();
    }
}
