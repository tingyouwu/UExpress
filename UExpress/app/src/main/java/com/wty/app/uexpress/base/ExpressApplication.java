package com.wty.app.uexpress.base;

import android.app.Application;
import com.orhanobut.logger.Logger;

/**
 * @author wty
 * App入口
 **/
public class ExpressApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Logger.init("UExpress")
                .methodCount(10)
				.hideThreadInfo();
	}
}
