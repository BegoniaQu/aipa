package com.qy.data.common.lock;


public interface IDistributeLockFactory {

	/**
	 * 
	 * @param key
	 * @param item
	 * @param timeoutMSecs  必须有，或者由factory提供默认值。 小于或者等于0表示使用factory提供的默认值
	 * @param expireMSecs 必须有，或者由factory提供默认值。 不能为0（0表示使用factory的默认值），允许小于0表示不能被抢占
	 * @return
	 */
	public IDistributeLock getLock(String key, String item, int timeoutMSecs,
			int expireMSecs);
	
	/**
	 * 
	 * @param key
	 * @param item
	 * @param timeoutMSecs
	 * @param expireMSecs 必须有，或者由factory提供默认值。 不能为0（0表示使用factory的默认值），允许小于0表示不能被抢占
	 * @return
	 */
	public IDistributeLock getLock(String key, int timeoutMSecs,
			int expireMSecs);	
	
	public String getKeyPrefix();
}
