package com.aipa.svc.v1.manager;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.aipa.svc.common.util.AipaTokenUtil;
import com.aipa.user.module.entity.User;
import com.aipa.user.module.service.UserService;

@Component
public class UserManager {

	@Resource
	private UserService userService;
	
	/**
	 * 根据主键获取用户信息
	 * @param uid
	 * @return
	 */
	public User getById(Long uid){
		return this.userService.findById(uid);
	}
	
	
	/**
	 * 登录校验
	 * @param username
	 * @param password
	 * @return
	 */
	public String getToken(String username,String password){
		User user = new User();
		user.setUsername(username);
		User queryEntity = this.userService.findByUniqueKey(user);
		if(queryEntity != null && !queryEntity.isDeleted() && queryEntity.isEnabled()){
			if(queryEntity.getPassword().equals(password)){
				//生成token
				Long uid = queryEntity.getId();
				String token = AipaTokenUtil.getToken(uid);
				user.setLatest_login_time(new Date());
				this.userService.update(user, new String[]{"token,latest_login_time"});
				return token;
			}
		}
		return null;
	}
	
	/**
	 * 根据账号获取账号信息
	 * @param username
	 * @return
	 */
	public User getByUsername(String username){
		User user = new User();
		user.setUsername(username);
		User queryEntity = this.userService.findByUniqueKey(user);
		return queryEntity;
	}
	
	/**
	 * 注册
	 * @param username
	 * @param password
	 */
	public void reg(String username,String password){
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		this.userService.add(user);
	}
	
	public void modify(User user){
		
	}

	public UserService getUserService() {
		return userService;
	}

}
