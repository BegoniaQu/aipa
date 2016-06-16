package com.aipa.user.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.user.module.dao.UserNoteCollectIndexDao;
import com.aipa.user.module.entity.UserNoteCollectIndex;
import com.aipa.user.module.rao.UserNoteCollectIndexRao;
import com.aipa.user.module.service.UserNoteCollectIndexService;
import com.aipa.user.module.utils.UserScoreUtils;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.rao.GenericIndexRao;
import com.qy.data.common.service.impl.AbstractGenericIndexServiceImpl;

@Service
public class UserNoteCollectIndexServiceImpl extends AbstractGenericIndexServiceImpl<UserNoteCollectIndex> implements UserNoteCollectIndexService{

	
	@Resource
	private UserNoteCollectIndexDao userNoteCollectIndexDao;
	
	private UserNoteCollectIndexRao userNoteCollectIndexRao = null;
	
	@Override
	protected GenericDao<UserNoteCollectIndex, Object> getDao() {
		return userNoteCollectIndexDao;
	}

	@Override
	protected GenericIndexRao<UserNoteCollectIndex> getRao() {
		return userNoteCollectIndexRao;
	}

	@Override
	protected void fillScore(UserNoteCollectIndex t) {
		UserScoreUtils.fillScore(t);
	}

}
