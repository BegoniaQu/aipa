package com.qy.data.common.rao.impl;

import com.qy.data.common.proxy.rao.StringCacheRao;
import com.qy.data.common.rao.MsgCountRao;
import com.qy.data.common.util.StringUtil;

/**
 * @author tianhui
 *
 */
public class MsgCountRaoImpl implements MsgCountRao {
	private StringCacheRao stringCacheRao;
	private String keyPrefix;
	private int second = 3600*24*30;
	
	@Override
	public void incre(String key) {
		stringCacheRao.incre(key, keyPrefix, 1, second);
	}
	
	
	@Override
	public long get(String key) {
		String count = stringCacheRao.get(key, keyPrefix);
		if(StringUtil.isEmpty(count)){
			return 0L;
		} else {
			return Long.valueOf(count);
		}
	}
	@Override
	public void del(String key) {
		stringCacheRao.del(key, keyPrefix);
	}


	public void setStringCacheRao(StringCacheRao stringCacheRao) {
		this.stringCacheRao = stringCacheRao;
	}


	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}


	public void setSecond(int second) {
		this.second = second;
	}
	
	

}
