package com.qy.data.common.dao;

import java.io.Serializable;
import java.util.List;

import com.qy.data.common.dao.impl.AbstractGenericDaoImpl;

/**
 * @author qy
 *
 * @param <T>
 * @param <PK>
 */
public interface GenericDao<T extends Serializable, PK> {

	/**
	 * 保存entity对象，如果属性为null，忽略此属性。
	 * 如果是自增，会填充
	 * @param entity
	 * @return
	 */
	boolean save(T entity);

	/**
	 * 保存entity对象，如果属性为null，忽略此属性。
	 * 如果是自增，会填充
	 * updateOnExist设置当key不存在的处理。true表示存在，更新不为null的value。false表示igore更新
	 * @param entity
	 * @param updateOnExist 当已存在时是否覆盖
	 * @return
	 */
	boolean save(T entity, boolean updateOnExist);
	
	/**
	 * 根据主键ID或唯一键更新全量，主键时如果field为null，也修改。
	 * @param entity
	 * @return
	 */
	boolean update(T entity) ;

	/**
	 * 必须有唯一键。根据唯一键键去更新全量，如果field为null，也修改
	 * @param entity
	 * @return
	 */
	boolean updateUnique(T entity);
	
	/**
	 * 必须有主键ID。根据主键去更新相应属性，如果field为null，也修改。
	 * @param entity
	 * @param properties domain的field name
	 * @return
	 */
	boolean update(T entity, String... fields) ;

	/**
	 * 必须有主键ID。根据主键去更新相应属性，如果field为null，也修改。
	 * 当skipMissingProperty为true时，忽略db不存在的字段
	 * @param entity
	 * @param properties domain的field name
	 * @return
	 */
	boolean update(T entity, boolean skipMissingProperty, String... properties);

	/**
	 * 必须有唯一键。根据唯一键去更新相应属性，如果field为null，也修改。
	 * 当skipMissingProperty为true时，忽略db不存在的字段
	 * @param entity
	 * @param properties domain的field name
	 * @return
	 */
	boolean updateUnique(T entity, boolean skipMissingProperty, String... properties);
	
	/**
	 * 如果field为null，也修改。
	 * @param entity
	 * @param conditions domain的field name
	 * @return
	 */
	boolean updateByCondition(T entity, List<AbstractGenericDaoImpl.Expression> expressionConditions, String... conditions) ;
	
	/**
	 * 真删除，别乱用。软删除调用update方法
	 * @param id
	 */
	boolean delete(PK id);

	/**
	 * 真删除，别乱用。软删除调用update方法
	 * @param id
	 */
	boolean delete(PK id, Object partitionHint);
	
	/**
	 * 真删除，别乱用。软删除调用update方法
	 * @param id
	 */
	boolean deleteUnique(T entity);
	
	/**
	 * 真删除，别乱用。软删除调用update方法
	 * 从fields中找entity的值，作为条件批量删除
	 * 如果fields对应的值为null，做is null判断
	 * @return
	 */
	boolean delete(T entity, List<AbstractGenericDaoImpl.Expression> expressionConditions, String... conditions) ;
	
//	T query(PK id, String partitionId);
	
	//List<T> query(T entity) ;
	

	/**
	 * 对于分表的数据库，PK必须是分表的建。
	 * @param id
	 * @return
	 */
	T query(PK id);

	/**
	 * 对于分表的数据库，hint为分表字段的值。
	 * @param id
	 * @return
	 */
	T query(PK id, Object partitionHint);
	
	T queryUnique(T entity);
	
    /**
     * @param conditions
     * @return
     */
    List<T> query(T entity, List<AbstractGenericDaoImpl.Expression> expressionConditions, String... conditions);
    
    //List<T> query(T entity, int pageno, int pagesize, int asc, String... conditions);
    
    List<T> query(T entity, int pageno, int pagesize, int asc, List<AbstractGenericDaoImpl.Expression> expressionConditions, String... conditions);
    
    /**
     * @param conditions
     * @return
     */
    int count(T entity, List<AbstractGenericDaoImpl.Expression> expressionConditions, String... conditions);
}
