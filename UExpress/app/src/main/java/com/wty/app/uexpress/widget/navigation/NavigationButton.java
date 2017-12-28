package com.wty.app.uexpress.widget.navigation;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.app.uexpress.R;

public class NavigationButton extends LinearLayout {
    private ImageView imageView;
    private TextView textView;
    private LinearLayout root;
    public NavigationButton(Context context){
        super(context);
        init();
    }

    public NavigationButton(Context context, AttributeSet attr){
        super(context,attr);
        init();
    }


    private void init(){

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_navigation_button,this);
        textView = (TextView)findViewById(R.id.navigation_btn_tv);
        imageView = (ImageView)findViewById(R.id.navigation_btn_img);
        root = (LinearLayout)findViewById(R.id.navigation_btn_root);
    }

    public final void setImageResource(int resource){
        this.imageView.setImageResource(resource);
    }

    public final void setText(String text){
        this.textView.setText(text);
    }

    protected void setRootGravity(int gravity){
        root.setGravity(gravity);
    }


    public void setButton(int resource,OnClickListener listener){
        setOnClickListener(listener);
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(resource);
    }

    public void setButton(String text,OnClickListener listener){
        setOnClickListener(listener);
        textView.setText(text);
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
    }

    public void show(){
        this.setVisibility(View.VISIBLE);
        this.setClickable(true);
    }

    public void hide(){
        hide(false);
    }

    public void hide(boolean isGone){
        if(isGone){
            this.setVisibility(View.GONE);
        }else{
            this.setVisibility(View.INVISIBLE);
        }
        this.setClickable(false);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        if (clickable){
            textView.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            textView.setTextColor(Color.parseColor("#e6e6e6"));//灰色
        }
    }
}