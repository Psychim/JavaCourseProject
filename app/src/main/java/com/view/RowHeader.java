package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aiocac on 2016/12/19.
 */

/**
 * 行头，显示节数
 */
public class RowHeader extends ViewGroup {
    /**
     * 每天总节数
     */
    private int lessonNum;
    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public RowHeader(Context context) {
        super(context);
        Init();
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
    public RowHeader(Context context, AttributeSet attrs) {
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
    public RowHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed==false)
            return;
        int count=getChildCount();
        int Width=r-l;
        int uHeight=(b-t)/count;
        for(int i=0;i<count;i++){
            View child=getChildAt(i);
            child.layout(0,uHeight*i,Width,uHeight*(i+1));
        }
    }

    private void Init(){
        setLessonNum(13);
    }
    public void setLessonNum(int num){
        lessonNum=num;
        removeAllViews();
        for(int i=1;i<=num;i++){
            HeaderItem hi=new HeaderItem(getContext());
            hi.setText(Integer.toString(i));
            addView(hi);
        }
    }
}
