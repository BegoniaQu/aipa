package com.aipa.svc.common.vo;

import java.util.List;

public class CollectedNoteBean {
	
	private Long id; //记录ID
	private Long note_id;
	private String title;
	private String content; 	//内容
	private List<String> pictures;
	private String category_name;
	private String create_time;
	
	public Long getNote_id() {
		return note_id;
	}
	public void setNote_id(Long note_id) {
		this.note_id = note_id;
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
	public List<String> getPictures() {
		return pictures;
	}
	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
