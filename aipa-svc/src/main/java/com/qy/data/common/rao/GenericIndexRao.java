package com.qy.data.common.rao;

import java.util.List;

import com.qy.data.common.domain.GenericIndex;

/**
 * 一个通用的对内容索引的Rao接口
 * @author qy
 *
 */
public interface GenericIndexRao<T extends GenericIndex> {
	
	/**
	 * 如果索引的Key不存在，返回null。否则返回1
	 * @param t 要加入的索引
	 * @return
	 */
	public Long zadd(T t);
	
	/**
	 * 用户recover key
	 * @param hashKey 用于redis集群分片的key，与keyType一起组成redis的key
	 * @param keyType
	 * @param ts
	 */
	public void zaddall(Object hashKey, Object keyType, List<T> ts);
	
	public Long zrem(T t);
	public Long zremrangebyrank(T t, long start, long end);
	
	public void delKey(T t);
	
	/**
	 * 判断t是否存在，如果key不存在，返回null
	 * @param t
	 * @return
	 */
	public Boolean exist(T t);
	
	public List<T> findNext(Object hashKey, Object keyType, Long nt, int count, int asc);
	
	public List<T> findPrevious(Object hashKey, Object keyType, Long pt, int count, int asc);

	public List<T> findByPage(Object hashKey, Object keyType, int pageno, int pagesize, int asc);
	
	public void delKey(Object hashKey, Object keyType);
	
	public Long zscore(T t);
	
	/**
	 * key不存在返回null,member不在列表返回-1
	 * @param t
	 * @return
	 */
	public Long zrank(T t);

	public Long zcard(Object hashKey, Object keyType);
	
	public Long zcardBetween(Object hashKey, Object keyType, Double scoreStart, Double scoreEnd);
	
	/**
	 * 获取索引熟练限制
	 * 
	 * 注意，此限制适用下述场景：
	 * 1. 仅有尾部（或接近尾部,score较大端）插入
	 * 2. limit较大（比如1000或更高）
	 * 3. 并不要求limit的绝对精确
	 * @return limit
	 */
	public Integer getLimit();
	
	public boolean isLimited();
}
