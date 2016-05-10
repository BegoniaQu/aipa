package com.aipa.svc.common.enume;

import java.util.HashMap;
import java.util.Map;

public enum Sex {

	男(1),女(2);
	
	private int value;
	
	private static Map<Integer,Sex> map = new HashMap<>();
	
	static{
		for(Sex sex : Sex.values()){
			map.put(sex.getValue(), sex);
		}
	}
	
	Sex(int sex){
		this.value = sex;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
	public static Sex get(Integer arg0){
		Sex sex = map.get(arg0);
		return sex;
	}
	
}
