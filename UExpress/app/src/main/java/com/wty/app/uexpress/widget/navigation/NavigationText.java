package com.wty.app.uexpress.widget.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wty.app.uexpress.R;

/**
 * @author Amberllo
 *	默认文字标题actionbar
 */
public class NavigationText extends NavigationContainer {
	
	public NavigationText(Context context) {
		super(context);
	}
	
	public NavigationText(Context context,AttributeSet attr) {
		super(context,attr);
	}

	TextView tv_title;

	@Override
	public View initCenterView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.navigation_text, null);
		tv_title = (TextView)view.findViewById(R.id.navigation_text_title);
		return view;
	}
	
	public NavigationText setTitle(String text){
		tv_title.setText(text);
		return this;
	}
}