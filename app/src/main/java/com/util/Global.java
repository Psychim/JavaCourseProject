package com.util;

import android.util.Log;

import com.example.scm.calendar.CourseTableFragment;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by aiocac on 2016/12/27.
 */

public class Global {
    private static Global instance=null;
    private Calendar calendar;
    private String stuID="213150865";
    private String acaYear="16-17-2";
    private Calendar FirstMonday;
    private CourseTableFragment courseTableFragment=null;
    private Global(){
        calendar=Calendar.getInstance();
        FirstMonday=Calendar.getInstance();
        setPresentWeek(1);
    }
    public void setCourseTableFragment(CourseTableFragment pCTF){
        courseTableFragment=pCTF;
    }
    public void setStuID(String pStuID){
        stuID=pStuID;
        if(courseTableFragment!=null)
            courseTableFragment.resetCourseTable();
    }
    public String getStuID(){
        return stuID;
    }
    public void setTime(int year,int month,int dayOfMonth){
        calendar.set(year,month,dayOfMonth);
        if(courseTableFragment!=null)
            courseTableFragment.resetDate();
    }
    public long getTime(){
        return calendar.getTimeInMillis();
    }
    public int getYear(){
        return calendar.get(Calendar.YEAR);
    }
    public int getDate(){
        return calendar.get(Calendar.DATE);
    }
    public void setPresentWeek(int pWeek){
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
    public void setAcaYear(String pAcaYear){
        acaYear=pAcaYear;
        if(courseTableFragment!=null)
            courseTableFragment.resetCourseTable();
    }
    public String getAcaYear(){
        return acaYear;
    }
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
    public int getDayOfWeek(){
        return getDayOfWeek(calendar);
    }
    private int getDayOfWeek(Calendar pCalendar){
        int today = pCalendar.get(Calendar.DAY_OF_WEEK);
        if ((today -= 2) == -1)
            today = 6;
        return today;
    }
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
}
