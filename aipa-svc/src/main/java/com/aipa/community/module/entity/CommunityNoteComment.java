package com.aipa.community.module.entity;

import java.io.Serializable;
import java.util.Date;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.dao.annotation.GeneratedValue;
import com.qy.data.common.dao.annotation.Id;
import com.qy.data.common.dao.annotation.Table;
import com.qy.data.common.rao.annotation.RedisField;
import com.qy.data.common.rao.annotation.RedisId;

@Table(name="tb_community_note_comment")
public class CommunityNoteComment implements Serializable{

	private static final long serialVersionUID = 1961561803718744074L;

	@Id
	@RedisId
	@GeneratedValue
	private Long id;
	
	@Column
	@RedisField
	private Long note_id; //帖子ID
	
	@Column
	@RedisField
	private Long root_comment_id; //评论根ID
	
	@Column
	@RedisField
	private Long commented_id;  //被回复的记录ID
	
	@Column
	@RedisField
	private String content;  //评论内容
	
	@Column
	@RedisField
	private Long content_user_id; //评论人ID
	
	@Column
	@RedisField
	private Date create_time = new Date(); //评论时间
	
	@Column
	@RedisField
	private Boolean deleted = false; //是否删除
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNote_id() {
		return note_id;
	}

	public void setNote_id(Long note_id) {
		this.note_id = note_id;
	}

	public Long getRoot_comment_id() {
		return root_comment_id;
	}

	public void setRoot_comment_id(Long root_comment_id) {
		this.root_comment_id = root_comment_id;
	}

	public Long getCommented_id() {
		return commented_id;
	}

	public void setCommented_id(Long commented_id) {
		this.commented_id = commented_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getContent_user_id() {
		return content_user_id;
	}

	public void setContent_user_id(Long content_user_id) {
		this.content_user_id = content_user_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
}
