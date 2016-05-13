package com.aipa.community.module.entity;

import java.util.HashMap;
import java.util.Map;

import com.qy.data.common.exception.CloudPlatformRuntimeException;

public enum GoodClickIndexType {

	note2good(1),comment2good(2);
	
	
	private static Map<Integer, GoodClickIndexType> map = new HashMap<Integer, GoodClickIndexType>();
	static{
		for(GoodClickIndexType deviceInstanceIndexType : GoodClickIndexType.values()){
			map.put(deviceInstanceIndexType.getValue(), deviceInstanceIndexType);
		}
	}

	private int value;
	private GoodClickIndexType(int value){
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public static GoodClickIndexType get(String name){
		try {
			return GoodClickIndexType.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	} 
	
	public static GoodClickIndexType convert(int value){
		GoodClickIndexType res = map.get(value);
		if(res == null) throw new CloudPlatformRuntimeException("unsupport goodClick index type " + value);
		return res;
	}

}
