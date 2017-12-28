package com.wty.app.uexpress.widget.navigation;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wty.app.uexpress.R;

/**
 * 标题导航栏
 * @author Amberllo
 */
public abstract class NavigationContainer extends LinearLayout{

	LinearLayout layout_center;
	View root;
    LinearLayout layout_right,layout_right_custom;
    NavigationButton btn_left,btn_right,btn_right2;
    LayoutParams layoutParams;
	public NavigationContainer(Context context) {
		super(context);
		init(context);
	}

	public NavigationContainer(Context context,AttributeSet attr) {
		super(context, attr);
		init(context);
	}

	private void init(Context context){
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.navigation_conainer, this);
		root = findViewById(R.id.navigation_root);
		layout_center = (LinearLayout)findViewById(R.id.navigation_layout_center);
        layout_right = (LinearLayout)findViewById(R.id.navigation_layout_right);
        layout_right_custom = (LinearLayout) findViewById(R.id.navigation_layout_customright);
        btn_left = (NavigationBackButton)findViewById(R.id.navigation_btn_left);
		btn_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });

        btn_right = new NavigationButton(context);
        btn_right.setButton("", new OnClickListener() {
            @Override
            public void onClick(View v) {
                //默认给个右按钮
            }
        });
        layout_right.addView(btn_right,layoutParams);

		View centerView = initCenterView();
		if(centerView!=null){

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

			layout_center.removeAllViews();
			layout_center.addView(centerView,layoutParams);
		}

	}

    public <T extends NavigationContainer> T setRightButton(int resource,OnClickListener listener){
        btn_right.setButton(resource, listener);
        layout_right.setVisibility(View.VISIBLE);
        layout_right_custom.setVisibility(View.GONE);
        btn_right.show();
        return (T)this;
    }

    public <T extends NavigationContainer> T setRightButton(String text,OnClickListener listener){
        layout_right.setVisibility(View.VISIBLE);
        layout_right_custom.setVisibility(View.GONE);
        btn_right.show();
        btn_right.setButton(text,listener);
        return (T)this;
    }

    public <T extends NavigationContainer> NavigationContainer setCustomRightButton(NavigationButton button){
        layout_right.setVisibility(View.GONE);
        layout_right_custom.setVisibility(View.VISIBLE);

        if(button!=null){
            layout_right_custom.removeAllViews();
            layout_right_custom.addView(button);
            button.show();
        }
        return this;
    }

	public <T extends NavigationContainer> T addRightButton(int resource,OnClickListener listener){
        NavigationButton button = new NavigationButton(getContext());
        button.setButton(resource, listener);
        layout_right.addView(button,0,layoutParams);
        if(btn_right2==null){
            btn_right2 = button;
        }
        return (T)this;
	}

    public <T extends NavigationContainer> T addRightButton(String text,OnClickListener listener){
        NavigationButton button = new NavigationButton(getContext());
        button.setButton(text, listener);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.rightMargin = 25;
        layout_right.addView(button,0,layoutParams);
        if(btn_right2==null){
            btn_right2 = button;
        }
        return (T)this;
    }

    public <T extends NavigationContainer> T addRightButton(NavigationButton button){
        if(button==null){
            return (T)this;
        }
        layout_right.addView(button,0,layoutParams);
        if(btn_right2==null){
            btn_right2 = button;
        }
        return (T)this;
    }

    public <T extends NavigationContainer> T setRightButton(NavigationButton button){
        if(button==null){
            return (T)this;
        }
        btn_right = button;
        layout_right.removeAllViews();
        layout_right.addView(button,layoutParams);
        return (T)this;
    }

    public NavigationButton getRightButton(){return btn_right;}
    public NavigationButton getRightButton2(){return btn_right2;}

    public NavigationButton getLeftButton(){
        return btn_left;
    }

    @Override
    public View getRootView(){
		return root;
	}
	public <T extends NavigationContainer> T build(){
		return (T)this;
	}
	
	public abstract View initCenterView();


	
}
