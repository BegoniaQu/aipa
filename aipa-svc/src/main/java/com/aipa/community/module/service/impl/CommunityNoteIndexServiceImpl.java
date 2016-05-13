package com.aipa.community.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.community.module.dao.CommunityNoteIndexDao;
import com.aipa.community.module.entity.CommunityNoteIndex;
import com.aipa.community.module.rao.CommunityNoteIndexRao;
import com.aipa.community.module.service.CommunityNoteIndexService;
import com.aipa.community.module.utils.CommunityScoreUtils;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.rao.GenericIndexRao;
import com.qy.data.common.service.impl.AbstractGenericIndexServiceImpl;

@Service
public class CommunityNoteIndexServiceImpl extends AbstractGenericIndexServiceImpl<CommunityNoteIndex> implements CommunityNoteIndexService{

	@Resource
	private CommunityNoteIndexDao communityNoteIndexDao;
	
	private CommunityNoteIndexRao communityNoteIndexRao = null;
	
	@Override
	protected GenericDao<CommunityNoteIndex, Object> getDao() {
		return communityNoteIndexDao;
	}

	@Override
	protected GenericIndexRao<CommunityNoteIndex> getRao() {
		return communityNoteIndexRao;
	}

	@Override
	protected void fillScore(CommunityNoteIndex t) {
		CommunityScoreUtils.fillScore(t);
	}

	
}
