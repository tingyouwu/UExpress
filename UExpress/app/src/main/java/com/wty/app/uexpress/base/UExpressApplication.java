package com.wty.app.uexpress.base;

import android.app.Application;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * @author wty
 * App入口
 **/
public class UExpressApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
//		if (LeakCanary.isInAnalyzerProcess(this)) {
//			// This process is dedicated to LeakCanary for heap analysis.
//			// You should not init your app in this process.
//			return;
//		}
//		LeakCanary.install(this);
		UExpressUtil.initDataBase(this,"UExpress_db");
		Logger.init("UExpress")
                .methodCount(10)
				.hideThreadInfo();
	}
}
