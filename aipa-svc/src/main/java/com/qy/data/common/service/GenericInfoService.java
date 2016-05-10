package com.qy.data.common.service;

import java.util.List;

import com.qy.data.common.dao.impl.AbstractGenericDaoImpl.Expression;

/**
 * info的service都实现这个接口
 * @author tianhui
 *
 * @param <T>
 */
public interface GenericInfoService <T, PK>{

	public void resyncRedis(PK pk);

	public void add (T t);
	
	public boolean add(T t, boolean updateOnExist);
	
	public void update (T t);
	
	public void update(T t, String... properties);
	
	public void del(PK pk);
	
	public void del(PK pk, Object partitionHint);
	
	public void delUnique(T entity);

	public T findById(PK pk);

	public T findById(PK pk, Object partitionHint);
	
	public T findByUniqueKey(T entity);
	/**
	 * 更具key和field找value。
	 * 如果不存在key，返回null。
	 * 如果不存在value，返回空串
	 * @param pk
	 * @param field
	 * @return
	 */
	public String findFieldValue(PK pk, String field);

	public String findFieldValue(PK pk, String field, Object partitionHint);
	
	/**
	 * 按条件查找（因为直接访问DAO，推荐仅供管理后台使用，conditions为参与where子句的字段，条件中指定的每一个字段会与entity参数中的对应字段
	 * 比较，相等时则返回；当相等无法满足需求时（模糊查找，数字比较，包含关系等），可以指定具体的Expression(property, operation, value),
	 * 语句"property operation value"会作为where子句条件之一参与查询；
	 * @param entity
	 * @param pageno
	 * @param pagesize
	 * @param asc
	 * @param expressionConditions
	 * @param conditions
	 * @return
	 */
	public List<T> find(T entity, int pageno, int pagesize, int asc, List<Expression> expressionConditions, String... conditions);
	/**
	 * 按条件记数，使用方法同find
	 * @see GenericInfoService#find(Object, int, int, int, List, String...)
	 */
	public int count(T entity, List<Expression> expressionConditions, String... conditions);
	
	public void fill(T t);
	
	
	public void increment(T entity,String prop);
}
