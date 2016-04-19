package com.qy.data.common.redis;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

public class ShardRedisBaseDao {
	
	private List<JedisPool> jedisPools;
	
	private Integer retryTimes = 3;
	
	public List<JedisPool> getJedisPools() {
		return jedisPools;
	}

	public void setJedisPools(List<JedisPool> jedisPools) {
		this.jedisPools = jedisPools;
	}
	
	public Jedis getJedis(String key){
		return jedisPools.get(getIndex(key)).getResource();
	}
	
	public Jedis getJedis(long key){
		return jedisPools.get(getIndex(key)).getResource();
	}
	
	public int getJedisSize(){
		return jedisPools.size();
	}
	
	public Transaction getTransaction (Jedis jedis){
		return jedis.multi();
	}

	public void closeJedis(Jedis jedis, String key){
		jedisPools.get(getIndex(key)).returnResource(jedis);
	}
	
	public void closeJedis(Jedis jedis, long key){
		jedisPools.get(getIndex(key)).returnResource(jedis);
	}
	
	public void closeBrokenJedis(Jedis jedis, String key){
		if( jedis != null ) {
			jedisPools.get(getIndex(key)).returnBrokenResource(jedis);
		}
	}
	
	public void closeBrokenJedis(Jedis jedis, long key){
		if( jedis != null ) {
			jedisPools.get(getIndex(key)).returnBrokenResource(jedis);
		}
	}
	
	public void unwatch(Jedis jedis){
		if( jedis != null ) {
			jedis.unwatch();
		}
	}
	
	public boolean retryLimit(int currentTimes){
		return currentTimes > retryTimes;
	}

	public Integer getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
	}
	
	public int getIndex(String key){
		return Math.abs(key.hashCode()) % getPoolSize();
	}
	
	public int getIndex(long key){
		long index = key % getPoolSize();
		return (int)index;
	}
	
	public int getPoolSize(){
		return jedisPools.size();
	}
}
