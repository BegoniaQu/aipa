package com.qy.data.common.service;

import java.util.List;

import com.qy.data.common.domain.GenericIndex;

/**
 * index的service都实现这个接口
 * @author qy
 *
 * @param <T>
 */
public interface GenericIndexService <T extends GenericIndex>{
	// 增加索引，已存在时报错
	public void add (T t);
	// 增加索引，已存在时覆盖或忽略
	public void add(T t, boolean updateOnExist);
	
	public void del(T t);

	public void resyncRedis(T t);
	
	public void delKey(T t);

	public void resyncRedisKey(T t);
	
	/**
	 * 判断t是否存在，如果key不存在，返回null
	 * @param t
	 * @return
	 */
	public boolean exist(T t);
	
	public Long getScore(T t);
	
	/**
	 * null没找得到rank，－1明确表示不在排名内
	 * @param t
	 * @return
	 */
	public Integer getRank(T t);
	
	public List<T> findByToken(T t, Long nt, Long pt, int pagesize, int asc);

	public List<T> findByPage(T t, int pageno, int pagesize, int asc);

	public int count(T t);
	
	public int countBetween(T t, Long scoreStart, Long scoreEnd);
}
