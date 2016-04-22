/**
 * 
 */
package com.qy.data.common.data.container.impl;

import java.util.LinkedList;
import java.util.List;

/**
 * @author qy
 * @Date 2016
 */
public class ListComprehension {
	
	public static <T> List<T> getListComprehension(Iterable<?> src, ItemExtrator<T> extractor){
		if(src == null) return null;
		List<T> resList = new LinkedList<T>();
		for (Object object : src) {
			resList.add(extractor.extractItem(object));
		}
		return resList;
	}
	
	public static interface ItemExtrator<T>{
		public T extractItem(Object o);
	}
}
