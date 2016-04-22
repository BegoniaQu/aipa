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
public @interface Column {
	String name() default "";
	boolean forPartition() default false;
	int partitionCount() default 100;
	boolean inUniqueKey() default false;
}
