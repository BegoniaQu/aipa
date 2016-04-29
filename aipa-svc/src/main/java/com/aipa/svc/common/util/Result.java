package com.aipa.svc.common.util;

public class Result {

	
	private Object data;
	
	private Integer err;
	
	private String msg;
	
	

	public Result(Object data, Integer err, String msg) {
		this.data = data;
		this.err = err;
		this.msg = msg;
	}
	
	
	
	public Object getData() {
		return data;
	}


	public Integer getErr() {
		return err;
	}


	public String getMsg() {
		return msg;
	}

	
	
}
