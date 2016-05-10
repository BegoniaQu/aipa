package com.aipa.svc.common.enume;

import java.util.HashMap;
import java.util.Map;

public enum SexOrient {
	
	男性恋(1),女性恋(2),双性恋(3),无性恋(4);
	
	private int value;
	
	private static Map<Integer,SexOrient> map = new HashMap<>();
	
	static{
		for(SexOrient sex : SexOrient.values()){
			map.put(sex.getValue(), sex);
		}
	}
	
	SexOrient(int sex){
		this.value = sex;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
	public static SexOrient get(Integer arg0){
		SexOrient sex = map.get(arg0);
		return sex;
	}
	
}
