package com.wty.app.uexpress.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.util.CoreScreenUtil;

/**
 * 红点图标
 */
public class UnreadView extends AppCompatTextView {

	private final static int MaxCount = 99;

	public UnreadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setGravity(Gravity.CENTER);
		setTextSize(10);
        setMinWidth(CoreScreenUtil.dip2px(context, 18));
        setMinHeight(CoreScreenUtil.dip2px(context, 18));
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.UnreadView);
        int unread = ta.getInt(R.styleable.UnreadView_unread, 0);
        setTextColor(getResources().getColor(R.color.white));
        setUnread(unread);
        ta.recycle();
	}

	public void setUnread(int unread){
		if(unread<=0){
			setVisibility(View.GONE);
			setText("0");
		}else{
			setVisibility(View.VISIBLE);
			if(unread>MaxCount){
				setText("99+");
                setMinWidth(CoreScreenUtil.dip2px(getContext(), 30));
                setBackgroundResource(R.drawable.circle_red2);
			}else{
                setMinWidth(CoreScreenUtil.dip2px(getContext(), 18));
                setBackgroundResource(R.drawable.circle_red);
				setText(""+unread);
			}
		}
	}
}
