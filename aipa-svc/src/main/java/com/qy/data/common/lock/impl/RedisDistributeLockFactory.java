package com.qy.data.common.lock.impl;

import com.qy.data.common.lock.IDistributeLock;
import com.qy.data.common.proxy.rao.impl.RedisBaseRaoImpl;
import com.qy.data.common.util.StringUtil;
import com.qy.data.common.lock.IDistributeLockFactory;

/**
 * keyPrefix 不需要 : 
 * @author qy
 *
 */
public class RedisDistributeLockFactory extends RedisBaseRaoImpl implements IDistributeLockFactory{
	private String keyPrefix;
	private Integer defaultTimeoutMSecs;
	private Integer defaultExpireMSecs;

	@Override
	public RedisDistributeLock getLock(String key, String item, int timeoutMSecs, int expireMSecs) {
		if(timeoutMSecs <= 0) timeoutMSecs = defaultTimeoutMSecs;
		if(expireMSecs == 0) expireMSecs = defaultExpireMSecs;
		return new RedisDistributeLock(this, getKey(key), item, timeoutMSecs, expireMSecs);
	}
	
	@Override
	public IDistributeLock getLock(String key, int timeoutMSecs, int expireMSecs) {
		return getLock(key, null, timeoutMSecs, expireMSecs);
	} 

	@Override
	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}
	
	private String getKey(String key){
		if(StringUtil.isEmpty(getKeyPrefix())) return "lock:" + key;
		else return "lock:" + keyPrefix + ":" + key;
	}

	public Integer getDefaultTimeoutMSecs() {
		return defaultTimeoutMSecs;
	}

	public void setDefaultTimeoutMSecs(Integer defaultTimeoutMSecs) {
		this.defaultTimeoutMSecs = defaultTimeoutMSecs;
	}

	public Integer getDefaultExpireMSecs() {
		return defaultExpireMSecs;
	}

	public void setDefaultExpireMSecs(Integer defaultExpireMSecs) {
		this.defaultExpireMSecs = defaultExpireMSecs;
	}
}
