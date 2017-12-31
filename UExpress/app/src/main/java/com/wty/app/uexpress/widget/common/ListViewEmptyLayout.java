package com.wty.app.uexpress.widget.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.app.uexpress.R;

/**
 * @author wty
 * 空内容布局
 **/
public class ListViewEmptyLayout extends LinearLayout {

	TextView tv_up;
    TextView tv_down;
    ImageView iv;
	TextView tv_empty;
	OnEmptyListener listener;

	public ListViewEmptyLayout(Context context){
		this(context, null);
	}

	public ListViewEmptyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_listview_empty, this);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.setMargins(0, 0, 0, 0);
		setLayoutParams(lp);
        tv_up = (TextView)findViewById(R.id.listview_empty_textup);
        tv_down = (TextView)findViewById(R.id.listview_empty_textdown);
        iv = (ImageView)findViewById(R.id.listview_empty_img);
		tv_empty = (TextView)findViewById(R.id.tv_refresh);
		tv_empty.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener != null) {
					setVisibility(View.GONE);
					listener.onEmptyClick();
				}
			}
		});
	}

	public void setEmptyText(String textup){
        tv_up.setText(textup);
	}

    public void setEmptyText(String textup,String textdown){
        tv_up.setText(textup);
		if(!TextUtils.isEmpty(textdown)){
			tv_down.setText(textdown);
			tv_down.setVisibility(View.VISIBLE);
		}
    }

    public void setEmptyLayout(String textup,String textdown,int resource){
        iv.setImageResource(resource);
        tv_up.setText(textup);
        tv_down.setText(textdown);
    }

	public void setSearchText(String text){
		iv.setImageResource(R.mipmap.img_search_empty);
        tv_up.setText(text);
        tv_down.setVisibility(View.GONE);
	}

	public void setOnEmptyListener(OnEmptyListener listener){
		tv_empty.setVisibility(View.VISIBLE);
		this.listener = listener;
	}

	public interface OnEmptyListener {
		void onEmptyClick();
	}

}
