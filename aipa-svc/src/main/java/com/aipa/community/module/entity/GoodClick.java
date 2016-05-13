package com.aipa.community.module.entity;

import java.io.Serializable;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.dao.annotation.GeneratedValue;
import com.qy.data.common.dao.annotation.Id;
import com.qy.data.common.dao.annotation.Table;
import com.qy.data.common.rao.annotation.RedisField;
import com.qy.data.common.rao.annotation.RedisId;

@Table(name="tb_good_click")
public class GoodClick implements Serializable{

	private static final long serialVersionUID = 3774067104863658764L;
	
	@Id
	@RedisId
	@GeneratedValue
	private Long id;
	
	@Column
	@RedisField
	private Long user_id;  		//用户ID
	
	@Column
	@RedisField
	private Long object_id;  		//点赞对象ID
	
	@Column
	@RedisField
	private Integer good_type;  //针对：1-帖子，2-评论 --当有索引表后，此字段其实没什么用了
	
	
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
	public Long getObject_id() {
		return object_id;
	}
	public void setObject_id(Long object_id) {
		this.object_id = object_id;
	}
	public Integer getGood_type() {
		return good_type;
	}
	public void setGood_type(Integer good_type) {
		this.good_type = good_type;
	}
}

