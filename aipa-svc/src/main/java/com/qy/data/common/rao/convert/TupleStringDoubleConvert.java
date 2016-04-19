package com.qy.data.common.rao.convert;

import com.qy.data.common.domain.redis.TupleObjectDouble;
import com.qy.data.common.domain.redis.TupleStringDouble;

/**
 * 元素为String的实现
 * @author qy
 * @date   2016
 */
public class TupleStringDoubleConvert extends AbstractTupleObjectDoubleConvert<TupleStringDouble>{
	@Override
	protected TupleStringDouble newObject() {
		return new TupleStringDouble();
	}

	@Override
	protected void setElementId(TupleObjectDouble tupleObjectDouble,
			String element) {
		((TupleStringDouble)tupleObjectDouble).setElementId(element);
	}
}
