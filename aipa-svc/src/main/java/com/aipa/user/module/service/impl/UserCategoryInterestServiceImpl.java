package com.aipa.user.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.user.module.dao.UserCategoryInterestDao;
import com.aipa.user.module.entity.UserCategoryInterest;
import com.aipa.user.module.rao.UserCategoryInterestRao;
import com.aipa.user.module.service.UserCategoryInterestService;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.rao.GenericInfoRao;
import com.qy.data.common.service.impl.AbstractGenericInfoServiceImpl;

@Service
public class UserCategoryInterestServiceImpl extends AbstractGenericInfoServiceImpl<UserCategoryInterest, Long> implements UserCategoryInterestService{

	@Resource
	private UserCategoryInterestDao userCategoryInterestDao;
	
	private UserCategoryInterestRao userCategoryInterestRao = null;
	
	@Override
	public void fill(UserCategoryInterest t) {}

	@Override
	protected GenericDao<UserCategoryInterest, Long> getDao() {
		return userCategoryInterestDao;
	}

	@Override
	protected GenericInfoRao<UserCategoryInterest, Long> getRao() {
		return userCategoryInterestRao;
	}

	
	
}
