package com.wty.app.uexpress.ui;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.widget.navigation.NavigationText;
import com.wty.app.uexpress.widget.navigation.SystemBarTintManager;
import com.wty.app.uexpress.widget.sweet.OnDismissCallbackListener;
import com.wty.app.uexpress.widget.sweet.SweetAlertDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author wty
 * Activity基类
 **/
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;
    protected NavigationText navigation;
    public SweetAlertDialog dialog;
    public SweetAlertDialog toastDialog;

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
        if (toastDialog != null && toastDialog.isShowing()) {
            toastDialog.dismiss();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
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

    /**
     * 在 startActivityForResult 基础上包一层 listener，从而将返回数据放在 listener 的 onResult() 方法中实现
     * @param intent
     * @param onActivityResultListener
     */
    public void startActivityForListener(Intent intent, OnActivityResultListener onActivityResultListener) {
        this.listener = onActivityResultListener;
        startActivityForResult(intent, 1011);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (listener!=null){
                listener.onResult(data);
            }
        }
        listener = null;
    }

    public void dismissAskLoading(final OnDismissCallbackListener callback){
        if(dialog!=null && dialog.isShowing()){
            new CountDownTimer(500,500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    dialog.setTitleText(callback.msg)
                            .showCancelButton(true)
                            .setCancelText("取消")
                            .setConfirmText("确定")
                            .setConfirmClickListener(callback)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .changeAlertType(callback.alertType);

                }
            }.start();
        }
    }

    public void onToast(OnDismissCallbackListener callback){
        if(dialog!=null && dialog.isShowing()){
            dialog.cancel();
        }
        if(toastDialog==null || !toastDialog.isShowing()){
            toastDialog = new SweetAlertDialog(this, callback.alertType);
            toastDialog.show();
        }
        toastDialog.setTitleText(callback.msg)
                    .setConfirmText("确定")
                    .setConfirmClickListener(callback)
                    .changeAlertType(callback.alertType);
    }

    public void onToastSuccess(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onToast(new OnDismissCallbackListener(msg,SweetAlertDialog.SUCCESS_TYPE));
            }
        });
    }

    public void onToastErrorMsg(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onToast(new OnDismissCallbackListener(msg,SweetAlertDialog.ERROR_TYPE));
            }
        });
    }

    /**
     * 即时按下返回键是的次数当进度时
     */
    private int countDestroyDialogKeycodeBack = 0;

    /**
     * 提示进度条 必须在handler或者控件触发 调用才有效
     */
    public void loading(String msg) {
            if (this.isFinishing()) {
                return;
            }
            if (dialog == null || !dialog.isShowing()) {
                countDestroyDialogKeycodeBack = 0;
                dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText(msg);
                dialog.setCancelable(false);
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogs, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            countDestroyDialogKeycodeBack++;
                            if (countDestroyDialogKeycodeBack == 6){
                                dialog.cancel();
                            }
                        }
                        return false;
                    }
                });
                dialog.show();
            }else if(dialog != null && dialog.isShowing()){
                countDestroyDialogKeycodeBack = 0;
                dialog.setTitleText(msg)
                        .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                dialog.setCancelable(false);
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogs, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            countDestroyDialogKeycodeBack++;
                            if (countDestroyDialogKeycodeBack == 6){
                                dialog.cancel();
                            }
                        }
                        return false;
                    }
                });
            }
    }

    public void dismissLoading(){
            if (dialog != null && dialog.isShowing()) {
                CountDownTimer timer = new CountDownTimer(500,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                    }
                };
                timer.start();
            }
    }


    public void dismissLoading(final OnDismissCallbackListener callback){
        if(dialog!=null && dialog.isShowing()){
            new CountDownTimer(500,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    dialog.setTitleText(callback.msg)
                            .showCancelButton(false)
                            .setConfirmText("确定")
                            .setConfirmClickListener(callback)
                            .changeAlertType(callback.alertType);

                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if(keyCode == KeyEvent.KEYCODE_BACK){
                                callback.onCallback();
                            }
                            return false;
                        }
                    });
                }
            }.start();
        }
    }

    private OnActivityResultListener listener;

    protected abstract int getContentLayout();
    protected abstract void initView();
}
