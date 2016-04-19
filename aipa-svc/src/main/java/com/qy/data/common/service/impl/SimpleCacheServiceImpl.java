package com.qy.data.common.service.impl;

import com.qy.data.common.service.SimpleCacheService;
import com.qy.data.common.proxy.rao.StringCacheRao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liuyang on 11/9/15.
 */
public class SimpleCacheServiceImpl implements SimpleCacheService {
    private static final Logger logger = LoggerFactory.getLogger(SimpleCacheServiceImpl.class);

    private StringCacheRao stringCacheRao;

    private String type;

    private int expireTime;

    public StringCacheRao getStringCacheRao() {
        return stringCacheRao;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setCache(String key, String cacheStr) {
        try {
            if (expireTime > 0) {
                stringCacheRao.set(key, type, cacheStr, expireTime);
            } else {
                stringCacheRao.set(key, type, cacheStr);
            }
        } catch (RuntimeException ignore) {
            logger.error("set cache error, key:{}, type:{}", key, type, ignore);
        }
    }

    @Override
    public void setCache(String key, String cacheStr, Integer expireTime) {
        try {
            if (expireTime == null || expireTime <= 0) {
                setCache(key, cacheStr);
            } else {
                stringCacheRao.set(key, type, cacheStr, expireTime);
            }
        } catch (RuntimeException ignore) {
            logger.error("set cache error, key:{}, type:{}", key, type, ignore);
        }
    }

    @Override
    public void clearCache(String key) {
        try {
            stringCacheRao.del(key, type);
        } catch (RuntimeException ignore) {
            logger.error("clear cache error, key:{}, type:{}", key, type, ignore);
        }
    }

    @Override
    public String getCache(String key) {
        try {
            return stringCacheRao.get(key, type);
        } catch (RuntimeException ignore) {
            logger.error("get cache error, key:{}, type:{}", key, type, ignore);
            return null;
        }
    }

    public void setStringCacheRao(StringCacheRao stringCacheRao) {
        this.stringCacheRao = stringCacheRao;
    }
}
