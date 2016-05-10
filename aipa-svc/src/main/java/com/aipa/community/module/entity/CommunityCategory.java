package com.aipa.community.module.entity;

import java.io.Serializable;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.dao.annotation.GeneratedValue;
import com.qy.data.common.dao.annotation.Id;
import com.qy.data.common.dao.annotation.Table;
import com.qy.data.common.rao.annotation.RedisField;
import com.qy.data.common.rao.annotation.RedisId;

/**
 * 爱啪社区分类实体
 * @author qy
 *
 */
@Table(name="tb_community_category")
public class CommunityCategory implements Serializable{

	private static final long serialVersionUID = -7208634932738639932L;
	
	@Id
	@GeneratedValue
	@RedisId
	private Long id;
	
	@Column
	@RedisField
	private String name;   		//分类名称
	
	@Column
	@RedisField
	private String descr;  		//描述
	
	@Column
	@RedisField
	private Long parent_id;  	//父分类ID
	
	
	@Column
	@RedisField
	private int user_interest_cnt; //用户针对的子分类的关注数量
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Long getParent_id() {
		return parent_id;
	}
	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}
	public int getUser_interest_cnt() {
		return user_interest_cnt;
	}
	public void setUser_interest_cnt(int user_interest_cnt) {
		this.user_interest_cnt = user_interest_cnt;
	}
}
