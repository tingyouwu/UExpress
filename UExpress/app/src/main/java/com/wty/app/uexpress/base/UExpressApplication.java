package com.wty.app.uexpress.base;

import android.app.Application;
import com.orhanobut.logger.Logger;

/**
 * @author wty
 * App入口
 **/
public class UExpressApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		UExpressUtil.initDataBase(this,"UExpress_db");
		Logger.init("UExpress")
                .methodCount(10)
				.hideThreadInfo();
	}
}
