package com.aipa.svc.common.enume;

import java.util.HashMap;
import java.util.Map;

public enum ImgBusiType {

	headpic(1),notepic(2);
	
	private int value;
	
	private static Map<Integer,ImgBusiType> map = new HashMap<>();
	
	static{
		for(ImgBusiType sex : ImgBusiType.values()){
			map.put(sex.getValue(), sex);
		}
	}
	
	ImgBusiType(int sex){
		this.value = sex;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
	public static ImgBusiType get(Integer arg0){
		ImgBusiType sex = map.get(arg0);
		return sex;
	}
	
}
