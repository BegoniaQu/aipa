package com.qy.data.common.lock.impl;

import com.qy.data.common.lock.RedisLock;
import com.qy.data.common.proxy.rao.impl.RedisBaseRaoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;


public class RedisLockImpl extends RedisBaseRaoImpl implements RedisLock {
	Logger logger = LoggerFactory.getLogger(RedisLockImpl.class);
	
	@Override
	public Boolean getKey(String key,Integer expiretime,Boolean defaultOnRedisError){
		Jedis jedis = null;
		try {
			//logger.debug("redis begin:"+new Date());
			jedis = getJedis();
			if (jedis.exists(key)){
				return false;
			}
			if (jedis.setnx(key, "locked")==1){
				jedis.expire(key, expiretime);
				return true;
			}else {
				return false;
			}
		}catch (Exception e){
			logger.warn("error when try to get lock",e);
			return defaultOnRedisError;
		}finally {
			closeJedis(jedis);
		}
		
	}

}
