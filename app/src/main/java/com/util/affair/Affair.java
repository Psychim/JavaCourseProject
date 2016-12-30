package com.util.affair;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * Created by aiocac on 2016/12/29.
 */

public class Affair implements Comparable<Affair>{
    private Integer ID;
    private Calendar time;
    private String content;
    public Affair(){
        time=Calendar.getInstance();
        content="";
    }
    public Affair(String pContent,int year,int month,int date,int hour,int minute){
        this();
        setContent(pContent);
        setTime(year,month,date,hour,minute);
    }
    public void setContent(String pContent){
        content=pContent;
    }
    public void setTime(int year,int month,int date,int hour,int minute){
        time.set(year,month,date,hour,minute);
    }
    public void setTime(Date date){
        time.setTime(date);
    }
    public String getContent(){
        return content;
    }
    public String getDateString(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(time.getTime());
    }
    public String getTimeString(){
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm");
        return timeFormat.format(time.getTime());
    }
    public void setID(Integer pID){
        ID=pID;
    }
    public Integer getID(){
        return ID;
    }
    public Integer getHour(){
        Integer hour=time.get(Calendar.HOUR);
        if(time.get(Calendar.AM_PM)==Calendar.PM)
            hour+=12;
        return hour;
    }
    public Integer getMinute(){
        return time.get(Calendar.MINUTE);
    }
    public long getTime(){
        return time.getTimeInMillis();
    }

    @Override
    public int compareTo(Affair o) {
        long dif=this.getTime()-o.getTime();
        if(dif>0) return 1;
        else if(dif<0) return -1;
        else return 0;
    }
}
