package com.util.coursetable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by aiocac on 2016/12/12.
 */

/**
 * 课程表
 */
public class CourseTable implements Iterable<CourseInstance> {
    private Set<CourseInstance> courses;
    private String stuID;
    private String acaYear;
    public void setAcaYear(String pAcaYear){ acaYear=pAcaYear;}
    public String getAcaYear(){return acaYear;}
    public void setStuID(String pStuID){
        stuID=pStuID;
    }
    public String getStuID(){
        return stuID;
    }
    public CourseTable(){
        courses=new HashSet<CourseInstance>();
    }
    public void add(CourseInstance pci){
        courses.add(pci);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<CourseInstance> iterator() {
        return courses.iterator();
    }
}
