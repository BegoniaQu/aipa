package com.qy.data.common.lock.impl;

import redis.clients.jedis.Jedis;

import com.qy.data.common.exception.RedisRuntimeException;
import com.qy.data.common.lock.IDistributeLock;


public class RedisDistributeLock implements com.qy.data.common.lock.IDistributeLock{
	private RedisDistributeLockFactory lockFactory;
	private String key;
	private String item;
	private int timeoutMSecs;
	private int expireMSecs;
	private boolean locked;
	private long lockExpireTime;
	private boolean quitWhenLockExpireLaterThanTimeout = false;
	
	/**
	 * 
	 * @param lockFactory  should not and will not be null as the lock is instantiated by factory
	 * @param key  can not be null
	 * @param item  can be null, should not be empty string
	 * @param timeoutMSecs  the factory should give a default value which depends on the specific factory
	 * @param expireMSecs  the factory should give a default value which depends on the specific factory
	 */
	public RedisDistributeLock(RedisDistributeLockFactory lockFactory, String key, String item, int timeoutMSecs, int expireMSecs){
		this.lockFactory = lockFactory;
		this.key = key;
		this.item = item;
		this.timeoutMSecs = timeoutMSecs;
		this.expireMSecs = expireMSecs;
	}

	/**
	 * 问题： 没有设置expire time，导致老的不会被删除... 
	 */
	@Override
	public synchronized boolean acquire() throws InterruptedException{
		Jedis jedis = null;
		int timeout = timeoutMSecs;
		String curKey = getKey();
		try{
			boolean result = false;
			jedis = getJedis();
			while (timeout >= 0) {
				long expires = System.currentTimeMillis() + expireMSecs + 1;
				String expiresStr = String.valueOf(expires);
				if (jedis.setnx(curKey, expiresStr) == 1) {
					// lock acquired
					locked = true;
					lockExpireTime = expires;
					result = true;
					break;
				}

				String currentValueStr = jedis.get(curKey);
				
				boolean curExpired = false;
				// -1表示不能被抢占（一般 不应该使用该...） , 不能为 0
				if (currentValueStr != null){
					long currentValue = Long.parseLong(currentValueStr);
					if(currentValue >= 0){  // < 0 表示不会过期...
						if(currentValue < System.currentTimeMillis()){
							// lock is expired
							curExpired = true;
						}else if(quitWhenLockExpireLaterThanTimeout && (currentValue > System.currentTimeMillis() + timeout)){
							// 没希望
							break;
						} // 有希望，重试...
					} 
				} else {
					// 为 null的话应该允许抢占...
					curExpired = true;
				}
				
				if(curExpired){
					String oldValueStr = jedis.getSet(curKey, expiresStr);
					if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
						// lock acquired
						locked = true;
						lockExpireTime = expires;
						result = true;
						break;
					}
				}

				timeout -= 100;
				Thread.sleep(100);
			}
			if(result && expireMSecs > 0){
				jedis.expire(curKey, 10 + expireMSecs / 1000);
			}
			return result;
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			throw new RedisRuntimeException("fail to acquire lock for key " + getKey(), e);
		} finally{
			closeJedis(jedis);
		}
	}

	@Override
	public synchronized void release() throws InterruptedException{
		if (locked) {
			if(lockExpireTime > System.currentTimeMillis()){
				Jedis jedis = null;
				try{
					jedis = getJedis();
					jedis.del(getKey());  // 如果耗时操作执行时间超过了expireMSecs导致另一个...获得了锁的话，release时不应该删除...
				} catch (RuntimeException e) { 
					closeBrokenJedis(jedis);
					jedis = null;
					throw new RedisRuntimeException("fail to release lock for key " + getKey(), e);
				} finally{
					closeJedis(jedis);
				}
				
			}
			locked = false;
		}
	}
	
	private Jedis getJedis(){
		return lockFactory.getJedis();
	}
	
	private void closeJedis(Jedis jedis){
        lockFactory.closeJedis(jedis);
	}
	
	private void closeBrokenJedis(Jedis jedis){
        lockFactory.closeBrokenJedis(jedis);
	}
	
	public String getKey(){
		if(item == null) return key;
		else return key + ":{" + item + "}";
	}

	@Override
	public boolean isLocked() {
		return locked;
	}

	@Override
	public Long getExpireTime() {
		return lockExpireTime;
	}

	public boolean isQuitWhenLockExpireLaterThanTimeout() {
		return quitWhenLockExpireLaterThanTimeout;
	}

	public void setQuitWhenLockExpireLaterThanTimeout(
			boolean quitWhenLockExpireLaterThanTimeout) {
		this.quitWhenLockExpireLaterThanTimeout = quitWhenLockExpireLaterThanTimeout;
	}

}
