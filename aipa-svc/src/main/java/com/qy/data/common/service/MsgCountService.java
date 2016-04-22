package com.qy.data.common.service;

public interface MsgCountService {
	public void incre(String key);

	public long get(String key);
	
	public void del(String key);
}
