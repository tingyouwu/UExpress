package com.wty.app.uexpress.base;

import android.content.Context;
import android.content.Intent;

import com.wty.app.uexpress.ui.fragment.ExpressCheckFragment;
import com.wty.app.uexpress.ui.fragment.ExpressDeleteFragment;
import com.wty.app.uexpress.ui.fragment.ExpressUnCheckFragment;
import com.wty.app.uexpress.ui.fragment.HomeFragment;

import static com.wty.app.uexpress.base.UExpressConstant.TAG_FRAGMENT;

/***
 * fragment 切换器
 */
public class FragmentObserver {

	/**
	 * 回到首页回收站
	 **/
	public static void goToHomeDeletePage(Context context){
		goToHomePage(context);
		Intent intentForUncheck = new Intent(BroadcastConstants.CHANGE_FRAGMENT_TAB);
		intentForUncheck.putExtra(TAG_FRAGMENT, ExpressDeleteFragment.TAG);
		context.sendBroadcast(intentForUncheck);
	}

	/**
	 * 回到首页未签收
	 **/
	public static void goToHomeUnCheckPage(Context context){
		goToHomePage(context);
		Intent intentForUncheck = new Intent(BroadcastConstants.CHANGE_FRAGMENT_TAB);
		intentForUncheck.putExtra(TAG_FRAGMENT, ExpressUnCheckFragment.TAG);
		context.sendBroadcast(intentForUncheck);
	}

	/**
	 * 回到首页
	 **/
	public static void goToHomePage(Context context){
		Intent intentForHome = new Intent(BroadcastConstants.CHANGE_HOME_TAB);
		intentForHome.putExtra(TAG_FRAGMENT, HomeFragment.TAG);
		context.sendBroadcast(intentForHome);
	}

	/**
	 * 回到首页签收
	 **/
	public static void goToHomeCheckPage(Context context){
		goToHomePage(context);
		Intent intentForUncheck = new Intent(BroadcastConstants.CHANGE_FRAGMENT_TAB);
		intentForUncheck.putExtra(TAG_FRAGMENT, ExpressCheckFragment.TAG);
		context.sendBroadcast(intentForUncheck);
	}

}