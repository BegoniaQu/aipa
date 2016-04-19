package com.qy.data.common.rao.impl;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.qy.data.common.exception.RedisRuntimeException;
import com.qy.data.common.proxy.rao.HashCacheRao;
import com.qy.data.common.rao.GenericInfoRao;
import com.qy.data.common.rao.annotation.RedisEntity;
import com.qy.data.common.rao.annotation.RedisField;
import com.qy.data.common.rao.annotation.RedisId;
import com.qy.data.common.util.DateConvert;
import com.qy.data.common.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qy.data.common.exception.CloudPlatformRuntimeException;


/**
 * @author tianhui
 *
 * @param <T>
 * @param <PK>
 */
public abstract class AbstractGenericInfoRaoImpl<T extends Serializable, PK> implements GenericInfoRao<T, PK> {
	protected static final Logger logger = LoggerFactory.getLogger("com.anlaiye.data.common.rao.impl.info");
	
	private HashCacheRao hashCacheRao;
	private Integer seconds;
	private String keyPrefix;
	
	//private String entityName;
	private Map<String, String> propNameToRedisFieldName;
	private Map<String, Field> redisFieldNameToField;
	private Field idRedisField;
	private Map<String, Field> uniqueKeyRedisFields;
	
	protected Class<T> entityClass;
//    protected Class<PK> idClass;
	
	@SuppressWarnings("unchecked")
	public AbstractGenericInfoRaoImpl(){
		Class<?> c = getClass();
        Type t = c.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            this.entityClass = (Class<T>) p[0];
//            this.idClass = (Class<PK>) p[1];
        }
        
        parseEntity(entityClass);
	}

	@Override
	public void add(T entity) {
		try {
			String hashKey = uniqueKeyRedisFields != null ?
					getUniqueKey(entity) : 
					String.valueOf(idRedisField.get(entity));
			
			Set<String> props = propNameToRedisFieldName.keySet();
			Map<String, String> fieldValues = convert2Map(entity, 
					props.toArray(new String[props.size()]), false, false);
			
			if(!fieldValues.isEmpty()){
				hashCacheRao.hmset(hashKey, keyPrefix, fieldValues, seconds);
			} else {
				throw new RedisRuntimeException("entity can't all be null!, t=" + JsonUtil.getJsonFromObject(entity));
			}
		} catch (Exception e) {
			throw new RedisRuntimeException("add redis error", e);
		}
		
	}

	/**
	 * @param entity
	 * @return
	 * @throws IllegalAccessException
	 */
	private Map<String, String> convert2Map(T entity, String[] props, boolean skipMissingProperty, boolean allowNull){
		try {
			Map<String, String> fieldValues = new HashMap<String, String>();
			
			for(String prop:props){
				prop = prop.toLowerCase();
				String redisFieldName = propNameToRedisFieldName.get(prop);
				if(redisFieldName==null){
					if (skipMissingProperty) continue;
					throw new RedisRuntimeException(prop+" doesn't match any field!", entityClass);
				}
				Field field = redisFieldNameToField.get(redisFieldName);
				Object obj = field.get(entity);
				String value;
				
				if(obj == null){
					if (allowNull) {
						value = null;
					} else {
						continue;
					}
				} else if(obj instanceof Date){
					value = DateConvert.getConvert().toValue((Date) obj);
				} else if(obj.getClass().isArray()){
					value = JsonUtil.getJsonFromObject(field.get(entity));
				} else {
					value = String.valueOf(obj);
				}
				
				fieldValues.put(redisFieldName, value);
			}
			return fieldValues;
		} catch (Exception e) {
			throw new RedisRuntimeException("add redis error", e);
		}
	}

	@Override
	public T get(PK id) {
		Map<String, String> redisMap = hashCacheRao.hgetall(id, keyPrefix);
		
		T obj = convert2Domain(id, redisMap);
		
		return obj;
	}
	
	@Override
	public T getUnique(T entity){
		Map<String, String> redisMap = hashCacheRao.hgetall(getUniqueKey(entity), keyPrefix);
		
		T obj = convert2Domain(null, redisMap);
		
		return obj;
	}

	/**
	 * @param id
	 * @param redisMap
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private T convert2Domain(PK id, Map<String, String> redisMap) {
		if (redisMap == null) {
			return null;
		}
		try {
			T obj = entityClass.newInstance();
			
			for(String prop:propNameToRedisFieldName.keySet()){
				String redisFieldName = propNameToRedisFieldName.get(prop);
				Field field = redisFieldNameToField.get(redisFieldName);
				String value = redisMap.get(redisFieldName);
				
				if(value==null){
					continue;
				}
				
				Class<?> type = field.getType();
				if(type == String.class){
					field.set(obj, value);
				} else if (type == Date.class){
					field.set(obj, DateConvert.getConvert().getValue(value));
				} else if (type == Long.class || type.getName().equalsIgnoreCase("long")){
					field.set(obj, Long.valueOf(value));
				} else if (type == Integer.class || type.getName().equalsIgnoreCase("int")){
					field.set(obj, Integer.valueOf(value));
				} else if (type == Short.class || type.getName().equalsIgnoreCase("short")){
					field.set(obj, Short.valueOf(value));
				} else if (type == Byte.class || type.getName().equalsIgnoreCase("byte")){
					field.set(obj, Byte.valueOf(value));
				} else if (type == Boolean.class || type.getName().equalsIgnoreCase("boolean")){
					field.set(obj, Boolean.valueOf(value));
				} else if (type == Double.class || type.getName().equalsIgnoreCase("double")){
					field.set(obj, Double.valueOf(value));
				} else if (type.isArray()){
					field.set(obj, JsonUtil.parserJsonArray(value, field.getType().getComponentType()));
				} else {
					throw new CloudPlatformRuntimeException("unsupported type "
							+ type + " row data = "+ value);
				}
			}
			
			if (id != null) {
				idRedisField.set(obj, id);
			}
			return obj;
		} catch (Exception e) {
			throw new RedisRuntimeException("get error", e);
		}
		
	}
	
	@Override
	public String get(PK id, String prop){
		prop = prop.toLowerCase();
		String redisFieldName = propNameToRedisFieldName.get(prop);
		
		String value = null;
		if(redisFieldName!=null){
			value = hashCacheRao.hget(id, keyPrefix, redisFieldName);
		} else {
			logger.warn("unknown field when get, keyPrefix={}, filed={}", keyPrefix, prop);
		}
		
		return value;
	}
	
	@Override
	public Boolean update(PK id, String prop, String value){//TODO:想清掉一些null
		prop = prop.toLowerCase();
		String redisFieldName = propNameToRedisFieldName.get(prop);
		
		if(redisFieldName==null){
			throw new RedisRuntimeException(prop+" doesn't match any field!", entityClass);
		}
		
		if(value==null){
			return hashCacheRao.hdel(id, keyPrefix, prop);
		} else {
			return hashCacheRao.hsetx(id, keyPrefix, redisFieldName, value, seconds);
		}
	}
	

	@Override
	public void update(T entity) {
		update(entity, false, false, propNameToRedisFieldName.keySet().toArray(new String[propNameToRedisFieldName.keySet().size()]));
	}
	
	@Override
	public Boolean update(T entity, String... props) {
		return update(entity, false, props);
	}
	
	@Override
	public Boolean update(T entity, boolean skipMissingProperty, String... props) {
		return update(entity, skipMissingProperty, true, props);
	}
	
	public Boolean update(T entity, boolean skipMissingProperty, boolean updateNull, String... props) {
		try {
			String hashKey = uniqueKeyRedisFields != null ?
					getUniqueKey(entity) : 
					String.valueOf(idRedisField.get(entity));

			Map<String, String> fieldValues = convert2Map(entity, props, skipMissingProperty, updateNull);
			if (updateNull) {
				Set<String> toDel = new HashSet<String>();
				for (Entry<String, String> entry : fieldValues.entrySet()) {
					if (entry.getValue() == null) {
						hashCacheRao.hdel(hashKey, keyPrefix, entry.getKey());
						toDel.add(entry.getKey());
					}
				}
				for (String key: toDel) {
					fieldValues.remove(key);
				}
			}
			
			if (skipMissingProperty && fieldValues.isEmpty()) {
				return hashCacheRao.existKey(hashKey, keyPrefix);
			}
			if(!fieldValues.isEmpty()){
				return hashCacheRao.hmsetx(hashKey, keyPrefix, fieldValues, seconds);
			} else {
				throw new RedisRuntimeException("value of "+props+" cann't all be null!, t=" + JsonUtil.getJsonFromObject(entity));
			}
		} catch (Exception e) {
			throw new RedisRuntimeException("update error ", e);
		}
		
	}
	
	@Override
	public Long incre(PK id, String prop, int increCount){
		prop = prop.toLowerCase();
		String redisFieldName = propNameToRedisFieldName.get(prop);
		
		if(redisFieldName==null){
			throw new RedisRuntimeException(prop+" doesn't match any field!", entityClass);
		}
		
		Field field = redisFieldNameToField.get(redisFieldName);
		Class<?> type = field.getType();
		
		if (type == Long.class || type.getName().equalsIgnoreCase("long") || type == Integer.class 
				|| type.getName().equalsIgnoreCase("int") || type == Short.class
				|| type.getName().equalsIgnoreCase("short")){
			Map<String, Long> map = new HashMap<String, Long>(1);
			map.put(redisFieldName, (long)increCount);
			
			Map<String, Long> res =  hashCacheRao.hincreby(id, keyPrefix, map, seconds);
			if(res!= null){
				return res.get(redisFieldName);
			} else {
				return null;
			}
		} else {
			throw new RedisRuntimeException("value of "+prop+" cann't be null!", entityClass);
		}
	}
	
	@Override
	public void delKey(PK id){
		hashCacheRao.del(id, keyPrefix);
	}
	
	@Override
	public void delUniqueKey(T entity){
		hashCacheRao.del(getUniqueKey(entity), keyPrefix);
	}
	
	protected String getUniqueKey(T entity) {
		if (uniqueKeyRedisFields == null) {
			throw new RedisRuntimeException("domain is not referred by unique key!", entityClass);
		}
		StringBuilder sb = new StringBuilder();
		try {
			for (Entry<String, Field> uniqEntry : uniqueKeyRedisFields.entrySet()) {
				Object value = uniqEntry.getValue().get(entity);
				if (value != null) {
					if (sb.length() > 0) {
						sb.append(":");
					}
					sb.append(value);
				} else {
					throw new RedisRuntimeException("not all fields for unique key are provided:"+uniqEntry.getKey(), entityClass);
				}
			}
			return sb.toString();
		} catch (IllegalAccessException e) {
			throw new RedisRuntimeException("failed to access unique key on domain!", entityClass);
		} catch (IllegalArgumentException e) {
			throw new RedisRuntimeException("failed to get value for unique key on domain!", entityClass);
		}
	}
	
	private void parseEntity(Class<T> entityClass){
		if(entityClass.isAnnotationPresent(RedisEntity.class)){
			RedisEntity annotation = entityClass.getAnnotation(RedisEntity.class);
			String entityName = annotation.name();
			if(entityName.equals("")){
				entityName = entityClass.getName();
			}
			
			if(keyPrefix == null){
				keyPrefix = entityName;
			}
		}
		
		Field[] fields = entityClass.getDeclaredFields();
		propNameToRedisFieldName = new HashMap<String, String>();
		redisFieldNameToField = new HashMap<String, Field>();
		
		if(fields!=null){
			for(Field field:fields){
				if(field.isAnnotationPresent(RedisField.class)){
					RedisField annotation = field.getAnnotation(RedisField.class);
					String redisFieldName = annotation.name();
					if(redisFieldName.equals("")){
						redisFieldName = field.getName();
					}

					field.setAccessible(true);
					propNameToRedisFieldName.put(field.getName().toLowerCase(), redisFieldName.toLowerCase());
					redisFieldNameToField.put(redisFieldName.toLowerCase(), field);
					if (annotation.inUniqueKey()) {
						if (uniqueKeyRedisFields == null) {
							uniqueKeyRedisFields = new TreeMap<String, Field>();
						}
						uniqueKeyRedisFields.put(redisFieldName, field);
					}
					
				} else if(field.isAnnotationPresent(RedisId.class)){//主键不存储
					field.setAccessible(true);
					idRedisField = field;
				}
			}
		}
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
