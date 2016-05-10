package com.aipa.svc.common.vo;



public class UserInfoBean {

	private String nickname = "";
	private String head_picture = "";
	private int sex;
	private Boolean sex_switch;
	private String sex_str = "";
	private int age;
	private Boolean age_switch; 	//true:公开，false:关闭
	private int sex_orient;  		//性取向：1-好男，2-好女，3-双杀，4-无性恋
	private Boolean sex_orient_switch;
	private String sex_orient_str = "";
	private int marital_status;  //婚姻情况：1-未婚，2-已婚
	private Boolean marital_status_switch;
	private String marital_status_str = "";
	private String location = "";  		//所在地
	
	
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHead_picture() {
		return head_picture;
	}
	public void setHead_picture(String head_picture) {
		this.head_picture = head_picture;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public Boolean getSex_switch() {
		return sex_switch;
	}
	public void setSex_switch(Boolean sex_switch) {
		this.sex_switch = sex_switch;
	}
	public String getSex_str() {
		return sex_str;
	}
	public void setSex_str(String sex_str) {
		this.sex_str = sex_str;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Boolean getAge_switch() {
		return age_switch;
	}
	public void setAge_switch(Boolean age_switch) {
		this.age_switch = age_switch;
	}
	public int getSex_orient() {
		return sex_orient;
	}
	public void setSex_orient(int sex_orient) {
		this.sex_orient = sex_orient;
	}
	public Boolean getSex_orient_switch() {
		return sex_orient_switch;
	}
	public void setSex_orient_switch(Boolean sex_orient_switch) {
		this.sex_orient_switch = sex_orient_switch;
	}
	public String getSex_orient_str() {
		return sex_orient_str;
	}
	public void setSex_orient_str(String sex_orient_str) {
		this.sex_orient_str = sex_orient_str;
	}
	public int getMarital_status() {
		return marital_status;
	}
	public void setMarital_status(int marital_status) {
		this.marital_status = marital_status;
	}
	public Boolean getMarital_status_switch() {
		return marital_status_switch;
	}
	public void setMarital_status_switch(Boolean marital_status_switch) {
		this.marital_status_switch = marital_status_switch;
	}
	public String getMarital_status_str() {
		return marital_status_str;
	}
	public void setMarital_status_str(String marital_status_str) {
		this.marital_status_str = marital_status_str;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
}
