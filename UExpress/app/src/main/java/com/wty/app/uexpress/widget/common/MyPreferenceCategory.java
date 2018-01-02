package com.wty.app.uexpress.widget.common;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

//自定义PreferenceCategory类，可以设置PreferenceCategory的title的字体大小、颜色，控制大小写等。
public class MyPreferenceCategory extends PreferenceCategory {
    public MyPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            //设置title文本不全为大写
            tv.setAllCaps(false);
            //设置title文本的颜色
            tv.setTextColor(Color.parseColor("#176ce3"));
        }
    }
}
