package com.qy.data.common.proxy.rao.impl;

import java.util.Set;

import redis.clients.jedis.Jedis;

import com.qy.data.common.constant.DataCommonConstant;
import com.qy.data.common.exception.RedisRuntimeException;
import com.qy.data.common.proxy.rao.SetCacheRao;

/**
 * 做过期时，如果set本身为空，会一直返回null，去查db，需要有个空标示。比如自动填0，标示empty。
 * 做不过期，不做这个处理
 * @author qy
 *
 */
public class SetCacheRaoImpl extends RedisBaseRaoImpl implements SetCacheRao {
	
	@Override
	public void sset(Object hashKey, String type, Integer seconds, String... values) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			jedis.del(key);
			if(null != seconds){// 缓存处理, 自动填充默认值
				jedis.expire(key, seconds);
			}
			
			if(null != values && values.length != 0){
				String[] toAdd = new String[values.length+1];
				toAdd[0] = DataCommonConstant.RAO_DEFAULT_FIELD_KEY;
				System.arraycopy(values, 0, toAdd, 1, values.length);
				jedis.sadd(key, toAdd);
			} else {
				jedis.sadd(key, DataCommonConstant.RAO_DEFAULT_FIELD_KEY);
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to sset, key=" + key + ", value=" + values, e);
		} finally {
			closeJedis(jedis);
		}				
	}
	
	@Override
	public Long sadd(Object hashKey, String type, Integer seconds, String... values) {
		if(null == values || values.length == 0){
			return 0L;
		}
		
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			
			if(seconds != null){// 缓存过期处理
				jedis.expire(key, seconds);
				
				if(jedis.exists(key)){
					//这里不存在并发问题，前面有expire，不会立即就过期
					return jedis.sadd(key, values);
				} else {
					return null;
				}
			} else {//持久化处理和不过期处理
				return jedis.sadd(key, values);
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to sadd, key=" + key + ", value=" + values, e);
		} finally {
			closeJedis(jedis);
		}				
	}
	@Override
	public Long saddwithkey(Object hashKey, String type, Integer seconds, String... values) {
		if(null == values || values.length == 0){
			return 0L;
		}
		
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);			
			jedis = getJedis();
			jedis.expire(key, seconds);
			
			jedis.sadd(key, DataCommonConstant.RAO_DEFAULT_FIELD_KEY);
			return jedis.sadd(key, values);
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to sadd, key=" + key + ", value=" + values, e);
		} finally {
			closeJedis(jedis);
		}				
	}
	@Override
	public Long srem(Object hashKey, String type, final Integer seconds, String... values) {
		if(null == values || values.length == 0){
			return 0L;
		}
		
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();

			if(seconds != null){// 缓存过期处理
				jedis.expire(key, seconds);
				
				if(jedis.exists(key)){
					//这里不存在并发问题，前面有expire，不会立即就过期
					return jedis.srem(key, values);
				} else {
					return null;
				}
			} else {//持久化处理和不过期处理
				return jedis.srem(key, values);
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to srem, key=" + key + ", value=" + values, e);
		} finally {
			closeJedis(jedis);
		}				
	}

	@Override
	public Set<String> smembers(Object hashKey, String type, Integer seconds) {
		
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			if(seconds != null){// 缓存过期处理
				jedis.expire(key, seconds);
			}
			
			Set<String> set = jedis.smembers(key);
			if(set.isEmpty()){
				return null;
			} else {
				set.remove(DataCommonConstant.RAO_DEFAULT_FIELD_KEY);
				return set;
			}
			//XXX:下面一种方式好像不是很好
//			if(seconds != null){// 缓存过期处理
//				jedis.expire(key, seconds);
//				
//				if(jedis.exists(key)){
//					//这里不存在并发问题，前面有expire，不会立即就过期
//					Set<String> set = jedis.smembers(key);
//					if(set.size() < 1 ){
//						throw new CloudPlatformRuntimeException("may be you use redis set error");
//					}
//					
//					set.remove(DEFAULT_VALUE);
//					return set;
//				} else {
//					return null;
//				}
//			} else {//持久化处理和不过期处理
//				return jedis.smembers(key);
//			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to smembers, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}	
	}
	
	@Override
	public Long scard(Object hashKey, String type) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();
			Long count = jedis.scard(key);
			if(count == 0){
				return null;
			} else {
				return count-1;
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to scard, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	@Override
	public Boolean sismember(Object hashKey, String type, String value) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(hashKey, type);
			
			jedis = getJedis();

			if(jedis.sismember(key, value)){
				return true;
			}
			
			if(jedis.exists(key)){
				return false;
			} else {
				return null;
			}
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to sismember, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
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
			throw new RedisRuntimeException("fail to sdel, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}		
	}
}
