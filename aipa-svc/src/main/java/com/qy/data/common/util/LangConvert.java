package com.qy.data.common.util;

import java.util.Set;

/**
 * 
 * @author qy
 *
 * @param <T>
 */
public interface LangConvert<T> {
	T getValue(String orgin);
	T getValue(String orgin, T defValue);
	T getValue(Object orgin);
	Set<T> getValues(Set<String> orgins);
	
	String toValue(T origin);
	Set<String> toValues(Set<T> orgins);
}
