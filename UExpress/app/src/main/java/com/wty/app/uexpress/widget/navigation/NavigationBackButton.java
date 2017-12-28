package com.wty.app.uexpress.widget.navigation;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.wty.app.uexpress.R;

public class NavigationBackButton extends NavigationButton {

    public NavigationBackButton(Context context){
        this(context,null);
    }

    public NavigationBackButton(Context context, AttributeSet attr){
        super(context, attr);
        setRootGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        setButton(R.mipmap.actionbar_back, new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(v.getContext() instanceof Activity){
                    ((Activity)v.getContext()).finish();
                }
            }
        });
    }
}