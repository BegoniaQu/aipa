package com.qy.data.common.proxy.rao.impl;

import com.qy.data.common.constant.DataCommonConstant;
import com.qy.data.common.domain.redis.TupleObjectDouble;
import com.qy.data.common.exception.CloudPlatformRuntimeException;
import com.qy.data.common.exception.RedisRuntimeException;
import com.qy.data.common.proxy.rao.IndexCacheRao;
import com.qy.data.common.rao.convert.TupleObjectDoubleConvert;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用于twemproxy，所以继承RedisBaseRaoImpl即可
 * twemproxy不能使用事务，不支持不带key的命令
 * @author qy
 * @date   2016
 * @param <T>
 */
public class IndexCacheRaoImpl<T extends TupleObjectDouble> extends
		RedisBaseRaoImpl implements IndexCacheRao<T> {

	private TupleObjectDoubleConvert<T> tupleObjectDoubleConvert;
	
	@Override
	public Long zrank(Object indexId, String type, String member){
		String key = null;
		Jedis jedis = null;
		try {
			key = getKey(indexId, type);
			jedis = getJedis();

			Long rank = jedis.zrank(key, member);
			if (null != rank){
				return rank;
			}

			if(jedis.exists(key)){
				return -1L;
			} else {
				return null;
			}
		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;			
			String msg = "fail to zrank key, key=" + key + ", member=" + member ;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
		
	}
	
	@Override
	public Long zadd(Object indexId, String type, double score, Object member,
			Integer seconds) {
		String key = null;
		Jedis jedis = null;
		try {
			key = getKey(indexId, type);
			jedis = getJedis();
			
			if(seconds != null){
				jedis.expire(key, seconds);
			}

			if(jedis.exists(key)){
				//这里不存在并发问题，前面有expire，不会立即就过期
				return jedis.zadd(key, score, String.valueOf(member));
			} else {
				return null;
			}
		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zadd key, key=" + key + ", member=" + member + ", score=" + score;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public Long zaddall(Object indexId, String type,
			Map<String, Double> scoreMembers, Integer seconds) {
		String key = null;
		Jedis jedis = null;
		try {
			key = getKey(indexId, type);
			Map<String, Double> map = tupleObjectDoubleConvert.convertto(scoreMembers);
			//FIXME 和zaddwithkey的逻辑不一致怎么办？ zaddwithkey没有这个convert，convert中加入了默认的 "0"
			
			jedis = getJedis();
			
			//jedis.del(key);
			Long result = jedis.zadd(key, map);
			if(seconds != null){
				jedis.expire(key, seconds);
			}
			
			return result-1;
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zset key, key=" + key + ", scoreMembers=" + scoreMembers;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public List<T> zlessthandesc(Object indexId, String type, Double score,
			int count, Integer seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			String strMax = null;
			if(null == score){
				strMax = "+inf";
			} else {
				strMax = "(" + score;
			}
			
			key = getKey(indexId, type);
			
			jedis = getJedis();
			if(null != seconds){
				jedis.expire(key, seconds);
			}
			
			if(jedis.exists(key)){
				//by humortian:必须判断exist，
				//如果tuples为空，不一定key不存在。需要在转换之前或者之后进行key是否存在的判断
				Set<Tuple> tuples = jedis.zrevrangeByScoreWithScores(key, strMax, "(0", 0, count);
				return tupleObjectDoubleConvert.convertfrom(tuples);
			} else {
				return null;
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zlessthan, key=" + key + ", nextToken=" + score + ", count=" + count;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public List<T> zmorethandesc(Object indexId, Double score, int count,
			String type, Integer seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			String strMin = null;
			if(null == score){
				strMin = "(0";
			} else {
				strMin = "(" + score;
			}
			
			key = getKey(indexId, type);
			
			jedis = getJedis();
			if(null != seconds){
				jedis.expire(key, seconds);
			}
			
			if(jedis.exists(key)){
				//by humortian:必须判断exist，
				//如果tuples为空，不一定key不存在。需要在转换之前或者之后进行key是否存在的判断
				Set<Tuple> tuples = jedis.zrangeByScoreWithScores(key, strMin, "+inf", 0, count);
				List<T> result = tupleObjectDoubleConvert.convertfrom(tuples);
				Collections.reverse(result);
				return result;
			} else {
				return null;
			}

		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zmorethandesc, key=" + key + ", previousToken=" + score + ", count=" + count;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	@Override
	public List<T> zrangedesc(Object indexId, String type, int start, int end,
			Integer seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(indexId, type);
			
			jedis = getJedis();
			if(null != seconds){
				jedis.expire(key, seconds);
			}
			
			if(jedis.exists(key)){
				//by humortian:必须判断exist，
				//如果tuples为空，不一定key不存在。需要在转换之前或者之后进行key是否存在的判断
				Set<Tuple> tuples = jedis.zrevrangeWithScores(key, start, end);
				//FIXME 还记得第一个为 "0": 0吗，asc里需要 start+1, end+1，desc时最后一个会为0...
				return tupleObjectDoubleConvert.convertfrom(tuples);
			} else {
				return null;
			}

		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zrangedesc, key=" + key + ", start=" + start + ", end=" + end;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public List<T> zrangeasc(Object indexId, String type, int start, int end,
			Integer seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(indexId, type);
			
			jedis = getJedis();
			if(null != seconds){
				jedis.expire(key, seconds);
			}
			
			if(jedis.exists(key)){
				//by humortian:必须判断exist，
				//如果tuples为空，不一定key不存在。需要在转换之前或者之后进行key是否存在的判断
				//asc要+1，第一个score是0
				Set<Tuple> tuples = jedis.zrangeWithScores(key, start+1, end+1);
				return tupleObjectDoubleConvert.convertfrom(tuples);
			} else {
				return null;
			}

		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zrangedesc, key=" + key + ", start=" + start + ", end=" + end;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}
	@Override
	public List<T> zbetweenasc(Object indexId, String type, Double start,
			Double end, Integer seconds) {
		if(null != start && null != end && start > end){
			throw new CloudPlatformRuntimeException("start more than end, start=" + start + ", end=" + end);
		}
		
		Jedis jedis = null;
		String key = null;
		try {
			String strMin = null;
			if(null == start){
				strMin = "(0";
			} else {
				strMin = "(" + start;
			}
			
			String strMax = null;
			if(null == end){
				strMax = "+inf";
			} else {
				strMax = end.toString();
			}
			
			key = getKey(indexId, type);
			
			jedis = getJedis();
			if(null != seconds){
				jedis.expire(key, seconds);
			}
			
			if(jedis.exists(key)){
				Set<Tuple> tuples = jedis.zrangeByScoreWithScores(key, strMin, strMax);
				return tupleObjectDoubleConvert.convertfrom(tuples);
			} else {
				return null;
			}
			
			
		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zbetweenasc, key=" + key + ", start=" + start + ", end=" + end;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public Long zcard(Object indexId, String type) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(indexId, type);
			
			jedis = getJedis();
			
			Long count = jedis.zcard(key);
			if(0 == count){
				return null;
			} else {
				return count - 1;
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zcard key, key=" + key ;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public Double zscore(Object indexId, String type, Object member) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(indexId, type);
			
			jedis = getJedis();
			
			Double score = jedis.zscore(key, String.valueOf(member));
			if(null != score){
				return score;
			}

			if(jedis.exists(key)){
				return -1.0;
			} else {
				return null;
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;

			throw new RedisRuntimeException("fail to zscore key, key=" + key , e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	public Long zremrangebyrank(Object indexId, String type, long start, long end, Integer seconds){
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(indexId, type);
			
			jedis = getJedis();
			if(null != seconds){
				jedis.expire(key, seconds);
			}
			
			Long remNum = jedis.zremrangeByRank(key, start, end);
			
			if(null == remNum || 0L == remNum){
				if(jedis.exists(key)){
					return 0L;
				} else {
					return null;
				}
			} else {
				return remNum;
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			String msg = "fail to remove members from key, key=" + key + " start=" + start + " end=" + end;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public Long zrem(Object indexId, String type, Integer seconds,
			Object... members) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(indexId, type);
			String[] strMembers = new String[members.length];
			for (int i=0; i<strMembers.length; ++i) {
				strMembers[i] = String.valueOf(members[i]);
			}
			
			jedis = getJedis();
			if(null != seconds){
				jedis.expire(key, seconds);
			}
			
			Long result = jedis.zrem(key, strMembers);
			if(null == result || 0 == result){
				if(jedis.exists(key)){
					return 0L;
				} else {
					return null;
				}
			} else {
				return result;
			}
			
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to remove members from key, key=" + key + ", members=" + members;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public void delKey(Object indexId, String type) {
		Jedis jedis = null;
		String key = null;
		try {
			key = getKey(indexId, type);
			
			jedis = getJedis();
			
			jedis.del(key);
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;

			throw new RedisRuntimeException("fail to del key, key=" + key , e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	protected String getKey(final Object indexId, final String type){
		return "i:"  + type + ":{" + indexId + "}";
	}

	public void setTupleObjectDoubleConvert(
			TupleObjectDoubleConvert<T> tupleObjectDoubleConvert) {
		this.tupleObjectDoubleConvert = tupleObjectDoubleConvert;
	}



	@Override
	public Long zaddwithkey(Object indexId, String type, double score,
			Object member, Integer seconds) {
		String key = null;
		Jedis jedis = null;
		try {
			key = getKey(indexId, type);
			jedis = getJedis();
			
			if(seconds != null){
				jedis.expire(key, seconds);
			}
			if(!jedis.exists(key)){
				jedis.zadd(key, 0, DataCommonConstant.RAO_DEFAULT_FIELD_KEY);
			}
			return jedis.zadd(key, score, String.valueOf(member));

		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zadd key, key=" + key + ", member=" + member + ", score=" + score;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}



	@Override
	public List<T> zmorethanasc(Object indexId, Double score, int count,
			String type, Integer seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			String strMin = null;
			if(null == score){
				strMin = "(0";
			} else {
				strMin = "(" + score;
			}
			
			key = getKey(indexId, type);
			
			jedis = getJedis();
			if(null != seconds){
				jedis.expire(key, seconds);
			}
			
			if(jedis.exists(key)){
				//by humortian:必须判断exist，
				//如果tuples为空，不一定key不存在。需要在转换之前或者之后进行key是否存在的判断
				Set<Tuple> tuples = jedis.zrangeByScoreWithScores(key, strMin,"+inf",  0, count+1);
				return tupleObjectDoubleConvert.convertfrom(tuples);
			} else {
				return null;
			}

		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zmorethandasc, key=" + key + ", previousToken=" + score + ", count=" + count;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}



	@Override
	public List<T> zlessthanasc(Object indexId, String type, Double score,
			int count, Integer seconds) {
		Jedis jedis = null;
		String key = null;
		try {
			String strMax = null;
			if(null == score){
				strMax = "+inf";
			} else {
				strMax = "(" + score;
			}
			
			key = getKey(indexId, type);
			
			jedis = getJedis();
			if(null != seconds){
				jedis.expire(key, seconds);
			}
			
			if(jedis.exists(key)){
				//by humortian:必须判断exist，
				//如果tuples为空，不一定key不存在。需要在转换之前或者之后进行key是否存在的判断
				Set<Tuple> tuples = jedis.zrevrangeByScoreWithScores(key, strMax, "(0", 0, count);
				List<T> result = tupleObjectDoubleConvert.convertfrom(tuples);
				Collections.reverse(result);
				return result;
			} else {
				return null;
			}
		} catch (RuntimeException e) { 
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zlessthan, key=" + key + ", nextToken=" + score + ", count=" + count;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}

	@Override
	public List<T> zscan(Object indexId, String type, String match, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long zcount(Object indexId, String type, Double start, Double end) {

		if(null != start && null != end && start > end){
			throw new CloudPlatformRuntimeException("start more than end, start=" + start + ", end=" + end);
		}
		
		Jedis jedis = null;
		String key = null;
		try {
			String strMin = null;
			if(null == start){
				strMin = "(0";
			} else {
				strMin = "(" + start;
			}
			
			String strMax = null;
			if(null == end){
				strMax = "+inf";
			} else {
				strMax = end.toString();
			}
			
			key = getKey(indexId, type);
			
			jedis = getJedis();
			
			if(jedis.exists(key)){
				return jedis.zcount(key, strMin, strMax);
			} else {
				return null;
			}
		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zbetweenasc, key=" + key + ", start=" + start + ", end=" + end;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	@Override
	public Long zexpire(Object indexId, String type, Integer seconds) {
		String key = null;
		Jedis jedis = null;
		try {
			key = getKey(indexId, type);
			jedis = getJedis();
			
			if (seconds != null){
				return jedis.expire(key, seconds);
			}
			return null;
		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zexpire, key=" + key + ", seconds=" + seconds;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}
	
	@Override
	public Long zexpireat(Object indexId, String type, Long expireAt) {
		String key = null;
		Jedis jedis = null;
		try {
			key = getKey(indexId, type);
			jedis = getJedis();
			
			if (expireAt != null){
				return jedis.expireAt(key, expireAt);
			}
			return null;
		} catch (RuntimeException e) {
			closeBrokenJedis(jedis);
			jedis = null;
			
			String msg = "fail to zexpireat, key=" + key + ", expireAt=" + expireAt;
			throw new RedisRuntimeException(msg, e);
		} finally {
			closeJedis(jedis);
		}
	}
}
