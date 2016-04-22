package com.qy.data.common.proxy.rao;

/**
 * 重启后queue丢失，需要依赖于恢复机制
 * @author qy
 * @date 2016
 */
public interface QueueRao {
	public void set(final Object hashKey, final String type, final String... values);
	
	public void push(final Object hashKey, final String type, final String... values);
	
	public String pop(final Object hashKey, final String type);
	
	public String bpop(Object hashKey, String type, final int second);
}
