package com.aipa.community.module.entity;

import java.util.HashMap;
import java.util.Map;

import com.qy.data.common.exception.CloudPlatformRuntimeException;

public enum CommunityNoteCommentIndexType {

	note2comment(1),comment2reply(2);
	
	
	private static Map<Integer, CommunityNoteCommentIndexType> map = new HashMap<Integer, CommunityNoteCommentIndexType>();
	static{
		for(CommunityNoteCommentIndexType deviceInstanceIndexType : CommunityNoteCommentIndexType.values()){
			map.put(deviceInstanceIndexType.getValue(), deviceInstanceIndexType);
		}
	}

	private int value;
	
	private CommunityNoteCommentIndexType(int value){
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public static CommunityNoteCommentIndexType get(String name){
		try {
			return CommunityNoteCommentIndexType.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	} 
	
	public static CommunityNoteCommentIndexType convert(int value){
		CommunityNoteCommentIndexType res = map.get(value);
		if(res == null) throw new CloudPlatformRuntimeException("unsupport comment index type " + value);
		return res;
	}
}
