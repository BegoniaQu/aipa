package com.aipa.svc.v1.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.aipa.user.module.entity.User;
import com.aipa.user.module.service.UserService;

@Component
public class UserManager {

	@Resource
	private UserService userService;
	
	/**
	 * 
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
				return "token";
			}
		}
		return null;
	}
}
