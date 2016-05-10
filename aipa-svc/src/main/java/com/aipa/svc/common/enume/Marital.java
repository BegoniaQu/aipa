package com.aipa.svc.common.enume;

import java.util.HashMap;
import java.util.Map;

public enum Marital {

	未婚(1),已婚(2);
	
	private int value;
	
	private static Map<Integer,Marital> map = new HashMap<>();
	
	static{
		for(Marital sex : Marital.values()){
			map.put(sex.getValue(), sex);
		}
	}
	
	Marital(int sex){
		this.value = sex;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
	public static Marital get(Integer arg0){
		Marital sex = map.get(arg0);
		return sex;
	}
	
}
