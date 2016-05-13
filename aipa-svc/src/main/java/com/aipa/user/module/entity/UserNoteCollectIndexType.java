package com.aipa.user.module.entity;

import java.util.HashMap;
import java.util.Map;

import com.qy.data.common.exception.CloudPlatformRuntimeException;

public enum UserNoteCollectIndexType {

	user2collectedNote(1),note2collectedUser(2);
	
	private static Map<Integer, UserNoteCollectIndexType> map = new HashMap<Integer, UserNoteCollectIndexType>();
	static{
		for(UserNoteCollectIndexType deviceInstanceIndexType : UserNoteCollectIndexType.values()){
			map.put(deviceInstanceIndexType.getValue(), deviceInstanceIndexType);
		}
	}

	private int value;
	
	private UserNoteCollectIndexType(int value){
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public static UserNoteCollectIndexType get(String name){
		try {
			return UserNoteCollectIndexType.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	} 
	
	public static UserNoteCollectIndexType convert(int value){
		UserNoteCollectIndexType res = map.get(value);
		if(res == null) throw new CloudPlatformRuntimeException("unsupport note index type " + value);
		return res;
	}
}
