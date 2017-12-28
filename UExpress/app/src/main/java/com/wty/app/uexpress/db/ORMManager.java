package com.wty.app.uexpress.db;

import com.wty.app.uexpress.db.annotation.SqliteAnnotationCache;
import com.wty.app.uexpress.db.annotation.SqliteDao;

import java.util.HashMap;
import java.util.Map;

/**
 * @Decription 管理数据库对象以及缓存对象(改善由于反射造成的性能)
 * @author 吴廷优 on 2016/09/01
 **/
public class ORMManager<T extends BaseDB> {

	private static volatile ORMManager sInstance = null;
	private SqliteAnnotationCache sqliteAnnotationCache;
	private Map<String,T> localDBMap = new HashMap<String,T>();
	private String dbname = "uexpress_db";

	private ORMManager(){
	}

	/**
	 * @Decription 初始化Orm缓存
	 **/
	public static ORMManager getInstance(){
		if (sInstance == null) {
			synchronized (ORMManager.class) {
				if (sInstance == null) {
					sInstance = new ORMManager();
				}
			}
		}
		return sInstance;
	}

	/**
	 * @Desc 设置当前数据库名字
	 **/
	public void setCurrentDBName(String dbname){
		this.dbname = dbname;
	}

	/**
	 * @Decription 关闭所有数据库(退出应用必须调用)
	 **/
	public void closeDB(){
		try {
			for(T db:localDBMap.values()){
				db.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		clearCache();
	}

	public void addSqliteOpenHelper(String xtion_dbname, T helper){
		localDBMap.put(xtion_dbname, helper);
	}

	public boolean isExistSqliteOpenHelper(String xtion_dbname){
		T db = localDBMap.get(xtion_dbname);
        return db != null;
    }

	/**
	 * 功能描述：退出应用必须要调用该方法，用于清除所有缓存对象
	 **/
	private void clearCache(){
		if(sqliteAnnotationCache != null){
			sqliteAnnotationCache.clear();//清掉数据库缓存对象
		}
		SqliteDao.clear();
	}

	SqliteAnnotationCache getSqliteAnnotationCache() {
		if(sqliteAnnotationCache == null){
			synchronized (this){
				if(sqliteAnnotationCache == null){
					sqliteAnnotationCache = new SqliteAnnotationCache();
				}
			}
		}
		return sqliteAnnotationCache;
	}

	T getSqliteHelper(){
		T db = localDBMap.get(dbname);
		if(db == null){
			throw new RuntimeException("please add it first!");
		}
		return db;
	}

	public T getSqliteHelper(String dbname){
		T db = localDBMap.get(dbname);
		return db;
	}
}
