package com.qy.data.common.proxy.rao;



public interface BinaryStringCacheRao {
	
	/**
	 * 不管是否存在，都set, 不设过期时间
	 * @param binKey
	 * @param value
	 */
	public void set(final byte[] binKey, final byte[] binValue);
	
	/**
	 * 不管key是否存在，都set
	 * @param binKey
	 * @param binValue
	 * @param seconds
	 */
	public void set(final byte[] binKey, final byte[] binValue, final int seconds);

	/**
	 * 当key不存在，返回null
	 * @param binKey
	 * @return
	 */
	public byte[] get(final byte[] binKey);
	
	/**
	 * 不敢key是否存在，都删除
	 * @param stringKey
	 * @param type
	 */
	public void del(final byte[] binKey);
}
