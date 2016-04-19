package com.qy.data.common.proxy.rao.impl;

import redis.clients.jedis.Jedis;

import com.qy.data.common.exception.RedisRuntimeException;
import com.qy.data.common.proxy.rao.BinaryStringCacheRao;

public class BinaryStringCacheRaoImpl extends RedisBaseRaoImpl implements BinaryStringCacheRao {

	@Override
	public void set(byte[] binKey, byte[] binValue) {
		Jedis jedis = null;
		String key = null;
		try {
			jedis = getJedis();
			jedis.set(binKey, binValue);
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to set, key=" + key, e);
		} finally {
			closeJedis(jedis);
		}		
	}

	@Override
	public void set(byte[] binKey, byte[] binValue, int seconds) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(binKey, binValue);
			jedis.expire(binKey, seconds);
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to set, key=" + binKey, e);
		} finally {
			closeJedis(jedis);
		}		
	}

	@Override
	public byte[] get(byte[] binKey) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.get(binKey);
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to set, key=" + binKey, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public void del(byte[] binKey) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.del(binKey);
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to set, key=" + binKey, e);
		} finally {
			closeJedis(jedis);
		}
	}
}
