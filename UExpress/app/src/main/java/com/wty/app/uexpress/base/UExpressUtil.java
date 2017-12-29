package com.wty.app.uexpress.base;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.wty.app.uexpress.db.BaseDB;
import com.wty.app.uexpress.db.ORMManager;

/**
 * @author wty
 * 应用工具类
 **/
public class UExpressUtil {

    public static UExpressDB initDataBase(Context context,String dbname){
        BaseDB xtionDB = ORMManager.getInstance().getSqliteHelper(dbname);
        if(xtionDB==null){
            xtionDB = new UExpressDB(context.getApplicationContext(),dbname,
                    UExpressUtil.getDbVersion(context.getApplicationContext()));
            ORMManager.getInstance().setCurrentDBName(dbname);
            ORMManager.getInstance().addSqliteOpenHelper(dbname,xtionDB);
        }else{
            ORMManager.getInstance().setCurrentDBName(dbname);
        }
        return (UExpressDB) xtionDB;
    }

    public static int getDbVersion(Context context) {
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getInt("dbversion");
        } catch (NameNotFoundException e) {

            e.printStackTrace();
            return 0;
        }
    }
}
