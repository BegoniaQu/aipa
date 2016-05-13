package com.aipa.svc.common.vo;

import java.util.ArrayList;
import java.util.List;

public class CommunityNoteDetailBean {
	private Long note_user_id;
	private String note_user_headpic;
	private String user_nickname;
	private int sex; //1-男，2-女
	private String location;
	
	private Long cat_id;
	private Long note_id;
	private String title;
	private String content; 	//内容
	private List<String> pictures = new ArrayList<>();
	private String time;
	
	private int zan_cnt; //点赞人数
	
	private List<UserBriefInfo> zan_user_list = new ArrayList<>();
	
	
	public Long getNote_user_id() {
		return note_user_id;
	}
	public void setNote_user_id(Long note_user_id) {
		this.note_user_id = note_user_id;
	}
	public String getNote_user_headpic() {
		return note_user_headpic;
	}
	public void setNote_user_headpic(String note_user_headpic) {
		this.note_user_headpic = note_user_headpic;
	}
	public String getUser_nickname() {
		return user_nickname;
	}
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
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
	
	public int getZan_cnt() {
		return zan_cnt;
	}
	public void setZan_cnt(int zan_cnt) {
		this.zan_cnt = zan_cnt;
	}
	
	public Long getCat_id() {
		return cat_id;
	}
	public void setCat_id(Long cat_id) {
		this.cat_id = cat_id;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<UserBriefInfo> getZan_user_list() {
		return zan_user_list;
	}
	public void setZan_user_list(List<UserBriefInfo> zan_user_list) {
		this.zan_user_list = zan_user_list;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
