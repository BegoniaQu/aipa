package com.aipa.user.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.user.module.dao.UserDao;
import com.aipa.user.module.entity.User;
import com.aipa.user.module.rao.UserRao;
import com.aipa.user.module.service.UserService;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.proxy.rao.StringCacheRao;
import com.qy.data.common.rao.GenericInfoRao;
import com.qy.data.common.service.impl.AbstractGenericInfoServiceImpl;

@Service
public class UserServiceImpl extends AbstractGenericInfoServiceImpl<User, Long> implements UserService{

	@Resource
	private UserDao userDao;
	
	private UserRao userRao = null;
	
	private StringCacheRao stringCacheRao = null; //TODO，添加缓存后，根据账户查用户，用这种缓存
	
	@Override
	public void fill(User t) {}

	@Override
	protected GenericDao<User, Long> getDao() {
		return userDao;
	}

	@Override
	protected GenericInfoRao<User, Long> getRao() {
		return userRao;
	}

	@Override
	public void addWithUniqueKey(User user) {
		
		
	}

	
	
}
