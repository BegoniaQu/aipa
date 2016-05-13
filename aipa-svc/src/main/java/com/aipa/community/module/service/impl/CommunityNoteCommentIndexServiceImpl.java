package com.aipa.community.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.community.module.dao.CommunityNoteCommentIndexDao;
import com.aipa.community.module.entity.CommunityNoteCommentIndex;
import com.aipa.community.module.rao.CommunityNoteCommentIndexRao;
import com.aipa.community.module.service.CommunityNoteCommentIndexService;
import com.aipa.community.module.utils.CommunityScoreUtils;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.rao.GenericIndexRao;
import com.qy.data.common.service.impl.AbstractGenericIndexServiceImpl;

@Service
public class CommunityNoteCommentIndexServiceImpl extends AbstractGenericIndexServiceImpl<CommunityNoteCommentIndex> 
implements CommunityNoteCommentIndexService{

	@Resource
	private CommunityNoteCommentIndexDao communityNoteCommentIndexDao;
	
	private CommunityNoteCommentIndexRao communityNoteCommentIndexRao = null;
	
	
	@Override
	protected GenericDao<CommunityNoteCommentIndex, Object> getDao() {
		return communityNoteCommentIndexDao;
	}

	@Override
	protected GenericIndexRao<CommunityNoteCommentIndex> getRao() {
		return communityNoteCommentIndexRao;
	}

	@Override
	protected void fillScore(CommunityNoteCommentIndex t) {
		CommunityScoreUtils.fillScore(t);
	}

	
	
}
