package com.wty.app.uexpress.task;

import android.content.DialogInterface;

import com.wty.app.uexpress.ui.BaseActivity;
import com.wty.app.uexpress.widget.sweet.OnDismissCallbackListener;
import com.wty.app.uexpress.widget.sweet.SweetAlertDialog;


public abstract class SimpleDialogTask extends SimpleTask{
    BaseActivity context;
    String loadingMessage;
    OnDismissCallbackListener dismissCallback;
    OnDismissCallbackListener dismissAskCallback;

    public SimpleDialogTask(BaseActivity context){
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(context!=null){
            context.loading(loadingMessage);
            context.dialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            if (!isCancelled()) {
                                cancel(true);
                            }
                        }
                    });
        }
    }

    @Override
    protected Object doInBackground(String... params) {
        return onAsync();
    }

    @Override
    protected void onPostExecute(Object obj) {
        onResult(obj);
        if(dismissAskCallback != null){
            context.dismissAskLoading(dismissAskCallback);
        }else if(dismissCallback!=null){
            context.dismissLoading(dismissCallback);
        }else{
            context.dismissLoading();
        }
    }

    public void setDismissCallback(String msg,int type){
        this.dismissCallback = new OnDismissCallbackListener(msg, type);
    }

    public void setDimissAskCallback(OnDismissCallbackListener dismissCallback){
        this.dismissAskCallback = dismissCallback;
    }

    public void setDismissCallback(String msg){
        this.setDismissCallback(msg, SweetAlertDialog.SUCCESS_TYPE);
    }

    public void setDismissCallback(OnDismissCallbackListener dismissCallback){
        this.dismissCallback = dismissCallback;
    }

    public void startTask(String loadingMessage) {
        this.loadingMessage = loadingMessage;
        super.startTask();
    }

    public void startTask(String loadingMessage,OnDismissCallbackListener dismissCallback) {
        this.loadingMessage = loadingMessage;
        this.dismissCallback = dismissCallback;
        super.startTask();
    }

    public abstract Object onAsync();

    public abstract void onResult(Object obj);
}
