package com.qy.data.common.service;


public interface StringCacheService {

	public void setCache(String suffix, String type, String cacheStr);
	public void setCache(String suffix, String type, String cacheStr, Integer expireTime);
	public void clearCache(String suffix, String type);
	public String getCache(String suffix, String type);
	
}
