package com.qy.data.common.proxy.rao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.qy.data.common.exception.RedisRuntimeException;
import com.qy.data.common.proxy.rao.HashCacheRao;
import redis.clients.jedis.Jedis;


/**
 * 为了以后扩展，需要遵循twemproxy的使用方式，不出现批量命令，不出现事务等
 * @author qy
 * @date 2016
 */
public class HashCacheRaoImpl extends RedisBaseRaoImpl implements HashCacheRao {

	@Override
	public void hmset(Object hashKey, String type, Map<String, String> fieldValues) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();

			jedis.hmset(key, fieldValues);
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to hmset, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}		
	}
	
	@Override
	public void hmset(Object hashKey, String type,
			Map<String, String> fieldValues, int seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();

			jedis.hmset(key, fieldValues);
			
			jedis.expire(key, seconds);
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to hmset, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public boolean hmsetx(Object hashKey, String type,
			Map<String, String> fieldValues, int seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			
			jedis.expire(key, seconds);
			if(jedis.exists(key)){
				jedis.hmset(key, fieldValues);
			} else {
				return false;
			}

		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to hmsetx, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
		
		return true;
	}

	@Override
	public boolean hsetx(Object hashKey, String type, String field,
			String value, int seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			
			jedis.expire(key, seconds);
			
			if(jedis.exists(key)){
				jedis.hset(key, field, value);
			} else {
				return false;
			}

		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to hsetx, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
		
		return true;
	}

	@Override
	public List<String> hmget(Object hashKey, String type, List<String> fields) {
		Jedis jedis = null;
		String key = null;
		
		try {
			key = getKey(hashKey, type);
			jedis = getJedis();
			
			List<String> values = jedis.hmget(key, fields.toArray(new String[fields.size()]));
			if(values.isEmpty()){
				return null;
			}
			
			return values;
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;

			throw new RedisRuntimeException("fail to hmget, key=" + key + ", fields=" + fields, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public String hget(Object hashKey, String type, String field) {
		Jedis jedis = null;
		String key = null;
	
		try {
			key = getKey(hashKey, type);
			jedis = getJedis();
			
			String value = jedis.hget(key, field);
			if(null != value){
				return value;
			}

			if(jedis.exists(key)){
				return jedis.hget(key, field);
				
//				value = jedis.hget(key, field);
//				if(null != value){
//					return value;
//				}
//				
//				if(jedis.exists(key)){
//					return "";
//				} else {
//					return null;
//				}
			} else {
				return null;
			}
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;

			throw new RedisRuntimeException("fail to hget, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public Map<String, String> hgetall(Object hashKey, String type) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			jedis = getJedis();
			
			Map<String, String> mapFeed = jedis.hgetAll(key);
			if(mapFeed.isEmpty()){
				return null;
			}
			
			return mapFeed;
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;

			throw new RedisRuntimeException("fail to hgetall, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public Long hincrbyx(Object hashKey, String type,
			String field, Long value){
		Jedis jedis = null;
		String key = null;
		Long newValue = null;
		try {
			key = getKey(hashKey, type);
			jedis = getJedis();
			
			if(jedis.hget(key, field) != null){
				newValue = jedis.hincrBy(key, field, value);
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;

			throw new RedisRuntimeException("fail to hincreby, key=" + key + ", fv=" + value, e);
		} finally {
			closeJedis(jedis);
		}
		
		return newValue;
	}
	
	@Override
	public Map<String, Long> hincreby(Object hashKey, String type,
			Map<String, Long> fieldValues, int seconds) {
		Jedis jedis = null;
		String key = null;
		Map<String, Long> newValues = null;
		try {
			key = getKey(hashKey, type);
			jedis = getJedis();
			
			//过期，再判断存在，是没有并发问题的。
			//但如果过期，再判断不存在，就有事务问题
			jedis.expire(key, seconds);
			
			if(jedis.exists(key)){
				newValues = new HashMap<String, Long>(fieldValues.size());
				for (Entry<String, Long> entry : fieldValues.entrySet()) {
					Long value = jedis.hincrBy(key, entry.getKey(), entry.getValue());
					newValues.put(entry.getKey(), value);
				}
			} 
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;

			throw new RedisRuntimeException("fail to hincreby, key=" + key + ", fv=" + fieldValues, e);
		} finally {
			closeJedis(jedis);
		}
		
		return newValues;
	}
	
	@Override
	public void del(Object hashKey, String type) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			jedis = getJedis();
			
			jedis.del(key);
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to del hash, key=" + key;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public boolean hmsetnx(Object hashKey, String type, Map<String, String> fieldValues, int seconds) {
		//XXX: 由于使用了代理，不能使用transaction，会有并发问题
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			
			if(!jedis.exists(key)){
				jedis.hmset(key, fieldValues);
				jedis.expire(key, seconds);
			}else{
				return false;
			}

		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to hsetnx, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
		return true;	
	}
	
	@Override
	public Long hset(Object hashKey, String type, String field,
			String value, Integer seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			Long res = jedis.hset(key, field, value);
			
			if(seconds != null){
				jedis.expire(key, seconds);
			}

			return res;
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to hsetx, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	@Override
	public boolean hsetnx(Object hashKey, String type, String field, String value, Integer seconds){
		Long resValue = null;
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			resValue = jedis.hsetnx(key, field, value);
			if(seconds != null) jedis.expire(key, seconds);
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to hsetnx, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
		return resValue == 0?false:true;
	}

	@Override
	public Long hlen(Object hashKey, String type) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();

			if(!jedis.exists(key)){
				return 0L;
			}else{
				return jedis.hlen(key);
			}
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to hmset, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}	
	}

	@Override
	public Boolean hdel(Object hashKey, String type, String... members) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);

			jedis = getJedis();

			if (jedis.exists(key)) {
				jedis.hdel(key, members);
				return true;
			} else {
				return false;
			}

		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to del members, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	@Override
	public Boolean existKey(final Object hashKey, final String type) {

		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			jedis = getJedis();
			return jedis.exists(key);
		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to check exist key, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}


}
