package com.aipa.svc.common.vo;

import java.util.ArrayList;
import java.util.List;

public class CommunityNoteBean {

	private Long note_user_id;
	private String note_user_headpic;
	private String user_nickname;
	private int sex; //1-男，2-女
	private String location;
	
	private Long note_id;
	private String title;
	private String content; 	//内容
	private boolean isPart = false; //是否显示部分内容，如果为true，则后台会截取一部分的内容返回给前端，前端在后面加上省略号
	private List<String> pictures = new ArrayList<>();
	private String time;
	
	private int shoucang_cnt; //收藏数
	private int pinglun_cnt;  //评论数
	private int scan_cnt;     //浏览数
	
	
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
	public int getShoucang_cnt() {
		return shoucang_cnt;
	}
	public void setShoucang_cnt(int shoucang_cnt) {
		this.shoucang_cnt = shoucang_cnt;
	}
	public int getPinglun_cnt() {
		return pinglun_cnt;
	}
	public void setPinglun_cnt(int pinglun_cnt) {
		this.pinglun_cnt = pinglun_cnt;
	}
	public int getScan_cnt() {
		return scan_cnt;
	}
	public void setScan_cnt(int scan_cnt) {
		this.scan_cnt = scan_cnt;
	}
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isPart() {
		return isPart;
	}
	public void setPart(boolean isPart) {
		this.isPart = isPart;
	}
	
}
