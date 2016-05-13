package com.aipa.community.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.community.module.dao.GoodClickIndexDao;
import com.aipa.community.module.entity.GoodClickIndex;
import com.aipa.community.module.rao.GoodClickIndexRao;
import com.aipa.community.module.service.GoodClickIndexService;
import com.aipa.community.module.utils.CommunityScoreUtils;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.rao.GenericIndexRao;
import com.qy.data.common.service.impl.AbstractGenericIndexServiceImpl;

@Service
public class GoodClickIndexServiceImpl extends AbstractGenericIndexServiceImpl<GoodClickIndex> implements GoodClickIndexService{

	@Resource
	private GoodClickIndexDao goodClickIndexDao;
	
	private GoodClickIndexRao goodClickIndexRao = null;
	
	@Override
	protected GenericDao<GoodClickIndex, Object> getDao() {
		return goodClickIndexDao;
	}

	@Override
	protected GenericIndexRao<GoodClickIndex> getRao() {
		return goodClickIndexRao;
	}

	@Override
	protected void fillScore(GoodClickIndex t) {
		CommunityScoreUtils.fillScore(t);
	}

	
	
}
