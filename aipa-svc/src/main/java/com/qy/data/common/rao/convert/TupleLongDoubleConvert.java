package com.qy.data.common.rao.convert;

import com.qy.data.common.domain.redis.TupleLongDouble;
import com.qy.data.common.domain.redis.TupleObjectDouble;

/**
 * 元素为long的实现
 * @author qt
 * @date   2016
 */
public class TupleLongDoubleConvert extends AbstractTupleObjectDoubleConvert<TupleLongDouble>{
	@Override
	protected TupleLongDouble newObject() {
		return new TupleLongDouble();
	}

	@Override
	protected void setElementId(TupleObjectDouble tupleObjectDouble,
			String element) {
		((TupleLongDouble)tupleObjectDouble).setElementId(Long.parseLong(element));
	}
}
