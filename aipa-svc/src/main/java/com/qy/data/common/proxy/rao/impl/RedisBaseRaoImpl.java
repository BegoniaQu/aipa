package com.qy.data.common.proxy.rao.impl;

import com.qy.data.common.util.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 为了以后扩展，需要遵循twemproxy的使用方式，不出现批量命令，不出现事务等
 * @author qy
 * @date 2016
 */
public abstract class RedisBaseRaoImpl {
	
	private JedisPool jedisPool;

	private Integer retryTimes;
	
	public Jedis getJedis(){
		return jedisPool.getResource();
	}
	
	public void closeJedis(Jedis jedis){
		if(null != jedis){
			jedisPool.returnResource(jedis);
		}
	}
	
	public void closeBrokenJedis(Jedis jedis){
		if(jedis != null) {
			jedisPool.returnBrokenResource(jedis);
		}
	}
	
	public boolean retryLimit(int currentTimes){
		return currentTimes > retryTimes;
	}
	
	protected String getKey(final Object key, final String type){
		if (StringUtil.isEmpty(type)){
			return key.toString();
		}
		return type + ":{" + key.toString() + "}";
	}

	public Integer getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
}
