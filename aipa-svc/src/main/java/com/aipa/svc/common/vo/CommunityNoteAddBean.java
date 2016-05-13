package com.aipa.svc.common.vo;

import java.util.List;


public class CommunityNoteAddBean {

	private String title; 		//标题
	private String content; 	//内容
	private List<String> pic_list;
	private Long user_id; 		//发布人ID
	private Long category_id; // 种类ID

	
	
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

	public List<String> getPic_list() {
		return pic_list;
	}

	public void setPic_list(List<String> pic_list) {
		this.pic_list = pic_list;
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
