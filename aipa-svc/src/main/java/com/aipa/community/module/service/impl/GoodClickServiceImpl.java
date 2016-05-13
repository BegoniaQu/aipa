package com.aipa.community.module.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aipa.community.module.dao.GoodClickDao;
import com.aipa.community.module.entity.GoodClick;
import com.aipa.community.module.rao.GoodClickRao;
import com.aipa.community.module.service.GoodClickService;
import com.qy.data.common.dao.GenericDao;
import com.qy.data.common.rao.GenericInfoRao;
import com.qy.data.common.service.impl.AbstractGenericInfoServiceImpl;

@Service
public class GoodClickServiceImpl extends AbstractGenericInfoServiceImpl<GoodClick, Long> implements GoodClickService{

	@Resource
	private GoodClickDao goodClickDao;
	
	private GoodClickRao goodClickRao = null;
	
	@Override
	public void fill(GoodClick t) {
		
	}

	@Override
	protected GenericDao<GoodClick, Long> getDao() {
		return goodClickDao;
	}

	@Override
	protected GenericInfoRao<GoodClick, Long> getRao() {
		return goodClickRao;
	}

}
