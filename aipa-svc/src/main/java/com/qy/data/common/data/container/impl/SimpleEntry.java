package com.qy.data.common.data.container.impl;

import java.io.Serializable;
import java.util.Map;

import com.qy.data.common.data.container.Entry;

/**
 * 
 * @author qy
 *
 * @param <K>
 * @param <V>
 */
public class SimpleEntry<K, V> implements Entry<K, V>, Map.Entry<K, V>, Serializable {
	
	private static final long serialVersionUID = 8610353566062871001L;
	
	private K k;
	private V v;
	
	public SimpleEntry(){}
	
	public SimpleEntry(K key, V value){
		k = key;
		v = value;
	}

	@Override
	public K getKey() {
		return k;
	}

	@Override
	public V getValue() {
		return v;
	}

	@Override
	public V setValue(V value) {
		this.v = value;
		return v;
	}

	@Override
	public K setKey(K key) {
		k = key;
		return k;
	}

	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		else if(o instanceof Entry){
			@SuppressWarnings("rawtypes")
			Entry e = (Entry)o;
			boolean res1 = k == null ? e.getKey() == null : k.equals(e.getKey());
			if(!res1) return res1;
			else return v == null ? e.getValue() == null : v.equals(e.getValue());
		}else if(o instanceof Map.Entry){
			@SuppressWarnings("rawtypes")
			Map.Entry e = (Map.Entry)o;
			boolean res1 = k == null ? e.getKey() == null : k.equals(e.getKey());
			if(!res1) return res1;
			else return v == null ? e.getValue() == null : v.equals(e.getValue());
		}else return super.equals(o);
	}
	
	@Override
	public String toString(){
		return String.format("Entry: <%s, %s>",  getKey(), getValue());
	}
}
