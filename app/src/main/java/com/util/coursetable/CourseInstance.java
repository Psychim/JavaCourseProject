package com.util.coursetable;

/**
 * Created by aiocac on 2016/12/14.
 */

/**
 * 一个具体的带有安排的课程实例
 */
public class CourseInstance {
    public Course C;
    public Arrangement A;
    public CourseInstance(Course pc,Arrangement pa){
        C=pc;
        A=pa;
    }
    @Override
    public boolean equals(Object obj){
        if(obj instanceof CourseInstance) {
            CourseInstance ci = (CourseInstance) obj;
            return C.equals(ci.C) && A.equals(ci.A);
        }
        else return false;
    }
    @Override
    public String toString(){
        String ret;
        ret=C.coursename+'@'+A.classroom;
        return ret;
    }
}
