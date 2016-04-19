/**
 * 
 */
package com.qy.data.common.annotation.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * derived 为true意味着 需要额外（向系统内/外）查询数据，而不是即取即用 (简单的数据格式转换不算...)
 * external 为true表示 需要向外部系统请求的数据，必然意味着 derived
 * optional 为true表示 允许没有的数据
 * userSpecific 为true表示 用户相关的数据，一般为derived
 * @author yonka
 * @Date 2015-5-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BeanField {
	String name();
	boolean isOptional() default false;
	boolean isExternal() default false;
	boolean isDerived() default false;
	boolean isUserSpecific() default false;
}
