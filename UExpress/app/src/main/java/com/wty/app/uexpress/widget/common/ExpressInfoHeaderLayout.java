package com.wty.app.uexpress.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.util.AppImageLoader;
import com.wty.app.uexpress.util.CoreImageURLUtils;
import com.wty.app.uexpress.widget.roundedimageview.RoundedImageView;

/**
 * @author wty
 * 物流信息空布局
 **/
public class ExpressInfoHeaderLayout extends LinearLayout {

	RoundedImageView item_icon;
	TextView item_name,item_remark;

	public ExpressInfoHeaderLayout(Context context){
		this(context, null);
	}

	public ExpressInfoHeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_expressinfo_header, this);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, 0, 0);
		setLayoutParams(lp);
		setPadding(0, 0, 0, 0);
		item_name = (TextView)findViewById(R.id.item_name);
		item_remark = (TextView)findViewById(R.id.item_remark);
		item_icon = (RoundedImageView)findViewById(R.id.item_icon);
	}

	public void setIcon(String url){
		AppImageLoader.displayImage(getContext(), CoreImageURLUtils.ImageScheme.HEADIMG.wrap(url),item_icon);
	}

	public void setName(String name){
		item_name.setText(name);
	}

	public void setRemark(String remark){
		item_remark.setText(remark);
	}
}
