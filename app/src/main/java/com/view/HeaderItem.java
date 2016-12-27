package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.example.scm.calendar.R;

/**
 * Created by aiocac on 2016/12/26.
 */

public class HeaderItem extends TextView {
    public HeaderItem(Context context) {
        super(context);
        Init();
    }

    public HeaderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public HeaderItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }
    private void Init(){
        setBackgroundResource(R.drawable.border);
        setGravity(Gravity.CENTER);
    }
    @Override
    public void onDraw(Canvas canvas){
        int width=getWidth();
        int height=getHeight();
        int minSize;
        if(width<height)
            minSize=width/3;
        else minSize=height/3;
        setTextSize(minSize/2);
        super.onDraw(canvas);
    }
}
