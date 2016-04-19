package com.qy.data.common.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qy
 */
@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Collection{
	String name() default "";
	PartitionType partitionType() default PartitionType.NONE;
	String partitionField() default "";
	int partitionCount() default 100;
}
