package com.qy.data.common.lock;


public interface IDistributeLock {
	public boolean acquire() throws InterruptedException;
	public void release() throws InterruptedException;
	public boolean isLocked();
	public Long getExpireTime();  // in MSecs, null means not implemented or will not expire
}
