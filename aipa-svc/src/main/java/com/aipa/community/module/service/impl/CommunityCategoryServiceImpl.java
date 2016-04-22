package com.aipa.community.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.community.module.dao.CommunityCategoryDao;
import com.aipa.community.module.entity.CommunityCategory;
import com.aipa.community.module.rao.CommunityCategoryRao;
import com.aipa.community.module.service.CommunityCategoryService;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.rao.GenericInfoRao;
import com.qy.data.common.service.impl.AbstractGenericInfoServiceImpl;

@Service
public class CommunityCategoryServiceImpl extends AbstractGenericInfoServiceImpl<CommunityCategory, Long> implements CommunityCategoryService{

	@Resource
	private CommunityCategoryDao communityCategoryDao;
	
	private CommunityCategoryRao communityCategoryRao = null;
	
	@Override
	public void fill(CommunityCategory t) {}

	@Override
	protected GenericDao<CommunityCategory, Long> getDao() {
		
		return communityCategoryDao;
	}

	@Override
	protected GenericInfoRao<CommunityCategory, Long> getRao() {
		
		return communityCategoryRao;
	}

	
}
