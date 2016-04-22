package com.qy.data.common.service.impl;

import com.qy.data.common.domain.GenericIndex;
import com.qy.data.common.rao.GenericIndexRao;
import com.qy.data.common.service.GenericIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 只支持redis存储。redis需要做好持久化操作。
 * @author qy
 *
 * @param <T>
 */
public abstract class AbstractGenericIndexServiceOnlyRedisImpl<T extends GenericIndex> implements GenericIndexService<T> {
	protected static final Logger logger = LoggerFactory.getLogger("com.anlaiye.data.common.service.impl.index.onlyredis");
	
	protected abstract GenericIndexRao<T> getRao();
	protected abstract void fillScore(T t);
	
	@Override
	public void add(T t) {
		if(t == null){
			logger.warn("add index error");
		}
		
		fillScore(t);

		if(getRao().zadd(t) == null){
			List<T> add = new ArrayList<T>();
			add.add(t);
			getRao().zaddall(t.getHashKey(), t.getKeyType(), add);//由于没有dao层，恢复操作是插入空的map到redis中。
		}
	}
	
	@Override
	public void add(T t, boolean updateOnExist) {
		if(t == null){
			logger.warn("add index error");
		}
		
		//存在，且存在不更新，才不操作redis，其它都需要add.也就是满足1 存在必须更新 2 不存在（为null或false） 必须更新
		Boolean exist = null;
		if( updateOnExist || (exist = getRao().exist(t)) == null || !exist){//判断顺序不能变，依赖exist()做恢复操作。
			add(t);
		}
	}

	@Override
	public void del(T t) {
		getRao().zrem(t);
	}

	@Override
	public boolean exist(T t) {
		Boolean exist = getRao().exist(t);
		if(exist != null){
			return exist;
		} else {
			return false;
		}
	}


	@Override
	public Long getScore(T t) {
		return getRao().zscore(t);
	}
	
	@Override
	public Integer getRank(T t) {
		Long rank = getRao().zrank(t);
		return rank == null ? null : rank.intValue();
	}
	
	@Override
	public List<T> findByToken(T t, Long nt,
			Long pt, int pagesize, int asc) {
		if(null != nt || null == pt){	//下一页			
			return getRao().findNext(t.getHashKey(), t.getKeyType(), nt, pagesize, asc);
		} else {
			return getRao().findPrevious(t.getHashKey(), t.getKeyType(), pt, pagesize, asc);
		}
	}
	
	public List<T> findNext(T t, Long nt, int count, int asc) {		
		return getRao().findNext(t.getHashKey(), t.getKeyType(), nt, count, asc);
	}

	
	public List<T> findPrevious(T t, Long pt, int count, int asc) {
		return getRao().findPrevious(t.getHashKey(), t.getKeyType(), pt, count, asc);
	}

	@Override
	public List<T> findByPage(T t, int pageno,
			int pagesize, int asc) {
		return getRao().findByPage(t.getHashKey(), t.getKeyType(), pageno, pagesize, asc);
	}

	@Override
	public int count(T t) {
		Long count = getRao().zcard(t.getHashKey(), t.getKeyType());
		
		if(null != count){
			return count.intValue();
		} else {
			return 0;
		}
	}

	@Override
	public int countBetween(T t, Long scoreStart,
			Long scoreEnd) {
		Long count = getRao().zcardBetween(t.getHashKey(), t.getKeyType(), (Double)scoreStart.doubleValue(), (Double)scoreEnd.doubleValue());
		
		if(null != count){
			return count.intValue();
		}
		
		return 0;
	}
	
	@Override
	public void delKey(T t) {
		if(t == null) return;
		getRao().delKey(t);
	}
}
