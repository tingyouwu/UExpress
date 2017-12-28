package com.wty.app.uexpress.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.widget.navigation.NavigationText;
import com.wty.app.uexpress.widget.navigation.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author wty
 * Activity基类
 **/
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;
    protected NavigationText navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        unbinder = ButterKnife.bind(this);
        initStatusBar();
        initView();
        setNavigation(getDefaultNavigation());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @TargetApi(19)
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.bottom_click);
        } else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bottom_click));
        }
    }

    public NavigationText getDefaultNavigation(){
        if(navigation==null){
            navigation = new NavigationText(this);
        }
        return navigation;
    }

    /**
     * 动态设置actionbar
     */
    public void setNavigation(View navigation){
        ActionBar actionBar = getSupportActionBar();
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(navigation,layoutParams);
        if(navigation instanceof  NavigationText) {
            this.navigation = (NavigationText) navigation;
        }
        actionBar.show();
    }

    protected abstract int getContentLayout();
    protected abstract void initView();
}
