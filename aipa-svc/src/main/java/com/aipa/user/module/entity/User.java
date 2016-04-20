package com.aipa.user.module.entity;

import java.io.Serializable;
import java.util.Date;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.rao.annotation.RedisField;
import com.qy.data.common.dao.annotation.GeneratedValue;
import com.qy.data.common.dao.annotation.Id;
import com.qy.data.common.dao.annotation.Table;
import com.qy.data.common.rao.annotation.RedisId;

@Table(name="platform_user")
public class User implements Serializable{

	private static final long serialVersionUID = 4452003585277082806L;

	@Id
	@GeneratedValue
	@RedisId
	private Long id;
	
	@Column
	@RedisField
	private Long version;
	
	@Column
	@RedisField
	private Date create_time;
	
	@Column
	@RedisField
	private String email;
	
	@Column
	@RedisField
	private Integer enable;
	
	@Column
	@RedisField
	private String head_picture;
	
	@Column
	@RedisField
	private String password;
	
	@Column
	@RedisField
	private String realname;
	
	@Column
	@RedisField
	private Long role_id;
	
	@Column(inUniqueKey = true)
	@RedisField(inUniqueKey = true)
	private String username;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public String getHead_picture() {
		return head_picture;
	}
	public void setHead_picture(String head_picture) {
		this.head_picture = head_picture;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public Long getRole_id() {
		return role_id;
	}
	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
