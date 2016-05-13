package com.aipa.community.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.community.module.dao.CommunityNoteCommentDao;
import com.aipa.community.module.entity.CommunityNoteComment;
import com.aipa.community.module.rao.CommunityNoteCommentRao;
import com.aipa.community.module.service.CommunityNoteCommentService;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.rao.GenericInfoRao;
import com.qy.data.common.service.impl.AbstractGenericInfoServiceImpl;

@Service
public class CommunityNoteCommentServiceImpl extends AbstractGenericInfoServiceImpl<CommunityNoteComment, Long> implements CommunityNoteCommentService{

	@Resource
	private CommunityNoteCommentDao noteCommentDao;
	
	private CommunityNoteCommentRao noteCommentRao = null;
	
	@Override
	public void fill(CommunityNoteComment t) {
	}

	@Override
	protected GenericDao<CommunityNoteComment, Long> getDao() {
		return noteCommentDao;
	}

	@Override
	protected GenericInfoRao<CommunityNoteComment, Long> getRao() {
		return noteCommentRao;
	}

}
