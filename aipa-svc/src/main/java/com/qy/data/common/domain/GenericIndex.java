package com.qy.data.common.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 所有index的domain对象必须实现GenericIndex
 * @author qy
 *
 */
public interface GenericIndex extends Serializable{
	
	public Long getScore();
	
	public void setScore(Long score);
	
	public Date getCreateTime();
	
	public void setCreateTime(Date createTime);
	
	/**
	 * 给redis的hash分片使用
	 * @return
	 */
	public Object getHashKey();
	
	/**
	 * 与hashkey一起组成redis key
	 * @return
	 */
	public Object getKeyType();
	
	/**
	 * 判断index是否是同一个。只要判断value是否相当
	 * @return
	 */
	public boolean equalIndex(GenericIndex index);
	
	/**
	 * 查询dao读取恢复index的条件
	 * 注：当该数组中所有字符串所表示的字段的值相等的索引会被恢复到同一个hash中，
	 * 一般来说为getHashKey与getKeyType会使用的字段..
	 * @return
	 */
	public String[] getConditionsOfQueryAll();
	
	/**
	 * 表示唯一index的联合主键
	 * @return
	 */
	public String[] getConditionsOfQueryUniqueRecord();
}
