package com.qy.data.common.proxy.rao.impl;

import redis.clients.jedis.Jedis;

import com.qy.data.common.exception.RedisRuntimeException;
import com.qy.data.common.proxy.rao.StringCacheRao;

public class StringCacheRaoImpl extends RedisBaseRaoImpl implements StringCacheRao {

	@Override
	public void set(Object stringKey, String type, String value) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(stringKey, type);
			jedis = getJedis();
			jedis.set(key, value);
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to set, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public void set(Object stringKey, String type, String value, int seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(stringKey, type);
			jedis = getJedis();
			jedis.set(key, value);
			jedis.expire(key, seconds);
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to set, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public String get(Object stringKey, String type) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(stringKey, type);
			jedis = getJedis();
			return jedis.get(key);
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to get, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
		
	}
	
	@Override
	public void setbit(Object stringKey, String type, long index,
			boolean flag, Integer seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(stringKey, type);
			jedis = getJedis();
			jedis.setbit(key, index, flag);
			
			if(null != seconds){
				jedis.expire(key, seconds);
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to set, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public boolean getbit(Object stringKey, String type, long index) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(stringKey, type);
			jedis = getJedis();
			return jedis.getbit(key, index);
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to set, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public void del(Object stringKey, String type) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(stringKey, type);
			jedis = getJedis();
			
			jedis.del(key);
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to del string, key=" + key;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	@Override
	public Long increx(Object stringKey, String type, long count, int seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(stringKey, type);
			jedis = getJedis();
			
			jedis.expire(key, seconds);
			
			if(jedis.exists(key)){
				return jedis.incrBy(key, count);
			} else {
				return null;
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to increx, key=" + key;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public Long decrex(Object stringKey, String type, long count, int seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(stringKey, type);
			jedis = getJedis();
			
			jedis.expire(key, seconds);
			
			if(jedis.exists(key)){
				return jedis.decrBy(key, count);
			} else {
				return null;
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to decrex, key=" + key;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public Long incre(Object stringKey, String type, long count, int seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(stringKey, type);
			jedis = getJedis();
			
			long newcount = jedis.incrBy(key, count);
			jedis.expire(key, seconds);
			
			return newcount;
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to incre, key=" + key;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	@Override
	public boolean exist(final Object stringKey, final String type){
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(stringKey, type);
			jedis = getJedis();
			
			return jedis.exists(key);
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to exist, key=" + key;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

    @Override
    public Long incre(Object stringKey, String type, long count)
    {
        Jedis jedis = null;
        String key = null;
        try {
            key = getKey(stringKey, type);
            jedis = getJedis();
            long newcount = jedis.incrBy(key, count);
            return newcount;
        } catch (RuntimeException e) { 
            closeBrokenJedis(jedis);
            jedis = null;
            
            String msg = "fail to incre, key=" + key;
            throw new RedisRuntimeException(msg, e);
        } finally {
            closeJedis(jedis);
        }
    }
}
