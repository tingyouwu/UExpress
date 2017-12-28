package com.wty.app.uexpress.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DatabaseField {
	
	FieldType Type() default FieldType.VARCHAR;
	
	boolean primaryKey() default false;

	boolean isLongType() default false;//用来区分int 型和 long型
	
	String fieldName() default "";
	
	enum FieldType{
		VARCHAR,INT,REAL
	}
}