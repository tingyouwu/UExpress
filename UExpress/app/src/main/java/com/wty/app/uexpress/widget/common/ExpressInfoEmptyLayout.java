package com.wty.app.uexpress.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.util.CoreCommonUtil;
import com.wty.app.uexpress.util.CorePhotoUtils;

/**
 * @author wty
 * 物流信息空布局
 **/
public class ExpressInfoEmptyLayout extends LinearLayout {

	TextView tv_phone;

	public ExpressInfoEmptyLayout(Context context){
		this(context, null);
	}

	public ExpressInfoEmptyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_expressinfo_emptylayout, this);
        tv_phone = (TextView)findViewById(R.id.tv_phone);
		tv_phone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CoreCommonUtil.usePhoneCall(getContext(),tv_phone.getText().toString());
			}
		});
	}

	public void setEmptyText(String textup){
        tv_phone.setText(textup);
	}

}
