package com.qy.data.common.service;

/**
 * Created by liuyang on 11/9/15.
 */
public interface SimpleCacheService {
    void setCache(String key, String cacheStr);
    void setCache(String key, String cacheStr, Integer expireTime);
    void clearCache(String key);
    String getCache(String key);
}
