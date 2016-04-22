package com.qy.data.common.rao.convert;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Tuple;

import com.qy.data.common.constant.DataCommonConstant;
import com.qy.data.common.domain.redis.TupleObjectDouble;

/**
 * 所有convert处理都差不多，只需要继承次类，实现new方法和set方法。
 * @author qy
 * @date   2016
 * @param <T>
 */
public abstract class AbstractTupleObjectDoubleConvert<T extends TupleObjectDouble> implements
		TupleObjectDoubleConvert<T> {
	
	@Override
	public List<T> convertfrom(Set<Tuple> tuples) {
		if(tuples.isEmpty()){//表示redis无数据
			return new LinkedList<T>() ;
		}
		
		List<T>  ltTuple = new LinkedList<T>();
		for (Tuple tuple : tuples) {
			//XXX:会有精度的损失么？待验证
			if(DataCommonConstant.RAO_DEFAULT_FIELD_SCORE == tuple.getScore()){
				continue;
			}
			
			//by humortian  模板的初始化有什么好办法？？？
			T tupleObjectDouble = newObject();
			tupleObjectDouble.setScore(tuple.getScore());
			setElementId(tupleObjectDouble, tuple.getElement());;
			ltTuple.add(tupleObjectDouble);
		}
		
		return ltTuple;
	}
	
	@Override
	public Map<String, Double> convertto(Map<String, Double> map) {
		map.put(DataCommonConstant.RAO_DEFAULT_FIELD_KEY, DataCommonConstant.RAO_DEFAULT_FIELD_SCORE);
		return map;
	}
	
	protected abstract T newObject();
	
	protected abstract void setElementId(TupleObjectDouble tupleObjectDouble, String element);
}
