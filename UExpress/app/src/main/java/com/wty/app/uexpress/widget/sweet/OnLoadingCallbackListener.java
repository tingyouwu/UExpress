package com.wty.app.uexpress.widget.sweet;

public class OnLoadingCallbackListener implements SweetAlertDialog.OnSweetClickListener {
        public String msg;
        public int alertType = SweetAlertDialog.WARNING_TYPE;
        public OnLoadingCallbackListener(String msg){
            this.msg = msg;
        }

        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            //点击确定不会dismiss  请注意
            onCallback();
        }

        public void onCallback(){
        }

}