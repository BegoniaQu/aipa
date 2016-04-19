package com.qy.data.common.rao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.qy.data.common.domain.GenericMapInfo;
import com.qy.data.common.exception.RedisRuntimeException;
import com.qy.data.common.proxy.rao.HashCacheRao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qy.data.common.rao.GenericMapInfoRao;

/**
 * @author yansqrt
 */
public class AbstractGenericMapInfoRaoImpl<T extends GenericMapInfo, PK>
	implements GenericMapInfoRao<GenericMapInfo, PK> {

	protected static final Logger logger = LoggerFactory.getLogger(AbstractGenericMapInfoRaoImpl.class);
	
	private HashCacheRao hashCacheRao;
	private Integer seconds;
	private String keyPrefix;
	
	protected Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public AbstractGenericMapInfoRaoImpl(){
		Class<?> c = getClass();
		Type t = c.getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			this.entityClass = (Class<T>) p[0];
//          this.idClass = (Class<PK>) p[1];
		}
//		parseEntity(entityClass);
	}

	@Override
	public void add(GenericMapInfo entity) {
		try {
//			String hashKey = String.valueOf(idRedisField.get(entity));
//			
//			if (entity.getAttributes() != null && !entity.getAttributes().isEmpty()) {
//				final Map<String, String> tmp = new HashMap<String, String>(entity.getAttributes().size());
//					for (final Entry<String, Object> entry : entity.getAttributes().entrySet()) {
//						tmp.put(entry.getKey(), (String)entry.getValue());
//					}
//				hashCacheRao.hmset(hashKey, keyPrefix, tmp, seconds);
//				tmp.clear();
//			} else {
//				throw new RedisRuntimeException("entity can't all be null!, t=" + JsonUtil.getJsonFromObject(entity));
//			}
		} catch (Exception e) {
			throw new RedisRuntimeException("add redis error", e);
		}
		
	}

	@Override
	public GenericMapInfo get(PK id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericMapInfo getUnique(GenericMapInfo t) {
		throw new RedisRuntimeException("Unique key unsupported yet", entityClass);
	}

	@Override
	public String get(PK id, String prop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean update(PK id, String prop, String value) {
		if (value == null){
			return hashCacheRao.hdel(id, keyPrefix, prop);
		} else {
			return hashCacheRao.hsetx(id, keyPrefix, prop, value, seconds);
		}
	}

	@Override
	public Boolean update(GenericMapInfo entity, boolean skipMissingProperty,
			String... props) {
		throw new RedisRuntimeException("Update entity unsupported yet", entityClass);
	}

	@Override
	public void update(GenericMapInfo entity) {
		throw new RedisRuntimeException("Update entity unsupported yet", entityClass);
		
	}

	@Override
	public Boolean update(GenericMapInfo entity, String... fields) {
		throw new RedisRuntimeException("Update entity unsupported yet", entityClass);
	}

	@Override
	public Long incre(PK id, String prop, int increCount) {
		Map<String, Long> map = new HashMap<String, Long>(1);
		map.put(prop, (long)increCount);
		Map<String, Long> res =  hashCacheRao.hincreby(id, keyPrefix, map, seconds);
		if(res!= null){
			return res.get(prop);
		} else {
			return null;
		}
	}

	@Override
	public void delKey(PK id) {
		hashCacheRao.del(id, keyPrefix);
	}

	@Override
	public void delUniqueKey(GenericMapInfo t) {
		throw new RedisRuntimeException("Unique key unsupported yet", entityClass);
	}

	public HashCacheRao getHashCacheRao() {
		return hashCacheRao;
	}
	public void setHashCacheRao(HashCacheRao hashCacheRao) {
		this.hashCacheRao = hashCacheRao;
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
}
