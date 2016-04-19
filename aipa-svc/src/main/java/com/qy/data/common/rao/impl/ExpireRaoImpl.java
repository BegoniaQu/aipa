package com.qy.data.common.rao.impl;

import com.qy.data.common.constant.DataCommonConstant;
import com.qy.data.common.proxy.rao.StringCacheRao;
import com.qy.data.common.rao.ExpireRao;

/**
 * @author tianhui
 *
 */
public class ExpireRaoImpl implements ExpireRao {
	private StringCacheRao stringCacheRao;
	
	private String keyPrefix = "exp:";
	
	/**
	 * @return 返回： 原先是否存在该key --- 失败的尝试也会更新计时器
	 */
	@Override
	public Boolean add(String stringKey, int expireTime) {
		Boolean exist = stringCacheRao.exist(stringKey, getType());
		
		stringCacheRao.set(stringKey, getType(), DataCommonConstant.RAO_DEFAULT_FIELD_KEY, expireTime);
		
		return exist;
	}

	@Override
	public boolean exist(String stringKey) {
		return stringCacheRao.exist(stringKey, getType());
	}
	
	private String getType(){
		return keyPrefix;
	}

	public void setStringCacheRao(StringCacheRao stringCacheRao) {
		this.stringCacheRao = stringCacheRao;
	}

}
