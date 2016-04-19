package com.qy.data.common.service.impl;

import com.qy.data.common.service.ExpireService;
import com.qy.data.common.rao.ExpireRao;

/**
 * @author tianhui
 *
 */
public class ExpireServiceImpl implements ExpireService {

	private ExpireRao expireRao;
	
	@Override
	public Boolean add(String stringKey, int expireTime) {
		if(0 ==  expireTime){
			return false;
		}
		
		return expireRao.add(stringKey, expireTime);
	}

	public void setExpireRao(ExpireRao expireRao) {
		this.expireRao = expireRao;
	}

	
}
