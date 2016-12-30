package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.scm.calendar.R;
import com.util.Constant.*;
import com.util.coursetable.Course;
import com.util.coursetable.CourseInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by aiocac on 2016/12/19.
 */

public class CourseTableContainer extends ViewGroup {
    private int lessonNum;
    private static final int[] drawable={R.drawable.course_item_01,R.drawable.course_item_02,R.drawable.course_item_03
            ,R.drawable.course_item_04,R.drawable.course_item_05,R.drawable.course_item_06};
    private int colorIndex;
    private int week;
    Map<Course,Integer> ColorMap;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public CourseTableContainer(Context context) {
        super(context);
        Init();
    }
    public void setWeek(int pWeek){
        week=pWeek;
    }

    public CourseTableContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public CourseTableContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /*Cannot invalidate if this statement is added
        if(!changed)
            return;
        */
        int uWidth=(r-l)/7;     //a unit's width
        int uHeight=(b-t)/lessonNum;    //a unit's height
        for(int i=0,size=getChildCount();i<size;i++) {
            View view = getChildAt(i);
            if (view instanceof CourseItem) {
                CourseItem ci = (CourseItem)view;
                _WEEKDAY wd = ci.getWeekday();
                int segStart = ci.getSegStart();
                int segEnd = ci.getSegEnd();
                int thisl = wd.ordinal() * uWidth;
                int thist = (segStart - 1) * uHeight;
                int thisb = segEnd * uHeight;
                ci.layout(thisl, thist, thisl + uWidth, thisb);
            }
            else if(view instanceof TextView&&size==1){
                view.layout(0,0,r-l,b-t);
            }
            else{
                view.layout(0,0,0,0);
            }
        }
    }

    private void Init() {
        colorIndex=0;
        ColorMap=new HashMap<Course,Integer>();
        setLessonNum(13);
        week=1;
    }
    public void setLessonNum(int pln){
        lessonNum=pln;
    }
    public void addItem(CourseInstance pci){
        if(pci.C.getWeekStart()>week||pci.C.getWeekEnd()<week)
            return;
        CourseItem courseItem=new CourseItem(getContext());
        courseItem.setCourseInstance(pci);
        if(!ColorMap.containsKey(pci.C)){
            ColorMap.put(pci.C,colorIndex);
            colorIndex=(colorIndex+1)%drawable.length;
        }
        courseItem.setBackgroundResource(drawable[ColorMap.get(pci.C)]);
        courseItem.setTextSize(10);
        addView(courseItem);
    }
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
}
