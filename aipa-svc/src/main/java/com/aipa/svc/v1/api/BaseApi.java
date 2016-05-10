package com.aipa.svc.v1.api;

import java.util.Date;

import com.aipa.svc.common.util.AipaTokenUtil;
import com.aipa.user.module.entity.User;
import com.aipa.user.module.service.UserService;

public abstract class BaseApi {

	
	/**
	 * 检查token是否过期、以及用户是否有效
	 * @param uid
	 * @return
	 */
	protected boolean checkTokenExpired(Long uid){
		User user = getUserService().findById(uid);
		long current = new Date().getTime();
		long latestLoginDate = user.getLatest_login_time().getTime();
		if((current - latestLoginDate) > AipaTokenUtil.expired){
			return true;
		}
		if(!user.isEnabled()||user.isDeleted()){
			return true;
		}
		return false;
	}
	
	
	public abstract UserService getUserService();
}



