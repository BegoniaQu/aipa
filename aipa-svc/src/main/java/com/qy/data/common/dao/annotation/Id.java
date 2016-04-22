package com.qy.data.common.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qy
 *
 */
@Target(value={ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Id{
	String name() default "";
	public boolean forPartition() default false;
	public int partitionCount() default 100;
	public boolean inUniqueKey() default false;
}
