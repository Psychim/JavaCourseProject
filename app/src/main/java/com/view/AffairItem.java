package com.view;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.scm.calendar.R;
import com.util.affair.Affair;

/**
 * Created by aiocac on 2016/12/29.
 */

public class AffairItem extends RelativeLayout {
    Affair affair;
    public AffairItem(Context context) {
        super(context);
        Init();
    }

    public AffairItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public AffairItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }
    private void Init(){
        LayoutInflater.from(getContext()).inflate(R.layout.affair_item,this,true);
    }
    public void setAffair(Affair pAffair){
        affair=pAffair;
        TextView remindtimetext=(TextView)findViewById(R.id.remindTimeText);
        remindtimetext.setText(affair.getTimeString());
        TextView affairtext=(TextView)findViewById(R.id.affairText);
        affairtext.setText(affair.getContent());
    }
}
