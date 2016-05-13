package com.aipa.svc.common.enume;

import java.util.HashMap;
import java.util.Map;

public enum ZanType {

	note(1),comment(2);
	
	private int value;
	
	private static Map<Integer,ZanType> map = new HashMap<>();
	
	static{
		for(ZanType sex : ZanType.values()){
			map.put(sex.getValue(), sex);
		}
	}
	
	ZanType(int sex){
		this.value = sex;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
	public static ZanType get(Integer arg0){
		ZanType sex = map.get(arg0);
		return sex;
	}
	
}
