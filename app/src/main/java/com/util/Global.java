package com.util;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.util.Log;

import com.example.scm.calendar.CourseTableFragment;
import com.example.scm.calendar.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Date;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aiocac on 2016/12/27.
 */

/**
 * 用于储存参数和控制不同Activity之间的交互和信息传递
 */
public class Global {
    private static Global instance=null;
    /**
     * Global时间
     * 由CalendarFragment.calendarView选中的日期决定
     */
    private Calendar calendar;
    /**
     * 当前的学生一卡通号
     */
    private String stuID;
    /**
     * 当前的学期
     */
    private String acaYear;
    /**
     * 当前学期第一周周一的日期
     */
    private Calendar FirstMonday;
    /**
     * CourseTableFragment引用
     */
    private CourseTableFragment courseTableFragment=null;
    /**
     * NotificationService引用
     */
    private MainActivity.NotificationService notificationService;
    private Global(){
        calendar=Calendar.getInstance();
        stuID="";
        acaYear="";
        setPresentWeek(1);
    }

    /**
     * TODO 从global.conf文件中读取信息
     * @param context
     */
    public void getSavedInfo(Context context){
        File file=new File(context.getFilesDir().getAbsolutePath()+"/"+"global.conf");
        String text;
        Matcher matcher;
        if(file.exists())
            try{
                FileInputStream fileInputStream=new FileInputStream(file);
                byte[] buffer=new byte[fileInputStream.available()];
                fileInputStream.read(buffer);
                fileInputStream.close();
                text=new String(buffer);
                /**
                 * 读取stuID
                 */
                matcher= Pattern.compile("Student_ID\\s*?=\\s*?([0-9]+)\\r?\\n").matcher(text);
                if(matcher.find()){
                    stuID=matcher.group(1);
                }
                /**
                 * 读取acaYear
                 */
                matcher=Pattern.compile("Academic_Year\\s*?=\\s*?([0-9]{2}-[0-9]{2}-[1-3])\\r?\\n").matcher(text);
                if(matcher.find()){
                    acaYear=matcher.group(1);
                }
                /**
                 * 读取FirstMonday
                 */
                matcher=Pattern.compile("First_Monday\\s*?=\\s*?([0-9]+?)\\r?\\n").matcher(text);
                if(matcher.find()){
                    if(FirstMonday==null)
                        FirstMonday=Calendar.getInstance();
                    FirstMonday.setTimeInMillis(Long.parseLong(matcher.group(1)));
                }
            }catch(Exception e){
                System.out.println(e.getClass().getName()+": "+e.getMessage());
                e.printStackTrace();
            }
    }

    /**
     * TODO 将当前信息保存至global.conf
     * @param context
     */
    public void SaveInfo(Context context){
        File file=new File(context.getFilesDir().getAbsolutePath()+"/"+"global.conf");
        String toWrite="";
        toWrite+="Student_ID = "+stuID+"\n";
        toWrite+="Academic_Year = "+acaYear+"\n";
        toWrite+="First_Monday = "+FirstMonday.getTimeInMillis()+"\n";
        try {
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = toWrite.getBytes();
            fileOutputStream.write(buffer);
        }catch(Exception e){
            System.out.println(e.getClass().getName()+": "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * TODO 设置CourseTableFragment引用
     * @param pCTF
     */
    public void setCourseTableFragment(CourseTableFragment pCTF){
        courseTableFragment=pCTF;
    }

    /**
     * TODO 设置学生一卡通号，并重设课表
     * @param pStuID
     */
    public void setStuID(String pStuID){
        stuID=pStuID;
        if(courseTableFragment!=null)
            courseTableFragment.resetCourseTable();
    }
    public String getStuID(){
        return stuID;
    }

    /**
     * TODO 设置global时间并重设课表
     * 该函数只会在calendarFragment中被调用
     */
    public void setTime(int year,int month,int dayOfMonth){
        calendar.set(year,month,dayOfMonth);
        if(courseTableFragment!=null)
            courseTableFragment.resetDate();
    }

    /**
     * TODO 设置global时间并重设课表
     * 该函数只会在calendarFragment中被调用
     */
    public void setTime(long date){
        calendar.setTimeInMillis(date);
        if(courseTableFragment!=null)
            courseTableFragment.resetDate();
    }

    /**
     * TODO 获取Global时间的毫秒级时间戳
     */
    public long getTime(){
        return calendar.getTimeInMillis();
    }

    /**
     * TODO 获取Global时间的年份
     * @return
     */
    public int getYear(){
        return calendar.get(Calendar.YEAR);
    }

    /**
     * TODO 获取Global时间的日期
     * @return
     */
    public int getDate(){
        return calendar.get(Calendar.DATE);
    }

    /**
     * TODO 设置Global时间的当前周，并计算出FirstMonday，通知courseTableFragment重设时间
     * @param pWeek
     */
    public void setPresentWeek(int pWeek){
        if(FirstMonday==null)
            FirstMonday=Calendar.getInstance();
        try {
            if(pWeek<1) throw new IllegalArgumentException("当前周不合法");
            FirstMonday.setTime(calendar.getTime());
            for (int i = pWeek; i > 1; i--) {
                FirstMonday.add(Calendar.DATE, -7);
            }
            FirstMonday.add(Calendar.DATE,-getDayOfWeek(FirstMonday));
        }catch(Exception e){
            System.out.println(e.getClass().getName()+": "+e.getMessage());
            e.printStackTrace();
        }
        if(courseTableFragment!=null)
            courseTableFragment.resetDate();
    }

    /**
     * TODO 根据FirstMonday计算出Global时间的当前周
     */
    public Integer getPresentWeek(){
        Calendar Lookover=Calendar.getInstance();
        Lookover.setTime(FirstMonday.getTime());
        int result=0;
        while(Lookover.compareTo(calendar)<=0){
            result++;
            Lookover.add(Calendar.DATE,7);
        }
        return result;
    }

    /**
     * TODO 同时设置学生一卡通号和学期，再通知课表界面重设课表
     * @param pStuID
     * @param pAcaYear
     */
    public void setIDandYear(String pStuID,String pAcaYear){
        stuID=pStuID;
        acaYear=pAcaYear;
        if(courseTableFragment!=null)
            courseTableFragment.resetCourseTable();
    }

    /**
     * TODO 设置学期，通知课表界面重设课表
     * @param pAcaYear
     */
    public void setAcaYear(String pAcaYear){
        acaYear=pAcaYear;
        if(courseTableFragment!=null)
            courseTableFragment.resetCourseTable();
    }
    public String getAcaYear(){
        return acaYear;
    }

    /**
     * TODO 返回Global的单实例
     * @return
     */
    public static Global getInstance(){
        synchronized (Global.class){
            if(instance==null)
                instance=new Global();
        }
        return instance;
    }

    public int getMonth(){
        return calendar.get(Calendar.MONTH);
    }

    /**
     * TODO 获取Global时间的星期的Calendar序号
     * @return
     */
    public int getDayOfWeek(){
        return getDayOfWeek(calendar);
    }

    /**
     * TODO 获取Global时间的星期的序号，按周一为0开始
     */
    private int getDayOfWeek(Calendar pCalendar){
        int today = pCalendar.get(Calendar.DAY_OF_WEEK);
        if ((today -= 2) == -1)
            today = 6;
        return today;
    }

    /**
     * TODO 计算Global时间所在周的每天的日期
     * @return
     */
    public int[] getWeekDateArray(){
        Calendar tmpCalendar=Calendar.getInstance();
        tmpCalendar.setTime(calendar.getTime());
        int[] dateArray=new int[7];
        int today=Global.getInstance().getDayOfWeek();
        dateArray[today]=calendar.get(Calendar.DAY_OF_MONTH);
        for(int i=1;today-i>=0;i++){
            tmpCalendar.add(Calendar.DATE,-1);
            dateArray[today-i]=tmpCalendar.get(Calendar.DAY_OF_MONTH);
        }
        tmpCalendar.setTime(calendar.getTime());
        for(int i=1;today+i<7;i++){
            tmpCalendar.add(Calendar.DATE,1);
            dateArray[today+i]=tmpCalendar.get(Calendar.DAY_OF_MONTH);
        }
        return dateArray;
    }

    /**
     * TODO 获取"yyyy年MM月dd日"表示的日期
     * @return
     */
    public String getDateString(){
        return getYear()+"年"+(getMonth()+1)+"月"+getDate()+"日";
    }

    /**
     * TODO 通知课表界面重新爬取课表
     */
    public void updateCourseTable(){
        courseTableFragment.updateCourseTable();
    }

    /**
     * TODO 设置提醒服务的引用
     * @param service
     */
    public void setNotificationService(MainActivity.NotificationService service){
        notificationService=service;
    }

    /**
     * TODO 通知提醒服务更新事务
     */
    public void refreshNotification(){
        notificationService.refreshAffairs();
    }
}
