package com.util.affair;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * Created by aiocac on 2016/12/29.
 */

public class Affair {
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
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(time.getTime());
    }
}
