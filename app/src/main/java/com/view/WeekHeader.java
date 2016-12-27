package com.view;

/**
 * Created by aiocac on 2016/12/12.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.scm.calendar.R;

public class WeekHeader extends ViewGroup{
    protected String[] weekString=new String[]{"一","二","三","四","五","六","日"};
    private int today;
    /**
     * To initialize fields
     */
    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public WeekHeader(Context context) {
        super(context);
        Init();
    }
    public WeekHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }
    public WeekHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }
    private void Init(){
        today=0;
        for(int i=0;i<7;i++) {
            HeaderItem hi=new HeaderItem(getContext());
            hi.setText(weekString[i]);
            if(i==today)
                hi.setBackgroundResource(R.drawable.choosed);
            addView(hi);
        }
    }
    public void setToday(int pToday){
        HeaderItem hi=(HeaderItem)getChildAt(today);
        hi.setBackgroundResource(R.drawable.border);
        today=pToday;
        hi=(HeaderItem)getChildAt(today);
        hi.setBackgroundResource(R.drawable.choosed);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(!changed)
            return;
        int count=getChildCount();
        int uWidth=(r-l)/count;
        int Height=b-t;
        for(int i=0;i<count;i++){
            HeaderItem child=(HeaderItem)getChildAt(i);

            child.layout(i*uWidth,0,uWidth*(i+1),Height);
        }
    }
}
