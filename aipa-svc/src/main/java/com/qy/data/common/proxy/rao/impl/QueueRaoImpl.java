package com.qy.data.common.proxy.rao.impl;

import java.util.List;

import com.qy.data.common.exception.RedisRuntimeException;
import com.qy.data.common.proxy.rao.QueueRao;
import redis.clients.jedis.Jedis;

public class QueueRaoImpl extends RedisBaseRaoImpl implements
		QueueRao {

	@Override
	public void set(Object hashKey, String type, String... values) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			
			jedis.del(key);
			
			jedis.lpush(key, values);
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to push, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	@Override
	public void push(Object hashKey, String type, String... values) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			
			jedis.lpush(key, values);
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to push, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public String pop(Object hashKey, String type) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			
			return jedis.rpop(key);
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to pop, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public String bpop(Object hashKey, String type, int second) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			
			List<String> value =  jedis.brpop(second, new String[]{key});
			if( (value != null) && (!value.isEmpty())){
				return value.get(1);
			}
			return null;
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to pop, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}

}
