package com.qy.data.common.proxy.rao;

import java.util.Set;

/**
 * 支持过期和不过期.这两种方式的处理是完全不一样的。使用要保证一致性
 * 作为cache使用时，每个方法必须要有second。
 * @author qy
 *
 */
public interface SetCacheRao {
	
	/**
	 * seconds为null，表示不过期.
	 * 替换原有的集合
	 */
	void sset(final Object hashKey, final String type, final Integer seconds, final String... values);

	/**
	 * seconds为null，表示不过期.
	 * 如果key不存在，返回null
	 * @param hashKey
	 * @param type
	 * @param seconds
	 * @param values
	 */
	Long sadd(final Object hashKey, final String type, final Integer seconds, final String... values);
	/**
	 * seconds为null，表示不过期.
	 * 如果key不存在，返回null
	 * @param hashKey
	 * @param type
	 * @param seconds
	 * @param values
	 */
	Long saddwithkey(final Object hashKey, final String type, final Integer seconds, final String... values);
	
	/**
	 * seconds为null，表示不过期.
	 * 如果key不存在，返回null
	 */
	Long srem(final Object hashKey, final String type, final Integer seconds, final String... values);
	
	/**
	 * seconds为null，表示不过期.
	 * 如果key不存在，返回null
	 */
	Set<String> smembers(final Object hashKey, final String type, final Integer seconds);
	
	/**
	 * 如果key不存在，返回null
	 */
	Long scard(final Object hashKey, final String type);
	
	Boolean sismember(final Object hashKey, final String type, final String value);
	
	/**
	 * seconds为null，表示不过期.
	 * 如果key不存在，返回null
	 */
	void del(final Object hashKey, final String type);
	
}
