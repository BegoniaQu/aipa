package com.aipa.user.module.entity;

import java.io.Serializable;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.dao.annotation.GeneratedValue;
import com.qy.data.common.dao.annotation.Id;
import com.qy.data.common.dao.annotation.Table;
import com.qy.data.common.rao.annotation.RedisField;
import com.qy.data.common.rao.annotation.RedisId;

/**
 * 用户关注社区分类实体
 * @author qy
 *
 */
@Table(name="tb_user_category_interest")
public class UserCategoryInterest implements Serializable{
	
	private static final long serialVersionUID = 740350122371560770L;

	@Id
	@GeneratedValue
	@RedisId
	private Long id;
	
	@Column
	@RedisField
	private Long user_id; //用户ID
	
	@Column
	@RedisField
	private Long category_id; // 种类ID

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Long category_id) {
		this.category_id = category_id;
	}
	
}
