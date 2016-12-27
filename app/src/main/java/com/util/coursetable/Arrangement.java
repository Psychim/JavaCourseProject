package com.util.coursetable;

import com.util.Constant.*;

/**
 * Created by aiocac on 2016/12/14.
 */

/**
 * course arrangement
 */
public class Arrangement {
     _WEEKDAY weekday;
    int segStart;
    int segEnd;
    String classroom;
    public Arrangement(){
    }
    public Arrangement(_WEEKDAY pwd,int pss,int pse,String pcr){
        weekday=pwd;
        setSegment(pss,pse);
        classroom=pcr;
    }
    public void setSegment(int pss,int pse){
        segStart=pss;
        segEnd=pse;
    }
    public void setWeekday(_WEEKDAY pwd){
        weekday=pwd;
    }
    public void setClassroom(String pcr){
        classroom=pcr;
    }
    public _WEEKDAY getWeekday(){
        return weekday;
    }
    public int getSegStart(){
        return segStart;
    }
    public int getSegEnd(){
        return segEnd;
    }
    public String getClassroom(){
        return classroom;
    }
    @Override
    public boolean equals(Object obj){
        Arrangement arr=(Arrangement)obj;
        return segStart==arr.segStart&&segEnd==arr.segEnd&&classroom.equals(arr.classroom);
    }
}
