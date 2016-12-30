package com.util.affair;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * Created by aiocac on 2016/12/29.
 */

/**
 * 事务
 */
public class Affair implements Comparable<Affair>{
    /**
     * 事务ID
     */
    private Integer ID;
    /**
     * 事务的提醒时间
     */
    private Calendar time;
    /**
     * 事务的内容
     */
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

    /**
     * TODO 设置事务内容
     * @param pContent
     */
    public void setContent(String pContent){
        content=pContent;
    }

    /**
     * TODO 设置提醒时间
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     */
    public void setTime(int year,int month,int date,int hour,int minute){
        time.set(year,month,date,hour,minute);
    }

    /**
     * TODO 设置提醒时间
     * @param date
     */
    public void setTime(Date date){
        time.setTime(date);
    }

    /**
     * TODO 获取事务内容
     * @return
     */
    public String getContent(){
        return content;
    }

    /**
     * TODO 获取提醒时间对应的日期字符串，形式为"yyyy-MM-dd"
     */
    public String getDateString(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(time.getTime());
    }

    /**
     * TODO 获取提醒时间对应的时间字符串，形式为"HH:mm"
     */
    public String getTimeString(){
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm");
        return timeFormat.format(time.getTime());
    }

    /**
     * TODO 设置事务ID
     */
    public void setID(Integer pID){
        ID=pID;
    }

    /**
     * TODO 获取事务ID
     */
    public Integer getID(){
        return ID;
    }

    /**
     * TODO 获取提醒时间的时
     * @return
     */
    public Integer getHour(){
        Integer hour=time.get(Calendar.HOUR);
        if(time.get(Calendar.AM_PM)==Calendar.PM)
            hour+=12;
        return hour;
    }

    /**
     * TODO 获取提醒时间的分钟
     * @return
     */
    public Integer getMinute(){
        return time.get(Calendar.MINUTE);
    }

    /**
     * TODO 获取提醒时间的毫秒级的时间戳
     * @return
     */
    public long getTime(){
        return time.getTimeInMillis();
    }

    /**
     * TODO 比较两事务的早晚
     */
    @Override
    public int compareTo(Affair o) {
        long dif=this.getTime()-o.getTime();
        if(dif>0) return 1;
        else if(dif<0) return -1;
        else return 0;
    }
}
