package com.util.coursetable;

/**
 * 课程（科目）
 */
public class Course {
    /**
     * 课程名
     */
    String coursename;
    /**
     * 开始周和结束周
     */
    int weekStart;
    int weekEnd;
    /**
     * 教师
     */
    String teacher;
    /**
     * 学分
     */
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
        if(obj instanceof Course) {
            Course cs = (Course) obj;
            return coursename.equals(cs.coursename)  &&
                    weekStart == cs.weekStart &&
                    weekEnd == cs.weekEnd &&
                    teacher.equals( cs.teacher) &&
                    credit == cs.credit;
        }
        else return false;
    }

    /**
     * TODO 计算hash码，使得Course类在HashMap中作为Key时使用的是课程本身的信息
     */
    @Override
    public int hashCode(){
        return new String(coursename+teacher).hashCode();
    }
}
