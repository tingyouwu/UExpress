package com.wty.app.uexpress.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class CoreScreenUtil {

	public static int SCREEN_WIDTH = 460;// 屏幕宽度

	/**
	 * 获取屏幕宽度
	 **/
	public static int getSCREEN_WIDTH(Context context) {
		if (context != null && SCREEN_WIDTH == 460) {
			DisplayMetrics displayMetrics = new DisplayMetrics();
			Activity a = (Activity) context;
			a.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			SCREEN_WIDTH = displayMetrics.widthPixels;// 初始化屏幕宽度
		}
		return SCREEN_WIDTH;
	}

	/**
	 * 将dp转换成不同分辨率下的px 适配各种机型
	 *
	 * @param context
	 * @param dpValue
	 * @return px
	 */
	public static int dip2px(Context context, double dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 功能描述：根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕分辨率
	 *
	 * @param context
	 * @return
	 **/
	public static DisplayMetrics getDisplayMetrics(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}

	/**
	 * 功能描述：获取屏幕宽度
	 **/
	public static int getScreenWidth(Context context) {
		DisplayMetrics metrics = getDisplayMetrics(context);
		return metrics.widthPixels;
	}

	/**
	 * 功能描述：获取屏幕高度
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics metrics = getDisplayMetrics(context);
		return metrics.heightPixels;
	}

	/**
	 * 获取该手机屏幕宽高
	 *
	 * @return
	 */
	public static DisplayMetrics getWidHei(Activity context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics;
	}

	/**
	 * 获取状态栏高度
	 *
	 * @param context context
	 * @return 状态栏高度
	 */
	public static int getStatusBarHeight(Context context) {
		// 获得状态栏高度
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		return context.getResources().getDimensionPixelSize(resourceId);
	}
}
