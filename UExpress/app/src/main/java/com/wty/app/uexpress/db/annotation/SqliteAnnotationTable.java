package com.wty.app.uexpress.db.annotation;

import android.database.Cursor;
import android.text.TextUtils;

import com.wty.app.uexpress.db.SqliteBaseDALEx;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqliteAnnotationTable {
	
	private String tableName;
	private Class<? extends SqliteBaseDALEx> clazz;
	private List<SqliteAnnotationField> fields;
	private Map<String,SqliteAnnotationField> fieldMaps;

	private String primaryKey;

	public SqliteAnnotationTable(String tableName, Class<? extends SqliteBaseDALEx> clazz) {
		this.tableName = tableName;
		this.clazz = clazz;
	}

	public String getTableName(){
		return tableName;
	}

	public SqliteAnnotationField getField(String name){
		if(fieldMaps==null){
			getFields();
		}
		return fieldMaps.get(name);
	}

	public synchronized List<SqliteAnnotationField> getFields(){

		if(fields==null){
			if(fieldMaps==null){
				fieldMaps = new HashMap<String,SqliteAnnotationField>();
			}
			fields = new ArrayList<SqliteAnnotationField>();

			for(Field f:clazz.getDeclaredFields()){
				DatabaseField dbf = f.getAnnotation(DatabaseField.class);
				if(dbf!=null){
					SqliteAnnotationField saf = new SqliteAnnotationField(f,dbf);
					fields.add(saf);
					fieldMaps.put(saf.getColumnName(),saf);
					if(saf.isPrimaryKey()){
						this.primaryKey = saf.getColumnName();
					}
				}
			}

			if(TextUtils.isEmpty(this.primaryKey)){
				this.primaryKey = SqliteBaseDALEx.PrimaryKey;
			}
		}
		return fields;
	}

	/**
	 * @Decription 获取表列对应的索引
	 **/
	public synchronized Map<String,Integer> getCursorIndex(Cursor cursor){
		Map<String,Integer> index = new HashMap<String,Integer>();
		for (int i = 0; i < cursor.getColumnCount(); i++) {
			String name = cursor.getColumnName(i);
			index.put(name, i);
		}
		return index;
	}

	public String getPrimaryKey() {
		if(TextUtils.isEmpty(primaryKey)){
			getFields();
		}
		return primaryKey;
	}
	
}