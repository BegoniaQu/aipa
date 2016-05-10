package com.aipa.user.module.entity;

import java.io.Serializable;
import java.util.Date;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.dao.annotation.GeneratedValue;
import com.qy.data.common.dao.annotation.Id;
import com.qy.data.common.dao.annotation.Table;
import com.qy.data.common.rao.annotation.RedisField;
import com.qy.data.common.rao.annotation.RedisId;

/**
 * 用户帖子收藏实体
 * @author qy
 *
 */
@Table(name="tb_user_note_collect")
public class UserNoteCollect implements Serializable{
	
	private static final long serialVersionUID = 5528247123914610121L;

	@Id
	@GeneratedValue
	@RedisId
	private Long id;
	
	@Column
	@RedisField
	private Long user_id; //用户ID
	
	@Column
	@RedisField
	private Long note_id; //帖子ID
	
	@Column
	@RedisField
	private Long category_id; //帖子种类（根据种类找到帖子在哪张帖子表中）
	
	@Column
	@RedisField
	private Date create_time = new Date();

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

	public Long getNote_id() {
		return note_id;
	}

	public void setNote_id(Long note_id) {
		this.note_id = note_id;
	}

	public Long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Long category_id) {
		this.category_id = category_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}
