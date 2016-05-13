package com.aipa.community.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.community.module.dao.CommunityNoteCounterDao;
import com.aipa.community.module.entity.CommunityNoteCounter;
import com.aipa.community.module.rao.CommunityNoteCounterRao;
import com.aipa.community.module.service.CommunityNoteCounterService;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.rao.GenericInfoRao;
import com.qy.data.common.service.impl.AbstractGenericInfoServiceImpl;

@Service
public class CommunityNoteCounterServiceImpl extends AbstractGenericInfoServiceImpl<CommunityNoteCounter, Long> implements CommunityNoteCounterService{

	@Resource
	private CommunityNoteCounterDao noteCounterDao;
	
	private CommunityNoteCounterRao noteCounterRao = null;
	
	@Override
	public void fill(CommunityNoteCounter t) {
		
	}

	@Override
	protected GenericDao<CommunityNoteCounter, Long> getDao() {
		return noteCounterDao;
	}

	@Override
	protected GenericInfoRao<CommunityNoteCounter, Long> getRao() {
		return noteCounterRao;
	}

}
