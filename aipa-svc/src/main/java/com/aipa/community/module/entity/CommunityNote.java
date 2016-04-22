package com.aipa.community.module.entity;

import java.io.Serializable;
import java.util.Date;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.dao.annotation.GeneratedValue;
import com.qy.data.common.dao.annotation.Id;
import com.qy.data.common.dao.annotation.Table;
import com.qy.data.common.rao.annotation.RedisField;
import com.qy.data.common.rao.annotation.RedisId;

@Table(name="tb_community_note")
public class CommunityNote implements Serializable{

	private static final long serialVersionUID = 6230844702017109538L;

	@Id
	@GeneratedValue
	@RedisId
	private Long id;
	
	@Column
	@RedisField
	private String title; 		//标题
	
	@Column
	@RedisField
	private String content; 	//内容
	
	@Column
	@RedisField
	private String pictures;	 // 图片,逗号分隔
	
	@Column
	@RedisField
	private Long user_id; 		//发布人ID
	
	@Column
	@RedisField
	private Date create_time; 	//发布时间
	
	@Column
	@RedisField
	private Long category_id; // 种类ID
	
	@Column
	@RedisField
	private int scan_count; 	//浏览人数
	
	@Column
	@RedisField
	private int comment_count; //评论数
	
	@Column
	@RedisField
	private int collect_count; //收藏数
	
	@Column
	@RedisField
	private int good_count;    //点赞数
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Long category_id) {
		this.category_id = category_id;
	}

	public int getScan_count() {
		return scan_count;
	}

	public void setScan_count(int scan_count) {
		this.scan_count = scan_count;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public int getCollect_count() {
		return collect_count;
	}

	public void setCollect_count(int collect_count) {
		this.collect_count = collect_count;
	}

	public int getGood_count() {
		return good_count;
	}

	public void setGood_count(int good_count) {
		this.good_count = good_count;
	}
	
}
