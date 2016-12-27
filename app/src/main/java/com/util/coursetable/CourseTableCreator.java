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

public class CourseTableCreator implements Callable<CourseTable>{
    private Context context;
    private String returnStr;
    private String queryStudentId;
    private String queryAcademicYear;
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
    public Map<String,Course> getCourses(String page){
        //get the number of courses and their names
        Matcher mcher=Pattern.compile(courseNameRegex).matcher(page);
        List<Course> courses=new ArrayList<Course>();
        while(mcher.find()){
            String courseName=mcher.group(1);
            if(courseName.equals("&nbsp;")||courseName.equals("合计")) break;
            courses.add(new Course(courseName));
            System.out.println(courseName);
        }
        // get teacher
        mcher=Pattern.compile(TeacherRegex).matcher(page);
        for(Course course:courses) {
            if(mcher.find()) {
                String teacher = mcher.group(1);
                if (teacher.equals("&nbsp;")) break;
                course.teacher=teacher;
            }
        }
        //get credit
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
        //get the week when the course starts and the week when the course ends
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
        Map<String,Course> coursemap=new HashMap<String,Course>();
        for(Course course:courses){
            coursemap.put(course.coursename,course);
        }
        return coursemap;
    }

    /**
     * TODO get the arrangement of courses
     * @param page the web page of the course table
     * @return a map from arrangement to course name
     */
    public Map<Arrangement,String> getArrangement(String page){
        //get weekday, segment and classroom
        Map<Arrangement,String> Arr2Name=new HashMap<Arrangement,String>();
        Matcher mcher;
        mcher=Pattern.compile(WeekdayDaytimeRegex).matcher(page);
        Matcher infomcher;
        int start=0;
        int end=4;
        for(int i=0;i<5;i++){
            if(i==1){
                mcher.find();   //jump “下午”
            }
            else if(i==2){
                mcher=Pattern.compile(WeekdayNightRegex).matcher(page);
            }
            else if(i==3){
                mcher=Pattern.compile(SaturdayRegex).matcher(page);
                start=end=5;
            }
            else if(i==4){
                mcher=Pattern.compile(SundayRegex).matcher(page);
                start=end=6;
            }
            for(int j=start;j<=end;j++){
                _WEEKDAY wd=_WEEKDAY.values()[j];
                if(mcher.find()){
                    infomcher=Pattern.compile(TableInfoRegex).matcher(mcher.group(1));
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
    @Override
    public CourseTable call(){
        CourseTable ct=null;
        StudentQuery sq=new StudentQuery(context);
        ct=sq.getCourseTable(queryStudentId,queryAcademicYear);
        if(ct!=null) {
            return ct;
        }
        String page=sendRequest();
        if(page!=null&&!page.isEmpty()){
            ct=CreateTable(getArrangement(page),getCourses(page));
            sq.SaveCourseTable(ct);
            return ct;
        }
        else return null;
    }
}
