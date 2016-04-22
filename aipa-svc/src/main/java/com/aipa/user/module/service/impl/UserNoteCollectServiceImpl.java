package com.aipa.user.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.user.module.dao.UserNoteCollectDao;
import com.aipa.user.module.entity.UserNoteCollect;
import com.aipa.user.module.rao.UserNoteCollectRao;
import com.aipa.user.module.service.UserNoteCollectService;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.rao.GenericInfoRao;
import com.qy.data.common.service.impl.AbstractGenericInfoServiceImpl;

@Service
public class UserNoteCollectServiceImpl extends AbstractGenericInfoServiceImpl<UserNoteCollect, Long> implements UserNoteCollectService{

	@Resource
	private UserNoteCollectDao userNoteCollectDao;
	
	private UserNoteCollectRao userNoteCollectRao = null;
	
	@Override
	public void fill(UserNoteCollect t) {}

	@Override
	protected GenericDao<UserNoteCollect, Long> getDao() {
		return this.userNoteCollectDao;
	}

	@Override
	protected GenericInfoRao<UserNoteCollect, Long> getRao() {
		return this.userNoteCollectRao;
	}

}
