package com.qy.data.common.rao.convert;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Tuple;

import com.qy.data.common.domain.redis.TupleObjectDouble;

/**
 * 每个人的convert可能不一样，在自己的项目层去实现这个接口
 * @author qy
 * @date 2016
 */
public interface TupleObjectDoubleConvert<T extends TupleObjectDouble>{	
	/**
	 * 如果tuples为空，不一定key不存在。需要在转换之前或者之后进行key是否存在的判断
	 * 如果里面就一个默认值，说明本身数据为empty，返回emptylist
	 * 其它返回除去默认值的实际数据
	 * @param tuples
	 * @return
	 */
	public List<T> convertfrom(final Set<Tuple> tuples);
	
	/**
	 * 实际数据转换为写入redis数据。
	 * 实际为空时，需要填入默认值
	 * @param map
	 * @return
	 */
	public Map<String, Double> convertto(final Map<String, Double> map);
}
