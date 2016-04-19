package com.qy.data.common.rao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qy.data.common.domain.GenericIndex;
import com.qy.data.common.domain.redis.TupleObjectDouble;
import com.qy.data.common.proxy.rao.IndexCacheRao;
import com.qy.data.common.rao.GenericIndexRao;

/**
 * @author tianhui
 *
 */
public abstract class AbstractGenericIndexRaoImpl<T extends GenericIndex, TOB extends TupleObjectDouble> implements GenericIndexRao<T> {
	private IndexCacheRao<TOB> indexCacheRao;
	private Integer seconds;
	private String keyPrefix;
	private Integer limit;
	
	@Override
	public Long zadd(T t) {
		return indexCacheRao.zadd(t.getHashKey(), getType(t.getKeyType()), t.getScore(), getValue(t), seconds);
	}

	@Override
	public void zaddall(Object hashKey, Object keyType, List<T> ts) {
		Map<String, Double> scoreMembers = new HashMap<String, Double>(ts.size());
		for(T t : ts){
			scoreMembers.put(String.valueOf(getValue(t)), (double) t.getScore());//每个index的score计算方式不一样。不能放在这里做计算
		}
		
		indexCacheRao.zaddall(hashKey, getType(keyType), scoreMembers, seconds);
	}

	@Override
	public Long zrem(T t) {
		return indexCacheRao.zrem(t.getHashKey(), getType(t.getKeyType()), seconds, getValue(t));
	}

	@Override
	public Long zremrangebyrank(T t, long start, long end) {
		return indexCacheRao.zremrangebyrank(t.getHashKey(), getType(t.getKeyType()), start, end, seconds);
	}

	@Override
	public Boolean exist(T t) {
		Long score = zscore(t);
		if(score == null){
			return null;
		} else if(score.longValue() < 0L) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<T> findNext(Object hashKey, Object keyType, Long nt, int count,
			int asc) {
		Double nextToken = null;
		if(null != nt){
			nextToken = nt.doubleValue();
		}

		List<TOB> tuples = null;
		if(asc == 0){//降序
			tuples = indexCacheRao.zlessthandesc(hashKey, getType(keyType), nextToken, count, seconds);
		} else {//升序
			tuples = indexCacheRao.zmorethanasc(hashKey, nextToken, count, getType(keyType), seconds);
		}
		
		return convert(hashKey, keyType, tuples);
	}

	@Override
	public List<T> findPrevious(Object hashKey, Object keyType, Long pt,
			int count, int asc) {
		Double previousToken = null;
		if(null != pt){
			previousToken = pt.doubleValue();
		}
		
		List<TOB> tuples = null;
		if(asc == 0){//降序
			tuples = indexCacheRao.zmorethandesc(hashKey, previousToken, count, getType(keyType), seconds);
		} else {//升序
			tuples = indexCacheRao.zlessthanasc(hashKey, getType(keyType), previousToken, count, seconds);
		}
		
		return convert(hashKey, keyType, tuples);
	}

	@Override
	public List<T> findByPage(Object hashKey, Object keyType, int pageno,
			int pagesize, int asc) {
		List<TOB> tuples = null;
		if(asc == 1){
			tuples = indexCacheRao.zrangeasc(hashKey, getType(keyType), pageno*pagesize, (pageno+1)*pagesize-1, seconds);
		} else {
			tuples = indexCacheRao.zrangedesc(hashKey, getType(keyType), pageno*pagesize, (pageno+1)*pagesize-1, seconds);
		}
		
		return convert(hashKey, keyType, tuples);
	}

	@Override
	public void delKey(Object hashKey, Object keyType) {
		indexCacheRao.delKey(hashKey, getType(keyType));
	}

	@Override
	public Long zscore(T t) {
		Double score = indexCacheRao.zscore(t.getHashKey(), getType(t.getKeyType()), getValue(t));
		if (score == null) {
			return null;
		} else {
			return score.longValue();
		}
	}

	@Override
	public Long zrank(T t) {
		Object member = getValue(t);
		return indexCacheRao.zrank(t.getHashKey(), getType(t.getKeyType()), member == null ? null : member.toString());
		
	}

	@Override
	public Long zcard(Object hashKey, Object keyType) {
		return indexCacheRao.zcard(hashKey, getType(keyType));
	}

	@Override
	public Long zcardBetween(Object hashKey, Object keyType, Double scoreStart,
			Double scoreEnd) {
		return indexCacheRao.zcount(hashKey, getType(keyType), scoreStart, scoreEnd);
	}

	protected String getType(Object keyType) {
		if(null == keyType){
			return keyPrefix;
		} else {
			return keyPrefix + ":" + keyType;
		}
		
	}
	
	private List<T> convert(Object hashKey, Object keyType, List<TOB> tuples) {
		if(null == tuples){
			return null;
		}
		
		if(tuples.isEmpty()){
			return  new ArrayList<T>();
		}
		
		List<T> ts = new ArrayList<T>();
		for (TOB tuple : tuples) {
			T t = convert(hashKey, keyType, tuple);
			ts.add(t);
		}

		return ts;
	}

	public IndexCacheRao<TOB> getIndexCacheRao() {
		return indexCacheRao;
	}

	public void setIndexCacheRao(IndexCacheRao<TOB> indexCacheRao) {
		this.indexCacheRao = indexCacheRao;
	}

	public Integer getSeconds() {
		return seconds;
	}

	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}	

	@Override
	public Integer getLimit() {
		return limit;
	}
	
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	@Override
	public boolean isLimited() {
		return limit != null;
	}
	
	protected abstract Object getValue(T t);
	
	protected abstract T convert(Object hashKey, Object keyType, TOB tuple);

	@Override
	public void delKey(T t) {
		indexCacheRao.delKey(t.getHashKey(), getType(t.getKeyType()));
	}
}
