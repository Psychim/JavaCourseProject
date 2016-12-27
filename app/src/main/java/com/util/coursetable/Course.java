package com.util.coursetable;

/**
 * course
 */
public class Course {
    String coursename;
    int weekStart;
    int weekEnd;
    String teacher;
    double credit;
    public Course(){
        coursename=new String();
    }
    public Course(String pName) {
        coursename=pName;
    }
    public void setWeek(int pws,int pwe){
        weekStart=pws;
        weekEnd=pwe;
    }
    public void setCoursename(String pcn){
        coursename=pcn;
    }
    public void setTeacher(String pt){
        teacher=pt;
    }
    public void setCredit(double pc){
        credit=pc;
    }
    public String getCoursename(){
        return coursename;
    }
    public int getWeekStart(){
        return weekStart;
    }
    public int getWeekEnd(){
        return weekEnd;
    }
    public String getTeacher(){
        return teacher;
    }
    public double getCredit(){
        return credit;
    }
    @Override
    public boolean equals(Object obj){
        Course cs=(Course)obj;
        return coursename==cs.coursename&&
                weekStart==cs.weekStart&&
                weekEnd==cs.weekEnd&&
                teacher==cs.teacher&&
                credit==cs.credit;
    }
}