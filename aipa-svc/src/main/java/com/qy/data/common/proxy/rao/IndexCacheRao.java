package com.qy.data.common.proxy.rao;

import java.util.List;
import java.util.Map;

import com.qy.data.common.domain.redis.TupleObjectDouble;


/**
 * 支持各种通用的分页索引
 * @author qy
 * @date   2016
 * @param <T>
 */
public interface IndexCacheRao <T extends TupleObjectDouble> {
	
	/**
	 * 加入一个field-value
	 * 当key不存在时，创建key。
	 * 成功返回新加入的元素数量，不包括已经存在的field，即使做了update
	 */
	
	public Long zaddwithkey(final Object indexId, final String type, final double score, final Object member, final Integer seconds);

	/**
	 * 加入一个field-value
	 * 当key不存在时，不做add操作，返回null。
	 * 成功返回新加入的元素数量，不包括已经存在的field，即使做了update
	 */
	public Long zadd(final Object indexId, final String type, final double score, final Object member, final Integer seconds);
	
	public Long zrank(final Object indexId, final String type,final String member);
	/**
	 * 全量加索引，会填入一个field和分数都为0的索引
	 */
	public Long zaddall(final Object indexId, final String type, final Map<String, Double> scoreMembers, final Integer seconds);
	
	/**
	 * 返回小于指定分值的元素,元素大小为count。second不传，不做expire.元素从大到小排列
	 * 如果key不存在，返回null
	 * 可用于计算下一页
	 */
	public List<T> zlessthandesc(final Object indexId, final String type, final Double score, final int count, final Integer seconds);
	
	/**
	 * 返回大于指定分值的元素,元素大小为count。second不传，不做expire， 元素从大到小排列
	 * 如果key不存在，返回null
	 * 可用于计算上一页
	 */
	public List<T> zmorethanasc(final Object indexId, final Double score, final int count, final String type, final Integer seconds);
	
	
	/**
	 * 返回小于指定分值的元素,元素大小为count。second不传，不做expire.元素从大到小排列
	 * 如果key不存在，返回null
	 * 可用于计算下一页
	 */
	public List<T> zlessthanasc(final Object indexId, final String type, final Double score, final int count, final Integer seconds);
	/**
	 * 根据位置查询
	 * 如果key不存在，返回null
	 * 可用于计算上一页
	 */
	public List<T> zrangeasc(final Object indexId, final String type, final int start, final int end, final Integer seconds);
	
	/**
	 * 返回大于指定分值的元素,元素大小为count。second不传，不做expire， 元素从大到小排列
	 * 如果key不存在，返回null
	 * 可用于计算上一页
	 */
	public List<T> zmorethandesc(final Object indexId, final Double score, final int count, final String type, final Integer seconds);
	
	/**
	 * 根据位置查询
	 * 如果key不存在，返回null
	 * 可用于计算上一页
	 */
	public List<T> zrangedesc(final Object indexId, final String type, final int start, final int end, final Integer seconds);
	
	/**
	 * 返回分值区间的元素。前开后闭。second不传，不做expire
	 * 如果key不存在，返回null
	 * 可用于计算下一页
	 */
	public List<T> zbetweenasc(final Object indexId, final String type, final Double start, final Double end, final Integer seconds);
	
	/**
	 * return
	 * number of elements(include 0) if key exist, 
	 * null if key does not exist.
	 */
	public Long zcard(final Object indexId, final String type);
	
	public Long zcount(final Object indexId, final String type, final Double start, final Double end);
	
	/**
	 * return 
	 * the score of member, If member exists in the sorted set;
	 * -1, If member does not exist in the sorted set;
	 * null, If key does not exist
	 * 可用于判断exist
	 */
	public Double zscore(final Object indexId, final String type, final Object member);

	/**
	 * 移除某几个元素
	 * 如果key不存在，返回null
	 */
	public Long zrem(final Object indexId, final String type, final Integer seconds, final Object... members);
	
	/**
	 * 移除有序集 key 中，指定排名(rank)区间内的所有成员。
	 * 区间分别以下标参数 start 和 stop 指出，包含 start 和 stop 在内。
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 * 时间复杂度: O(log(N)+M)， N 为有序集的基数，而 M 为被移除成员的数量
	 * @param indexId
	 * @param type
	 * @param start 开始index，含，允许为负数
	 * @param end 结尾index，含，允许为负数
	 * @param seconds
	 * @return 被移除成员的数量。
	 */	
	public Long zremrangebyrank(Object indexId, String type, long start, long end, Integer seconds);
	
	/**
	 * 如果key不存在，返回null
	 */
	public void delKey(final Object indexId, final String type);
	
	/**
	 * 如果key不存在，返回null
	 */
	public List<T> zscan(final Object indexId, final String type, String match, int count);
	
	/**
	 * 修改expire时间，可能被其他操作覆盖
	 */
	public Long zexpire(Object indexId, String type, Integer seconds);

	/**
	 * 修改expireAt时间，可能被其他操作覆盖
	 */
	public Long zexpireat(Object indexId, String type, Long expireAt);
}
