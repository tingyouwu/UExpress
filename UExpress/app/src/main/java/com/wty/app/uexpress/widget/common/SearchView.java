package com.wty.app.uexpress.widget.common;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.util.CoreCommonUtil;

public class SearchView extends LinearLayout{
	
	public static int SEARCH_EMPTY = 0xb;
	public static int SEARCH_CHANGE = 0xa;
	OnSearchListener searchListener;
    View layout_operator;
	TextView tv_operator;
	ProgressBar pb_loading;
	EditText editText;
	ImageButton clearButton;


	public SearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SearchView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context){
		
		// 以下代码实现动态加载xml布局文件
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		// 获取LAYOUT_INFLATER_SERVICE，实例化LayoutInflater，实现动态加载布局
		li = (LayoutInflater) context.getSystemService(infService);
		li.inflate(R.layout.layout_search, this);
		
		editText = (EditText)findViewById(R.id.search_content);
		editText.clearFocus();
		editText.addTextChangedListener(watcher);
		
		clearButton = (ImageButton)findViewById(R.id.search_btn_del);
		clearButton.setVisibility(View.GONE);
		clearButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                editText.setText("");
            }
        });
		tv_operator = (TextView)findViewById(R.id.search_operator);
		pb_loading = (ProgressBar)findViewById(R.id.search_loading);
        layout_operator = findViewById(R.id.search_operatorlayout);
	}

	public EditText getEditText(){
	    return editText;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.what==SEARCH_CHANGE){
				final String searchContent = editText.getText().toString();
				if(searchContent!=null && searchContent.length()!=0){
				    clearButton.setVisibility(View.VISIBLE);
				}
				if(searchListener!=null){
				    searchListener.onSearchChange(searchContent);
				}
			}else if(msg.what==SEARCH_EMPTY){
				clearButton.setVisibility(View.GONE);
				if(searchListener!=null){
					searchListener.onSearchEmpty();
				}
			}
		}
	};

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (0 < count || 0 < before) {
				 handler.sendEmptyMessageDelayed(SEARCH_CHANGE, 200);
			}
			if (start == 0 && count == 0) {
				handler.sendEmptyMessageDelayed(SEARCH_EMPTY,100);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable arg0) {
		}
	};

	public void setOnSearchListener(OnSearchListener listener){
		this.searchListener = listener;
	}

	public interface OnSearchListener{
		void onSearchChange(String content);
		void onSearchEmpty();
	}

	public void clearText(){editText.setText("");}

	public void setHint(String hint){
		editText.setHint(hint);
	}

    public void setTextButton(String text,final OnClickListener listener){
        layout_operator.setVisibility(View.VISIBLE);
        tv_operator.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
				CoreCommonUtil.keyboardControl(getContext(),false,  v);
                listener.onClick(v);
            }
        });
        tv_operator.setText(text);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textview, int code, KeyEvent key) {
                if (code == EditorInfo.IME_ACTION_SEARCH) {
                    if(listener!=null)listener.onClick(textview);
                }
                return false;
            }
        });
    }

	public void setMaxLength(int max){
		InputFilter[] filters = {new LengthFilter(max)};  
		getEditText().setFilters(filters); 
	}
	
	public void setLoading(boolean isLoading){
		if(isLoading){
			this.tv_operator.setVisibility(View.GONE);
			this.pb_loading.setVisibility(View.VISIBLE);
		}else{
			this.tv_operator.setVisibility(View.VISIBLE);
			this.pb_loading.setVisibility(View.GONE);
		}
	}
	
}
