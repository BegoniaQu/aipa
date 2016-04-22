package com.qy.data.common.service.impl;

import com.qy.data.common.service.GenericInfoService;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.dao.impl.AbstractGenericDaoImpl.Expression;
import com.qy.data.common.rao.GenericInfoRao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * @author qy
 *
 * @param <T>
 * @param <PK>
 */
public abstract class AbstractGenericInfoServiceImpl<T extends Serializable, PK> implements GenericInfoService<T, PK> {
	protected static final Logger logger = LoggerFactory.getLogger("com.anlaiye.data.common.service.impl.index");
	
	protected abstract GenericDao<T, PK> getDao();
	protected abstract GenericInfoRao<T, PK> getRao();

	@Override
	public void add(T t) {
		if(t == null) return;
		getDao().save(t);
		try {
			GenericInfoRao<T, PK> rao = getRao();
			if(rao != null){
				rao.add(t);
			}
		} catch (Exception e) {
			logger.warn("redis info error",e);
		}
	}

	@Override
	public boolean add(T t, boolean updateOnExist) {
		if(t == null){
			logger.warn("add index error");
			return false;
		}
		if (getDao().save(t, updateOnExist)) {
			try {
				GenericInfoRao<T, PK> rao = getRao();
				if(rao != null){
					rao.add(t);
				}
			} catch (Exception e) {
				logger.warn("redis info error",e);
			}
			return true;
		}
		return false;
	}

	@Override
	public void update(T t) {
		if(t == null) return;
		getDao().update(t);
		try {
			GenericInfoRao<T, PK> rao = getRao();
			if(rao != null){
				rao.update(t);
			}
		} catch (Exception e) {
			logger.warn("redis info error",e);
		}
	}

	@Override
	public void update(T t, String... properties) {
		if(t == null) return;
		getDao().update(t, true, properties);
		try {
			GenericInfoRao<T, PK> rao = getRao();
			if(rao != null){
				rao.update(t, true, properties);
			}
		} catch (Exception e) {
			logger.warn("redis info error",e);
		}
	}
	
	@Override
	public void del(PK pk) {
		del(pk, null);
	}
	
	@Override
	public void del(PK pk, Object partitionHint) {
		if(pk == null) return;
		getDao().delete(pk, partitionHint);
		try {
			GenericInfoRao<T, PK> rao = getRao();
			if(rao != null){
				rao.delKey(pk);
			}
		} catch (Exception e) {
			logger.warn("redis info error",e);
		}
	}

	@Override
	public void resyncRedis(PK pk){
		try {
			GenericInfoRao<T, PK> rao = getRao();
			if(rao != null){
				rao.delKey(pk);
			}
		} catch (Exception e) {
			logger.warn("redis info error",e);
		}
	}

	@Override
	public void delUnique(T entity) {
		if(entity == null) return;
		getDao().deleteUnique(entity);
		try {
			GenericInfoRao<T, PK> rao = getRao();
			if(rao != null){
				rao.delUniqueKey(entity);
			}
		} catch (Exception e) {
			logger.warn("redis info error",e);
		}
	}
	
	@Override
	public T findById(PK pk) {
		return findById(pk, null);
	}
	
	@Override
	public T findById(PK pk, Object partitionHint) {
		if(pk == null) return null;
		GenericInfoRao<T, PK> rao = getRao();
		if(rao != null){
			try {
				T t = rao.get(pk);
				if(null != t){
					return t;
				}
			} catch (Exception e) {
				logger.warn("info cache error", e);
			}
		}
		
		T t = getDao().query(pk, partitionHint);
		if(t != null){
			try {
				fill(t);
				if(rao != null) rao.add(t);
			} catch (Exception e) {
				logger.warn("info cache error", e);
			}
		}
		
		return t;
	}
	
	@Override
	public T findByUniqueKey(T entity) {
		if(entity == null) return null;
		GenericInfoRao<T, PK> rao = getRao();
		if(rao != null){
			try {
				T t = rao.getUnique(entity);
				if(null != t){
					return t;
				}
			} catch (Exception e) {
				logger.warn("info cache error", e);
			}
		}
		
		T t = getDao().queryUnique(entity);
		if(t != null){
			try {
				fill(t);
				if(rao != null) rao.add(t);
			} catch (Exception e) {
				logger.warn("info cache error", e);
			}
		}
		
		return t;
	}
	
	@Override
	public String findFieldValue(PK pk, String field){
		return findFieldValue(pk, field, null);
	}

	@Override
	public String findFieldValue(PK pk, String field, Object partitionHint){
		GenericInfoRao<T, PK> rao = getRao();
		if(rao != null){
			try {
				String value = getRao().get(pk, field);
				if(null != value){
					return value;
				}
			} catch (Exception e) {
				logger.warn("info cache error", e);
			}
		}
		
		T t = getDao().query(pk, partitionHint);
		if(t != null){
			try {
				fill(t);
				if(rao != null) getRao().add(t);
				//TODO 需要从t直接获取field，而不是多读一次redis
			} catch (Exception e) {
				logger.warn("info cache error", e);
			}
		}
		
		try {
			return getRao().get(pk, field);
		} catch (Exception e) {
			logger.warn("info cache error", e);
		}
		
		return null;
	}
	
	@Override
	public List<T> find(T entity, int pageno, int pagesize, int asc,
			List<Expression> expressionConditions, String... conditions) {
		List<T> ts = getDao().query(entity, pageno, pagesize, asc, expressionConditions, conditions);
		if(ts != null){
			for (T t : ts) {
				if(t != null){
					try{
						fill(t);
					} catch (Exception e) {
						logger.warn("fill info cache error", e);
					}
				}
			}
		}
		return ts;
	}
	@Override
	public int count(T entity, List<Expression> expressionConditions,
			String... conditions) {
		return getDao().count(entity, expressionConditions, conditions);
	}
}
