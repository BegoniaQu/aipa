/**
 * 
 */
package com.qy.data.common.data.container.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qy
 * @Date 2016
 */
public class DictComprehension {
	
	public static <K, V> Map<K, V> getDictComprehension(Iterable<?> src, ItemExtrator<K, V> extractor){
		if(src == null) return null;
		Map<K, V> resMap = new HashMap<K, V>();
		for (Object object : src) {
			resMap.put(extractor.extractKey(object), extractor.extractValue(object));
		}
		return resMap;
	}
	
	public static interface ItemExtrator<K, V>{
		public K extractKey(Object o);
		public V extractValue(Object o);
	}
}
