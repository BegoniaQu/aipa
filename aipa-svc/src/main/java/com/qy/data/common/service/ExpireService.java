package com.qy.data.common.service;

/**
 * @author qy
 *
 */
public interface ExpireService {
	/**
	 * @param key
	 * @param expire
	 */
	public Boolean add(String stringKey, int expireTime);
	
	/**
	 * @param key
	 */
	//public boolean exist(String stringKey);
}
