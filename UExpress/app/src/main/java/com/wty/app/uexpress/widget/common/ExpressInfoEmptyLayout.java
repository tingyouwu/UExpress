package com.wty.app.uexpress.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.app.uexpress.R;

/**
 * @author wty
 * 物流信息空布局
 **/
public class ExpressInfoEmptyLayout extends LinearLayout {

	TextView tv_emtpy;

	public ExpressInfoEmptyLayout(Context context){
		this(context, null);
	}

	public ExpressInfoEmptyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_expressinfo_emptylayout, this);
		tv_emtpy = (TextView)findViewById(R.id.tv_detail);
	}

	public void setEmptyText(String textup){
		tv_emtpy.setText(textup);
	}

}
