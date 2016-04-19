package com.qy.data.common.service.impl;

import com.qy.data.common.proxy.rao.StringCacheRao;
import com.qy.data.common.service.StringCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringCacheServiceImpl implements StringCacheService {

	private static final Logger logger = LoggerFactory.getLogger(StringCacheServiceImpl.class);
	
	private StringCacheRao stringCacheRao;
	
	@Override
	public void setCache(String suffix, String type, String cacheStr) {
		try {
			stringCacheRao.set(suffix, type, cacheStr);
		} catch (RuntimeException ignore) {
			logger.error("set cache error, suffix:{}, type:{}", suffix, type, ignore);
		}

	}

	@Override
	public void setCache(String suffix, String type, String cacheStr, Integer expireTime) {
		try {
			stringCacheRao.set(suffix, type, cacheStr, expireTime);
		} catch (RuntimeException ignore) {
			logger.error("set cache error, suffix:{}, type:{}", suffix, type, ignore);
		}

	}

	@Override
	public void clearCache(String suffix, String type) {
		try {
			stringCacheRao.del(suffix, type);
		} catch (RuntimeException ignore) {
			logger.error("clear cache error, suffix:{}, type:{}", suffix, type, ignore);
		}

	}

	@Override
	public String getCache(String suffix, String type) {
		try {
			return stringCacheRao.get(suffix, type);
		} catch (RuntimeException ignore) {
			logger.error("get cache error, suffix:{}, type:{}", suffix, type, ignore);
			return null;
		}
	}

	public void setStringCacheRao(StringCacheRao stringCacheRao) {
		this.stringCacheRao = stringCacheRao;
	}
	
}
