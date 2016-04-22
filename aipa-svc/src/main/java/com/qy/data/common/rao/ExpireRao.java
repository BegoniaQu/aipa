package com.qy.data.common.rao;

/**
 * 专门用于过期时间。比如控制用户添加的频率。
 * @author qy
 * @date   2014-10-22
 */
public interface ExpireRao {
	/**
	 * 如果key存在，往后延迟expireTime秒。如果key不存在
	 * @param key
	 * @param expire
	 */
	public Boolean add(String stringKey, int expireTime);
	
	/**
	 * @param key
	 */
	public boolean exist(String stringKey);
}
