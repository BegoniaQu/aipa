package com.qy.data.common.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.dao.annotation.GenerationType;
import com.qy.data.common.dao.annotation.Id;
import com.qy.data.common.exception.GenericDaoException;
import com.qy.data.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.dao.annotation.GeneratedValue;
import com.qy.data.common.dao.annotation.Table;
import com.mysql.jdbc.StringUtils;

/**
 * @author qy
 *
 * @param <T>
 * @param <PK>
 */
public class AbstractGenericDaoImpl<T extends Serializable, PK> extends BaseNamedParamDaoImpl implements GenericDao<T, PK>{
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractGenericDaoImpl.class);
	
	protected static final String QUERY_BY_ENTITY_FORMAT="select * from %s where %s";
	protected static final String COUNT_BY_ENTITY_FORMAT="select count(0) from %s where %s";
	protected static final String QUERY_BY_ID_FORMAT="select * from %s  where %s=:%s";
	protected static final String SAVE_FORMAT="insert into %s(%s) values(%s)";
	protected static final String SAVE_ON_DUP_UPDATE_FORMAT="insert into %s(%s) values(%s) on duplicate key update %s";
	protected static final String SAVE_ON_DUP_IGNORE_FORMAT="insert ignore into %s(%s) values(%s)";
	protected static final String UPDATE_BY_ID_FORMAT="update %s set %s where %s=:%s";
	protected static final String UPDATE_BY_ENTITY_FORMAT="update %s set %s where %s";
	protected static final String DELETE_BY_ID_FORMAT="delete from %s where %s=:%s";
	protected static final String DELETE_BY_ENTITY_FORMAT="delete from %s where %s";
	protected Map<String, String> propNameToColumnName;
	protected Map<String, Field> columnNameToField;
	protected Map<String, Method> columnNameToMethod;
	protected Map<String, Field> uniqueKeyFields;
	protected RowMapper<T> mapper = new BeanMapper();
	
	protected Class<T> entityClass;
	protected String tableName;
	protected String idName;
	protected Field idField;
	protected boolean idAuto = false;
	protected Field partitionField;
	protected Integer partitionCount;
	
	@SuppressWarnings("unchecked")
	public AbstractGenericDaoImpl(){
		Class<?> c = getClass();
        Type t = c.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            this.entityClass = (Class<T>) p[0];
        }
		parseEntity(entityClass);
	}
	
//	public void setDataSource(DataSource dataSource) {
//		mapper = new BeanMapper();
//		this.jdbcTemplate = new JdbcTemplate(dataSource);
//	}
//	
	@Override
	public T query(PK id) {
		return query(id, null);
	}

	@Override
	public T query(PK id, Object partitionHint) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put(idName, id);
		
		List<T> results = jdbcTemplate.query(
				String.format(QUERY_BY_ID_FORMAT, getTabName(partitionHint != null ? partitionHint : id), idName, idName), 
				paramMap, 
				mapper);
		
		if (null == results || results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}

	@Override
	public T queryUnique(T entity) {
		if (uniqueKeyFields != null) {
			String[] uniqueFields = new String[uniqueKeyFields.size()];
			int i = 0;
			try {
				for (Entry<String, Field> uniqEntry : uniqueKeyFields.entrySet()) {
					Object value = uniqEntry.getValue().get(entity);
					if (value != null) {
						uniqueFields[i++] = uniqEntry.getKey();
					} else {
						throw new GenericDaoException(entityClass, "not all fields for unique key are provided:"+uniqEntry.getKey());
					}
				}
			} catch (IllegalAccessException e) {
				throw new GenericDaoException(entityClass, "failed to access unique key on domain!");
			} catch (IllegalArgumentException e) {
				throw new GenericDaoException(entityClass, "failed to get value for unique key on domain!");
			}
			List<T> results = query(entity, null, uniqueFields);
			if (results != null && !results.isEmpty()) {
				if (results.size() > 1) {
					throw new GenericDaoException(entityClass, "Multiple results found, unique key not constrainted on domain!");
				} else {
					return results.get(0);
				}
			} else {
				return null;
			}
		} else {
			throw new GenericDaoException(entityClass, "domain is not referred by unique key!");
		}
	}

	@Override
	public boolean save(T entity) {
		String[] properties = propNameToColumnName.keySet().toArray(new String[propNameToColumnName.size()]);
		try {
			Map<String, Object> map = getNameValues(entity, properties, false);
			StringBuilder columns = new StringBuilder();
			StringBuilder parameters = new StringBuilder();
			
			for(String name: map.keySet()){
				columns.append(name).append(",");
			    parameters.append(":").append(name).append(",");
			}
			
			columns = columns.deleteCharAt(columns.length()-1);
			parameters = parameters.deleteCharAt(parameters.length()-1);
		
			this.jdbcTemplate.update("set names utf8mb4", new HashMap<String, String>());
			if(idAuto){
				KeyHolder keyHolder = new GeneratedKeyHolder();
				int count = jdbcTemplate.update(String.format(SAVE_FORMAT, getTabName(entity), columns, parameters), new MapSqlParameterSource(map), keyHolder);
				idField.setAccessible(true);
				if (idField.getType()==Integer.class){
					idField.set(entity, keyHolder.getKey().intValue());
				}else{
					idField.set(entity, keyHolder.getKey().longValue());

				}
				return count > 0;
			} else {
				return jdbcTemplate.update(String.format(SAVE_FORMAT, getTabName(entity), columns, parameters), map)>0;
			}
			
		} catch (Exception e) {
			throw new GenericDaoException("save error", e);
		}
	}
	
	@Override
	public boolean save(T entity, boolean updateOnExist) {
		String[] properties = propNameToColumnName.keySet().toArray(new String[propNameToColumnName.size()]);
		try {
			Map<String, Object> map = getNameValues(entity, properties, false);
			StringBuilder columns = new StringBuilder();
			StringBuilder parameters = new StringBuilder();
			StringBuilder onDupUpdates = new StringBuilder();
			
			for(String name: map.keySet()){
				columns.append(name).append(",");
			    parameters.append(":").append(name).append(",");
			    if (uniqueKeyFields == null || !uniqueKeyFields.containsKey(name)) {
			    	onDupUpdates.append(name).append("=:").append(name).append(",");
			    }
			}
			if (onDupUpdates.length() > 0) {
				onDupUpdates.setLength(onDupUpdates.length()-1);
			}
			
			columns = columns.deleteCharAt(columns.length()-1);
			parameters = parameters.deleteCharAt(parameters.length()-1);
		
			String addSql = updateOnExist ? 
					String.format(SAVE_ON_DUP_UPDATE_FORMAT, getTabName(entity), columns, parameters, onDupUpdates) : 
					String.format(SAVE_ON_DUP_IGNORE_FORMAT, getTabName(entity), columns, parameters);
			
			this.jdbcTemplate.update("set names utf8mb4", new HashMap<String, String>());
			if(idAuto){
				KeyHolder keyHolder = new GeneratedKeyHolder();
				int count = jdbcTemplate.update(addSql, new MapSqlParameterSource(map), keyHolder);
				idField.setAccessible(true);
				if (idField.getType()==Integer.class){
					idField.set(entity, keyHolder.getKey().intValue());
				}else{
					idField.set(entity, keyHolder.getKey().longValue());

				}
				return count > 0;
			} else {
				return jdbcTemplate.update(addSql, map)>0;
			}
			
		} catch (Exception e) {
			throw new GenericDaoException("save error", e);
		}
	}
	
	@Override
	public boolean update(T entity){
		if (uniqueKeyFields != null) {
			return updateUnique(entity);
		}
		if(StringUtils.isEmptyOrWhitespaceOnly(idName)){
			throw new GenericDaoException(entityClass, idName + " is empty! t=" + JsonUtil.getJsonFromObject(entity));
		}
		
		String[] properties = propNameToColumnName.keySet().toArray(new String[propNameToColumnName.size()]);
		Map<String, Object> map = getNameValues(entity, properties, false);
		if(map.isEmpty()){
			throw new GenericDaoException(entityClass, "update empty, t=" + JsonUtil.getJsonFromObject(entity));
		}
		
		StringBuilder parameters = new StringBuilder();

		for (String property : properties) {//如果property对象的属性为null，更新值为null
			property = property.toLowerCase();
			if(map.containsKey(property)){
				parameters.append(property).append("=:").append(property).append(",");
			} else if (!property.equals(idName)){
				parameters.append(property).append("=null ").append(",");
			}
		}
//		for(String name:map.keySet()){//忽略为null的属性
//			parameters.append(name).append("=:").append(name).append(",");
//		}
		
		if(!map.containsKey(idName)){
			try {
				idField.setAccessible(true);
				map.put(idName, idField.get(entity));
			} catch (Exception e) {
				throw new GenericDaoException(entityClass, idName + " error! t=" + JsonUtil.getJsonFromObject(entity));
			}
		}
		
		String sql = String.format(UPDATE_BY_ID_FORMAT, getTabName(entity), parameters.deleteCharAt(parameters.length()-1), idName, idName);
		this.jdbcTemplate.update("set names utf8mb4", new HashMap<String, String>());
		return jdbcTemplate.update(sql, map)>0;
	}
	
	@Override
	public boolean updateUnique(T entity) {
		if (uniqueKeyFields != null) {
		
			String[] uniqueFields = new String[uniqueKeyFields.size()];
			int i = 0;
			try {
				for (Entry<String, Field> uniqEntry : uniqueKeyFields.entrySet()) {
					Object value = uniqEntry.getValue().get(entity);
					if (value != null) {
						uniqueFields[i++] = uniqEntry.getKey();
					} else {
						throw new GenericDaoException(entityClass, "not all fields for unique key are provided:"+uniqEntry.getKey());
					}
				}
			} catch (IllegalAccessException e) {
				throw new GenericDaoException(entityClass, "failed to access unique key on domain!");
			} catch (IllegalArgumentException e) {
				throw new GenericDaoException(entityClass, "failed to get value for unique key on domain!");
			}
			return updateByCondition(entity, null, uniqueFields);
		} else {
			throw new GenericDaoException(entityClass, "domain is not referred by unique key!");
		}
	}
	
	@Override
	public boolean update(T entity, String... properties) {
		return update(entity, false, properties);
	}
	
	@Override
	public boolean update(T entity, boolean skipMissingProperty, String... properties) {
		if (uniqueKeyFields != null) {
			return updateUnique(entity, skipMissingProperty, properties);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(idName)){
			throw new GenericDaoException(entityClass, idName + " is empty! t=" + JsonUtil.getJsonFromObject(entity));
		}
		
		Map<String, Object> map = getNameValues(entity, properties, skipMissingProperty);
		if (skipMissingProperty && map.isEmpty()) {
			return true;
		}
		StringBuilder parameters = new StringBuilder();
		
		for (String property : properties) {//如果property对象的属性为null，更新值为null
			property = property.toLowerCase();
			if(map.containsKey(property)){
				parameters.append(property).append("=:").append(property).append(",");
			} else {
				parameters.append(property).append("=null ").append(",");
			}
		}
		
		if(!map.containsKey(idName)){
			try {
				idField.setAccessible(true);
				map.put(idName, idField.get(entity));
			} catch (Exception e) {
				throw new GenericDaoException(entityClass, idName + " error! t=" + JsonUtil.getJsonFromObject(entity), e);
			}
		}
		
		String sql = String.format(UPDATE_BY_ID_FORMAT, getTabName(entity), parameters.deleteCharAt(parameters.length()-1), idName, idName);
		this.jdbcTemplate.update("set names utf8mb4", new HashMap<String, String>());
		return jdbcTemplate.update(sql, map)>0;
	}
	

	
	@Override
	public boolean updateUnique(T entity, boolean skipMissingProperty, String... properties) {
		if (uniqueKeyFields != null) {

			Map<String, Object> map = getNameValues(entity, properties, skipMissingProperty);
			if (skipMissingProperty && map.isEmpty()) {
				return true;
			}
			StringBuilder parameters = new StringBuilder();
			
			for (String property : properties) {//如果property对象的属性为null，更新值为null
				property = property.toLowerCase();
				if(map.containsKey(property)){
					parameters.append(property).append("=:").append(property).append(",");
				} else {
					parameters.append(property).append("=null ").append(",");
				}
			}
			
			String[] uniqueFields = new String[uniqueKeyFields.size()];
			int i = 0;
			try {
				for (Entry<String, Field> uniqEntry : uniqueKeyFields.entrySet()) {
					Object value = uniqEntry.getValue().get(entity);
					if (value != null) {
						uniqueFields[i++] = uniqEntry.getKey();
					} else {
						throw new GenericDaoException(entityClass, "not all fields for unique key are provided:"+uniqEntry.getKey());
					}
				}
			} catch (IllegalAccessException e) {
				throw new GenericDaoException(entityClass, "failed to access unique key on domain!");
			} catch (IllegalArgumentException e) {
				throw new GenericDaoException(entityClass, "failed to get value for unique key on domain!");
			}

			StringBuilder builder = new StringBuilder();
			buildCondition(entity, null, map, builder, uniqueFields);
			
			String sql = String.format(UPDATE_BY_ENTITY_FORMAT, getTabName(entity), 
					parameters.deleteCharAt(parameters.length()-1), 
					builder.deleteCharAt(builder.length()-1));
			this.jdbcTemplate.update("set names utf8mb4", new HashMap<String, String>());
			return jdbcTemplate.update(sql, map)>0;
			
		} else {
			throw new GenericDaoException(entityClass, "domain is not referred by unique key!");
		}
	}
	
	@Override
	public boolean updateByCondition(T entity, List<Expression> expressionConditions,  String... conditions) {
		String[] properties = propNameToColumnName.keySet().toArray(new String[propNameToColumnName.size()]);
		Map<String, Object> map = getNameValues(entity, properties, false);
		StringBuilder parameters = new StringBuilder();
		
		for (String property : properties) {//如果property对象的属性为null，更新值为null
			if (containsProperty(expressionConditions, property) ){
				continue;
			}
			if(map.containsKey(property)){
				parameters.append(property).append("=:").append(property).append(",");
			} else if (!property.equals(idName)){
				parameters.append(property).append("=null ").append(",");
			}
			
		}
		
		StringBuilder builder = new StringBuilder();
		buildCondition(entity, expressionConditions, map, builder, conditions);
		
		String sql = String.format(UPDATE_BY_ENTITY_FORMAT, getTabName(entity), 
				parameters.deleteCharAt(parameters.length()-1), 
				builder.deleteCharAt(builder.length()-1));
		this.jdbcTemplate.update("set names utf8mb4", new HashMap<String, String>());
		return jdbcTemplate.update(sql, map)>0;
	}
	
	@Override
	public boolean delete(PK id) {
		return delete(id, null);
	}
	
	@Override
	public boolean delete(PK id, Object partitionHint) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put(idName, id);
		
		return jdbcTemplate.update(
				String.format(DELETE_BY_ID_FORMAT, 
						getTabName(partitionHint != null ? partitionHint : id), idName, idName), 
						paramMap) > 0;
	}

	@Override
	public boolean deleteUnique(T entity) {
		if (uniqueKeyFields != null) {
			String[] uniqueFields = new String[uniqueKeyFields.size()];
			int i = 0;
			try {
				for (Entry<String, Field> uniqEntry : uniqueKeyFields.entrySet()) {
					Object value = uniqEntry.getValue().get(entity);
					if (value != null) {
						uniqueFields[i++] = uniqEntry.getKey();
					} else {
						throw new GenericDaoException(entityClass, "not all fields for unique key are provided:"+uniqEntry.getKey());
					}
				}
			} catch (IllegalAccessException e) {
				throw new GenericDaoException(entityClass, "failed to access unique key on domain!");
			} catch (IllegalArgumentException e) {
				throw new GenericDaoException(entityClass, "failed to get value for unique key on domain!");
			}
			return delete(entity, null, uniqueFields);
		} else {
			throw new GenericDaoException(entityClass, "domain is not referred by unique key!");
		}
	}
	
	@Override
	public boolean delete(T entity, List<Expression> expressionConditions, String... conditions) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			StringBuilder builder = new StringBuilder();
			buildCondition(entity, expressionConditions, params, builder, conditions);

			String sql = String.format(DELETE_BY_ENTITY_FORMAT, getTabName(entity), builder.toString());
			return jdbcTemplate.update(sql, params)>0;
		} catch (Exception e) {
			throw new GenericDaoException("delete error", e);
		}
	}
	
	@Override
	public List<T> query(T entity, List<Expression> expressionConditions, String... conditions){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder builder = new StringBuilder();
		buildCondition(entity, expressionConditions, params, builder, conditions);

		String sql = String.format(QUERY_BY_ENTITY_FORMAT, getTabName(entity), builder.toString());
		return  this.jdbcTemplate.query(sql, params, mapper);
	}
	
	@Override
	public int count(T entity, List<Expression> expressionConditions, String... conditions){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder builder = new StringBuilder();
		buildCondition(entity, expressionConditions, params, builder, conditions);

		String sql = String.format(COUNT_BY_ENTITY_FORMAT, getTabName(entity), builder.toString());

		return jdbcTemplate.queryForObject(sql, params, Integer.class);
	}
	
	@Override
	public List<T> query(T entity, int pageno, int pagesize, int asc, List<Expression> expressionConditions, String... conditions){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder builder = new StringBuilder();
		buildCondition(entity, expressionConditions, params, builder, conditions);

		String sql = String.format(QUERY_BY_ENTITY_FORMAT, getTabName(entity), builder.toString());
		sql += " order by " + idName;
        if (asc == 1) {
            sql += " asc ";
        } else {
            sql += " desc ";
        }
        sql += " limit " + pagesize*pageno + "," + pagesize;
	
        logger.debug("sql={}, param={}", sql, params);
        return this.jdbcTemplate.query(sql, params, mapper);
	}

	protected void buildCondition(T entity, List<Expression> expressionConditions,
			Map<String, Object> params, StringBuilder builder,
			String... conditions) {
		if(conditions.length != 0){
			for(String prop:conditions){
				prop = prop.toLowerCase();
				String columnName = propNameToColumnName.get(prop);
				if(StringUtils.isEmptyOrWhitespaceOnly(columnName)){
					throw new GenericDaoException(entityClass, prop + " doesn't match any column!");
				}
				
				Field field = columnNameToField.get(columnName);
				try {
					field.setAccessible(true);
					Object value = field.get(entity);
					if(value == null){
						builder.append(columnName).append(" is null ").append(" and ");
					} else {
						builder.append(columnName).append("=:").append(prop).append(" and ");
						params.put(prop, value);
					}
				} catch (Exception e) {
					throw new GenericDaoException("build by condition error", e);
				}
			
			}
		}
		
		if(expressionConditions != null){
			int expCt=1;
			for (Expression entry : expressionConditions) {
				
				String columnName = propNameToColumnName.get(entry.property.toLowerCase());
				if(StringUtils.isEmptyOrWhitespaceOnly(columnName)){
					throw new GenericDaoException(entityClass, entry.property + " doesn't match any column!");
				}
				while (params.containsKey("exp"+expCt)) expCt++;
				if(entry.operator != null && entry.value != null) {
					builder.append(columnName).append(" " + entry.operator + " :").append("exp"+expCt).append(" and ");
					params.put("exp"+expCt, entry.value);
				}
			}
		}
		
		builder.append(" 1=1 ");
	}
	
	/**
	 * @param id
	 * @return
	 */
	public String getTabName(Object id) {
		String tbName = null;
		if(partitionField != null ){//id必须是分片ID
			Class<?> type = partitionField.getType();
			if (type == Long.class || type.getName().equalsIgnoreCase("long") || type == Integer.class 
					|| type.getName().equalsIgnoreCase("int") || type == Short.class
					|| type.getName().equalsIgnoreCase("short")){
				tbName = tableName+"_"+getTableIndex((long)id, partitionCount);
			} else {
				tbName = tableName+"_"+getTableIndex(String.valueOf(id), partitionCount);
			}
		} else {
			tbName = tableName;
		}
		return tbName;
	}
	
	public String getTabName(T t) {
		try {
			String tbName = null;
			if(partitionField != null ){//id必须是分片ID
				Object partitionKey = partitionField.get(t);
				
				Class<?> type = partitionField.getType();
				if (type == Long.class || type.getName().equalsIgnoreCase("long")) {
					tbName = tableName+"_"+getTableIndex((long)partitionKey, partitionCount);
				} else if (type == Integer.class || type.getName().equalsIgnoreCase("int")) {
					tbName = tableName+"_"+getTableIndex((int)partitionKey, partitionCount);
				} else if (type == Short.class || type.getName().equalsIgnoreCase("short")) {
					tbName = tableName+"_"+getTableIndex((short)partitionKey, partitionCount);
				} else {
					tbName = tableName+"_"+getTableIndex(String.valueOf(partitionKey), partitionCount);
				}
			} else {
				tbName = tableName;
			}
			return tbName;
		} catch (Exception e) {
			throw new GenericDaoException(entityClass, "get table name error");
		}
	}
	
//	@SuppressWarnings("rawtypes")
//	private String valueFromList(List list){
//		if(null == list){
//			return null;
//		}
//		return JsonUtil.getJsonFromObject(list);
//		StringBuilder value = new StringBuilder();
//		for(Object o:list){
//			if(o!=null){
//				value.append(o).append(",");
//			}
//		}
//		if(StringUtils.isNullOrEmpty(value.toString())){
//			value.deleteCharAt(value.length()-1);
//		}
//		
//		return value.toString();
//	}
//	
//	private List<String> valueToList(String value){
//		return JsonUtil.parserJsonList(value, String.class);
//	}

	private void parseEntity(Class<?> clazz){
        if (clazz!=null) {
            if(clazz.isAnnotationPresent(Table.class)){
    			Table annotation = (Table) clazz.getAnnotation(Table.class);
    			tableName = annotation.name();
    		}else{
    			tableName = clazz.getName();
    		}
            propNameToColumnName = new HashMap<String, String>();
            columnNameToField = new HashMap<String, Field>();
    		Field[] fields = clazz.getDeclaredFields();
    		for (Field field : fields) {
    			String columnName = null;
    			String fieldName = field.getName().toLowerCase();
    			
    			if (field.isAnnotationPresent(Column.class)||field.isAnnotationPresent(Id.class)) {
    				if(field.isAnnotationPresent(Column.class)){
    					Column annotation = field.getAnnotation(Column.class);
        				columnName = annotation.name();
        				
        				if(annotation.forPartition()){
        					field.setAccessible(true);
        				    partitionField = field;
        				    partitionCount = annotation.partitionCount();
        				}
        				if (annotation.inUniqueKey()) {
        					if (uniqueKeyFields == null) {
        						uniqueKeyFields = new TreeMap<String, Field>();
        					}
        					field.setAccessible(true);
        					uniqueKeyFields.put(fieldName, field);
        				}
    				}else{
    					Id annotation = field.getAnnotation(Id.class);
    					columnName = annotation.name();
    					if(field.isAnnotationPresent(GeneratedValue.class)){
    						GeneratedValue a = field.getAnnotation(GeneratedValue.class);
    						if(a.strategy()== GenerationType.AUTO){//自增ID，没有必要save
    							idAuto = true;;
    						}
    					}
    					if(annotation.forPartition()){
        					field.setAccessible(true);
        				    partitionField = field;
        				    partitionCount = annotation.partitionCount();
        				}
        				if (annotation.inUniqueKey()) {
        					if (uniqueKeyFields == null) {
        						uniqueKeyFields = new TreeMap<String, Field>();
        					}
        					field.setAccessible(true);
        					uniqueKeyFields.put(fieldName, field);
        				}
    				}

        			if(StringUtils.isEmptyOrWhitespaceOnly(columnName)){
        				columnName = fieldName;
        			}
        			
        			columnName = columnName.toLowerCase();
    				propNameToColumnName.put(fieldName, columnName);
        			
        			if(field.isAnnotationPresent(Id.class)){
        				idName = columnName;
        				idField = field;
        			}
        			
        			columnNameToField.put(columnName, field);
    			}
    		}
    		
    		columnNameToMethod = new HashMap<String, Method>();
    		Method[] methods = clazz.getDeclaredMethods();
    		for (Method method : methods) {
    			String columnName = null;
    			if (method.isAnnotationPresent(Column.class)) {
    				Column annotation = method.getAnnotation(Column.class);
    				columnName = annotation.name();
    				
        			String methodName = method.getName().toLowerCase();
        			String fieldName = methodName.substring(3, methodName.length()).toLowerCase();
        			fieldName = fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1);
        			try {
						Field f = clazz.getDeclaredField(fieldName);
						if(f!=null){
							if(StringUtils.isEmptyOrWhitespaceOnly(columnName)){
								columnName = fieldName;
							}
							
							columnName = columnName.toLowerCase();
							
							columnNameToField.put(columnName, f);
							
							columnNameToMethod.put(columnName, method);
							
							propNameToColumnName.put(fieldName, columnName);
						}
						
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					}
        			
    			}
    		}
        }
		
	}
	
	private Map<String, Object> getNameValues(Object entity, String[] properties, boolean skipMissingProperty){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			for (String propName: properties) {
				propName = propName.toLowerCase();
				String columnName = propNameToColumnName.get(propName);
				if(StringUtils.isEmptyOrWhitespaceOnly(columnName)){
					if (skipMissingProperty) continue;
					throw new GenericDaoException(entityClass, propName + " doesn't match any column!");
				}
				
				Field field = columnNameToField.get(columnName);
					
				if(field.isAnnotationPresent(Id.class)&&field.isAnnotationPresent(GeneratedValue.class)){
					GeneratedValue a = field.getAnnotation(GeneratedValue.class);
					if(a.strategy()==GenerationType.AUTO){//自增ID，没有必要save
						continue;
					}
				}
				
				field.setAccessible(true);
				Object value;
				if (field.getType().isArray()) {
					value = field.get(entity);
					value = value == null ? null : JsonUtil.getJsonFromObject(value);
				} else {
					value = field.get(entity);
				}
				
				if(value!=null){
				    map.put(columnName, value);
				}
			}
			
			return map;
		} catch (Exception e) {
			throw new GenericDaoException(entityClass, "get field error", e);
		}
		
	}
	
	public class BeanMapper implements RowMapper<T> {
		@SuppressWarnings("unchecked")
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResultSetMetaData metaData = rs.getMetaData();
			int count = metaData.getColumnCount();
			Object entity = null;
			try {
				entity = entityClass.newInstance();
				for(int i=1; i<=count;i++){
					String columnName = metaData.getColumnLabel(i);
					Method method = columnNameToMethod.get(columnName.toLowerCase());
					if(method!=null){
						Class<?> clazz = method.getParameterTypes()[0];
						if(clazz.isArray()){
							method.invoke(entity, JsonUtil.parserJsonArray(rs.getString(columnName), clazz.getComponentType()));
//						} else if(clazz.isAssignableFrom(List.class)){
//					        method.invoke(entity, valueToList(rs.getString(columnName)));
						}else{
							Object value = rs.getObject(columnName);
							if(value!=null){
								if(value.getClass()==Integer.class&&clazz == Boolean.class){
									method.invoke(entity, ((Integer)value).intValue() != 0);
								}else{
						            method.invoke(entity, value);
								}
							}
						}
					}else{
						Field field = columnNameToField.get(columnName.toLowerCase());
						if(field!=null){
							field.setAccessible(true);
							Object value = rs.getObject(columnName);
							if(value != null){
								if(field.getType().isArray()){
									field.set(entity, JsonUtil.parserJsonArray(rs.getString(columnName), field.getType().getComponentType()));
								} else{
									if (value.getClass() == BigInteger.class) {
                                        long longValue = ((BigInteger) value).longValue();
                                        if(field.getType() == Boolean.class){
                                            field.set(entity, longValue != 0);
                                        } else if(field.getType() == Long.class) {
                                            field.set(entity, longValue);
                                        } else if(field.getType() == Byte.class) {
                                            field.set(entity, (byte)longValue);
                                        } else field.set(entity, longValue);
                                    } else if(value.getClass() == Integer.class){
										int intValue = ((Integer)value).intValue();
										if(field.getType() == Boolean.class){
											field.set(entity, intValue != 0);
										} else if(field.getType() == Long.class) {
											field.set(entity, (long)intValue);
										} else if(field.getType() == Byte.class) {
											field.set(entity, (byte)intValue);
										} else field.set(entity, intValue);
									} else {
										field.set(entity, value);
									}					
								}
							}
								
						}
					}		
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return (T) entity;
		}
	}

	private boolean containsProperty(List<Expression> expressions, String property) {
		if (expressions == null || property == null) return false;
		for (Expression exp : expressions) {
			if (property.equals(exp.property)) return true;
		}
		return false;
	}
	
	public static class Expression { 
		String property;
		String operator;
		Object value;
		public Expression(String property, String operator, Object value) {
			this.property = property;
			this.operator = operator;
			this.value = value;
		}
	}
}
