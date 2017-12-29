package com.wty.app.uexpress.base;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wty.app.uexpress.db.BaseDB;

/**
 * 应用数据库
 **/
public class UExpressDB extends BaseDB {

	public UExpressDB(Context context, String dbname,int dbVersion) {
		super(context, dbname,dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i1) {
	}
}
