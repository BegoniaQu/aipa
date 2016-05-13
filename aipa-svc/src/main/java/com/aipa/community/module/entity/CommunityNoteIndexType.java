package com.aipa.community.module.entity;

import java.util.HashMap;
import java.util.Map;

import com.qy.data.common.exception.CloudPlatformRuntimeException;

public enum CommunityNoteIndexType {

	user2note(1),category2note(2);
	
	private static Map<Integer, CommunityNoteIndexType> map = new HashMap<Integer, CommunityNoteIndexType>();
	static{
		for(CommunityNoteIndexType deviceInstanceIndexType : CommunityNoteIndexType.values()){
			map.put(deviceInstanceIndexType.getValue(), deviceInstanceIndexType);
		}
	}

	private int value;
	
	private CommunityNoteIndexType(int value){
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public static CommunityNoteIndexType get(String name){
		try {
			return CommunityNoteIndexType.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	} 
	
	public static CommunityNoteIndexType convert(int value){
		CommunityNoteIndexType res = map.get(value);
		if(res == null) throw new CloudPlatformRuntimeException("unsupport note index type " + value);
		return res;
	}
}
