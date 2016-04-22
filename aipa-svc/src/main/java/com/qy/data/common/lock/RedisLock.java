package com.qy.data.common.lock;


public interface RedisLock {
	public Boolean getKey(String key, Integer expiretime,Boolean defaultOnRedisError);

}
