package com.aipa.user.module.entity;

import java.io.Serializable;
import java.util.Date;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.rao.annotation.RedisField;
import com.qy.data.common.dao.annotation.GeneratedValue;
import com.qy.data.common.dao.annotation.Id;
import com.qy.data.common.dao.annotation.Table;
import com.qy.data.common.rao.annotation.RedisId;

/**
 * 爱啪用户实体
 * @author qy
 *
 */
@Table(name="tb_user")
public class User implements Serializable{

	private static final long serialVersionUID = 4452003585277082806L;

	@Id
	@GeneratedValue
	@RedisId
	private Long id;
	
	@Column(inUniqueKey = true)
	@RedisField(inUniqueKey = true)
	private String username;
	
	@Column
	@RedisField
	private String password;
	
	@Column
	@RedisField
	private String head_picture;
	
	@Column
	@RedisField
	private String nickname;
	
	@Column
	@RedisField
	private Short sex;
	
	@Column
	@RedisField
	private Boolean sex_switch;
	
	@Column
	@RedisField
	private Short age;
	
	@Column
	@RedisField
	private Boolean age_switch; //true:公开，false:关闭
	
	@Column
	@RedisField
	private Short sex_orient;  //性取向：1-好男，2-好女，3-双杀，4-无性恋
	
	@Column
	@RedisField
	private Boolean sex_orient_switch;
	
	@Column
	@RedisField
	private Short marital_status;  //婚姻情况：1-未婚，2-已婚
	
	@Column
	@RedisField
	private Boolean marital_status_switch;
	
	@Column
	@RedisField
	private String location;  //所在地
	
	@Column
	@RedisField
	private Date create_time;
	
	@Column
	@RedisField
	private Date update_time;
	
	@Column
	@RedisField
	private Date latest_login_time; //最近登录时间
	
	@Column
	@RedisField
	private boolean enabled = true;   //0：不可用，1：可用
	
	@Column
	@RedisField
	private boolean deleted = false;   // 0：删除，1：正常

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHead_picture() {
		return head_picture;
	}

	public void setHead_picture(String head_picture) {
		this.head_picture = head_picture;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Short getSex() {
		return sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public Boolean getSex_switch() {
		return sex_switch;
	}

	public void setSex_switch(Boolean sex_switch) {
		this.sex_switch = sex_switch;
	}

	public Short getAge() {
		return age;
	}

	public void setAge(Short age) {
		this.age = age;
	}

	public Boolean getAge_switch() {
		return age_switch;
	}

	public void setAge_switch(Boolean age_switch) {
		this.age_switch = age_switch;
	}

	public Short getSex_orient() {
		return sex_orient;
	}

	public void setSex_orient(Short sex_orient) {
		this.sex_orient = sex_orient;
	}

	public Boolean getSex_orient_switch() {
		return sex_orient_switch;
	}

	public void setSex_orient_switch(Boolean sex_orient_switch) {
		this.sex_orient_switch = sex_orient_switch;
	}

	public Short getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(Short marital_status) {
		this.marital_status = marital_status;
	}

	public Boolean getMarital_status_switch() {
		return marital_status_switch;
	}

	public void setMarital_status_switch(Boolean marital_status_switch) {
		this.marital_status_switch = marital_status_switch;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public Date getLatest_login_time() {
		return latest_login_time;
	}

	public void setLatest_login_time(Date latest_login_time) {
		this.latest_login_time = latest_login_time;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
