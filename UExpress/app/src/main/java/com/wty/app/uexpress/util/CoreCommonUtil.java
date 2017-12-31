package com.wty.app.uexpress.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import rx.functions.Action1;

@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class CoreCommonUtil {

	/**
	 * 使用系统电话 拨号页面
	 * @param context
	 * @param telStr
	 */
	public static void usePhoneCall(final Context context, String telStr){
		final String tel = telStr.replaceAll("[^0-9]","").trim();// 获取电话号码,过滤掉非数字字符
		Uri uri = Uri.parse("tel:" + tel); // 拨打电话号码的URI格式
		final Intent it = new Intent(); // 实例化Intent
		it.setAction(Intent.ACTION_DIAL); // 指定Action
		it.setData(uri); // 设置数据

		if (Build.VERSION.SDK_INT >= 23){
			RxPermissions.getInstance(context)
					.request(Manifest.permission.CALL_PHONE)
					.subscribe(new Action1<Boolean>() {
						@Override
						public void call(Boolean aBoolean) {
							if (aBoolean) {
								context.startActivity(it);
							}
						}
					});
		}else {
			context.startActivity(it);
		}
	}

	/**
	 * 获取版本号(内部识别号) : buildno
     */
	public static int getVersionCode(Context context)
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取版本号
     */
	public static String getVersion(Context context)
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取应用名称
	 * @param context
	 * @return
	 */
	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	public static int getTargetSDKVersion(Context context){
		int targetSdkVersion = 0;
		try {
			final PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			targetSdkVersion = info.applicationInfo.targetSdkVersion;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return targetSdkVersion;
	}

    public static String loadJsonFile(Context context,String fileName){
    	 String json="";
    	 AssetManager am = context.getAssets();
    	 InputStreamReader reader = null;
    	 BufferedReader bufReader = null;
    	 InputStream is = null;
         try {
             is = am.open(fileName);
             reader = new InputStreamReader(is);
             bufReader = new BufferedReader(reader);
             String line="";
             while((line = bufReader.readLine()) != null){
                 json += line;
             }
         } catch (Exception e) {
             e.printStackTrace();
         } finally{
				try {
					if(is!=null){
						is.close();
					}
					if(reader!=null){
						reader.close();
					}
					if(bufReader != null){
						bufReader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
         }
         return json;
    }
    
	/**
	 * 获取设备的 UUID
	 * @param context
	 * @return
     */
	public static String getDeviceUUID(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

	    String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    String uniqueId = deviceUuid.toString();
	    return uniqueId;
	}

	/**
     * 获取设备的品牌
     */
	public static String getDeviceBrand(){
        String brand = Build.BRAND;
        return brand;
    }

    /**
     * 获取设备的型号
     */
    public static String getDeviceModel(){
        String model = Build.MODEL;
        return model;
    }

    /**
     * 获取设备的系统版本
     */
    public static String getDeviceVersion(){
        String version = Build.VERSION.RELEASE;
        return version;
    }

    public static String getManifestMetal(Context context,String key){
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (NameNotFoundException e) {

            e.printStackTrace();
            return "";
        }
    }

	/**
	 * 键盘控制
	 * @param context
	 * @param show
	 * @param editText
     */
	public static void keyboardControl(Context context,boolean show,View editText){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(show){
			//显示键盘
			imm.showSoftInput(editText, 0);
		}else{
			//隐藏键盘
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}
}
