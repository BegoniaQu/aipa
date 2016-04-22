package com.aipa.community.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.community.module.dao.CommunityNoteDao;
import com.aipa.community.module.entity.CommunityNote;
import com.aipa.community.module.rao.CommunityNoteRao;
import com.aipa.community.module.service.CommunityNoteService;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.rao.GenericInfoRao;
import com.qy.data.common.service.impl.AbstractGenericInfoServiceImpl;

@Service
public class CommunityNoteServiceImpl extends AbstractGenericInfoServiceImpl<CommunityNote, Long> implements CommunityNoteService{

	@Resource
	private CommunityNoteDao communityNoteDao;
	
	private CommunityNoteRao CommunityNoteRao = null;
	
	@Override
	public void fill(CommunityNote t) {}

	@Override
	protected GenericDao<CommunityNote, Long> getDao() {
		return communityNoteDao;
	}

	@Override
	protected GenericInfoRao<CommunityNote, Long> getRao() {
		return CommunityNoteRao;
	}

	
}
