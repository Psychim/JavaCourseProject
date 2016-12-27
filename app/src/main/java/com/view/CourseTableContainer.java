package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.util.Constant.*;
import com.util.coursetable.Course;
import com.util.coursetable.CourseInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aiocac on 2016/12/19.
 */

public class CourseTableContainer extends ViewGroup {
    private int lessonNum;
    private static final int[] COLORS=new int[]{Color.RED,Color.BLUE,Color.YELLOW,Color.GREEN};
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
    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p>
     * <p>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #View(Context, AttributeSet, int)
     */
    public CourseTableContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @see #View(Context, AttributeSet)
     */
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
        for(int i=0,size=getChildCount();i<size;i++){
            CourseItem ci=(CourseItem)getChildAt(i);
            _WEEKDAY wd=ci.getWeekday();
            int segStart=ci.getSegStart();
            int segEnd=ci.getSegEnd();
            int thisl=wd.ordinal()*uWidth;
            int thist=(segStart-1)*uHeight;
            int thisb=segEnd*uHeight;
            ci.layout(thisl, thist, thisl+uWidth, thisb);
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
            colorIndex=(colorIndex+1)%COLORS.length;
        }
        courseItem.setBackgroundColor(COLORS[ColorMap.get(pci.C)]);
        courseItem.setTextSize(10);
        addView(courseItem);
    }
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
}
