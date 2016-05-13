package com.qy.data.common.service.impl;

import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.domain.GenericIndex;
import com.qy.data.common.rao.GenericIndexRao;
import com.qy.data.common.service.GenericIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

/**
 * 当redis key不存在时，从dao层恢复。
 * TODO:恢复导致的并发问题暂时不考虑。
 * 修改redis时，不catch redis异常。读操作时，忽略redis异常。
 * @author qy
 *
 * @param <T>
 */
public abstract class AbstractGenericIndexServiceImpl<T extends GenericIndex> 
	implements GenericIndexService<T> {
	
	protected static final Logger logger = LoggerFactory.getLogger("com.anlaiye.data.common.service.impl.index");
	
	protected abstract GenericDao<T, Object> getDao();
	protected abstract GenericIndexRao<T> getRao();
	protected abstract void fillScore(T t);
	
	private Random r = new Random();
	private static final int TRUNCATE_PERCENT = 10;
	
	@Override
	public void add(T t) {
		if(t == null){
			logger.warn("add index error");
			return;
		}
		fillScore(t);
		getDao().save(t);
		///////////////////////
		if(getRao() == null){
			logger.error("add: rao obj is null,please check");
			return;
		}
		///////////////////////
		if(getRao().zadd(t) == null){
			//recover(t);//dao先插入数据，recover会完成本次的rao的zadd。
		} else if (getRao().isLimited() && r.nextInt(getRao().getLimit()*TRUNCATE_PERCENT/100) < 2) { 
			// 在超出TRUNCATE数量的操作内期望发生两次truncate操作，例如limit1000，则在1000-1100中有2%几率会尝试truncate
			truncate(t);
		}
	}
	
	@Override
	public void add(T t, boolean updateOnExist) {
		if(t == null){
			logger.warn("add index error");
			return;
		}
		fillScore(t);
		if(getDao().save(t, updateOnExist) && getRao().zadd(t) == null){//dao和rao同时影响
			//recover(t);//dao先插入数据，recover会完成本次的rao的zadd。
		} else if (getRao().isLimited() && r.nextInt(getRao().getLimit()*TRUNCATE_PERCENT/100) < 2) { 
			// 在超出TRUNCATE数量的操作内期望发生两次truncate操作，例如limit1000，则在1000-1100中有2%几率会尝试truncate
			truncate(t);
		}
	}

	@Override
	public void resyncRedis(T t){
		if(t == null) return;
		getRao().zrem(t);
	}

	@Override
	public void del(T t) {
		if(t == null) return;
		getDao().delete(t, null, t.getConditionsOfQueryUniqueRecord());
		///////////////////////
		if(getRao() == null){
		logger.error("del: rao obj is null,please check");
		return;
		}
		///////////////////////
		getRao().zrem(t);
	}

	@Override
	public boolean exist(T t) {
		Boolean exist = null;
		try {
			exist = getRao().exist(t);
			if(exist != null){
				return exist;
			}
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}
		
		
		exist = false;
		List<T> ts = getDao().query(t, null, t.getConditionsOfQueryAll());
		if(ts != null && !ts.isEmpty()){
			for (T et : ts) {
				if(t.equalIndex(et)){
					exist = true;
				}
				fillScore(et);
			}
		}
		
		try {
			getRao().zaddall(t.getHashKey(), t.getKeyType(), ts);
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}	
		
		return exist;
	}


	@Override
	public Long getScore(T t) {
		try {
			Long score = getRao().zscore(t);
			if(score != null){
				return score;
			}
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}
		
		Long score = null;

		List<T> ts = getDao().query(t, null, t.getConditionsOfQueryAll());
		if(ts != null && !ts.isEmpty()){
			for (T et : ts) {
				fillScore(et);
				
				if(t.equalIndex(et)){
					score = et.getScore();
				}
			}
		}
		
		try {
			getRao().zaddall(t.getHashKey(), t.getKeyType(), ts);
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}	
		
		return score;
	}

	@Override
	public Integer getRank(T t) {
		try {
			Long rank = getRao().zrank(t);
			if (rank != null) {
				return rank.intValue();
			}
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}

		List<T> ts = getDao().query(t, null, t.getConditionsOfQueryAll());
		if(ts != null && !ts.isEmpty()){
			for (T et : ts) {
				fillScore(et);
			}
		}
		
		try {
			getRao().zaddall(t.getHashKey(), t.getKeyType(), ts);
			Long rank = getRao().zrank(t);
			return rank == null ? null : rank.intValue();
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}	
		
		return null;
	}
	
	@Override
	public List<T> findByToken(T t, Long nt,
			Long pt, int pagesize, int asc) {
		if(null != nt || null == pt){	//下一页			
			return findNext(t, nt, pagesize, asc);
		} else {
			return findPrevious(t, pt, pagesize, asc);
		}
	}
	
	public List<T> findNext(T t, Long nt, int count, int asc) {
		try {
			List<T> ts = getRao().findNext(t.getHashKey(), t.getKeyType(), nt, count, asc);
			
			if(null != ts){
				return ts;
			}
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}
		
		recover(t);
		
		return getRao().findNext(t.getHashKey(), t.getKeyType(), nt, count, asc);
	}


	protected int recover(T t) {//TODO:zk锁对象
		Long count = getRao().zcard(t.getHashKey(), t.getKeyType());
		if(count != null){
			return count.intValue();
		}
		
		List<T> ts = getDao().query(t, null, t.getConditionsOfQueryAll());
		
		int result = 0;
		if(ts != null && !ts.isEmpty()){
			if (getRao().isLimited() && ts.size() > getRao().getLimit()) {
				TreeMap<Long, T> map = new TreeMap<Long, T>();
				for (T t2 : ts) {
					fillScore(t2);
					if (t2.getScore() == null) continue;
					if (map.size() <= getRao().getLimit()) {
						map.put(t2.getScore(), t2);
					} else if (t2.getScore() > map.firstKey()) {
						map.remove(map.firstKey());
						map.put(t2.getScore(), t2);
					}
				}
				ts = new ArrayList<T>(map.values());
			} else for (T t2 : ts) {
				fillScore(t2);
			}
		}
		
		try {
			getRao().zaddall(t.getHashKey(), t.getKeyType(), ts);
			result = ts.size();
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}	
		
		return result;
	}
	
	private void truncate(T t) {
		Long count = getRao().zcard(t.getHashKey(), t.getKeyType());
		if (count != null && count > getRao().getLimit()) {
			logger.debug("index number {} exceeded limit {}, trying to truncate", count, getRao().getLimit());
			getRao().zremrangebyrank(t, 0-getRao().getLimit()*TRUNCATE_PERCENT/100, -1);
		}
	}
	
	public List<T> findPrevious(T t, Long pt, int count, int asc) {
		
		try {
			List<T> ts = getRao().findPrevious(t.getHashKey(), t.getKeyType(), pt, count, asc);
			
			if(null != ts){
				return ts;
			}
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}
		
		recover(t);
		
		return getRao().findPrevious(t.getHashKey(), t.getKeyType(), pt, count, asc);
	}

	@Override
	public List<T> findByPage(T t, int pageno,
			int pagesize, int asc) {
		try {
			List<T> ts = getRao().findByPage(t.getHashKey(), t.getKeyType(), pageno, pagesize, asc);
			if(null != ts){
				return ts;
			}
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}
		
		recover(t);
		
		return getRao().findByPage(t.getHashKey(), t.getKeyType(), pageno, pagesize, asc);
	}

	@Override
	public int count(T t) {
		try {
			Long count = getRao().zcard(t.getHashKey(), t.getKeyType());
			
			if(null != count){
				return count.intValue();
			}
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}
		
		return recover(t);
	}

	@Override
	public int countBetween(T t, Long scoreStart, Long scoreEnd) {
		try {
			Long count = getRao().zcardBetween(t.getHashKey(), t.getKeyType(), 
					(Double)scoreStart.doubleValue(), (Double)scoreEnd.doubleValue());
			
			if(null != count){
				return count.intValue();
			}
		} catch (Exception e) {
			logger.warn("index cache error", e);
		}
		
		recover(t);
		
		Long count = getRao().zcardBetween(t.getHashKey(), t.getKeyType(), 
				(Double)scoreStart.doubleValue(), (Double)scoreEnd.doubleValue());
		return count == null ? null : count.intValue();
	}
	@Override
	public void delKey(T t) {
		if(t == null) return;
		getDao().delete(t, null, t.getConditionsOfQueryAll());
		///////////////////////
		if(getRao() == null){
		logger.error("delKey: rao obj is null,please check");
		return;
		}
		///////////////////////
		getRao().delKey(t);
	}

	@Override
	public void resyncRedisKey(T t){
		if(t == null) return;
		getRao().delKey(t);
	}

}
