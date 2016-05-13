package com.aipa.community.module.rao.impl;

import com.aipa.community.module.entity.GoodClickIndex;
import com.aipa.community.module.rao.GoodClickIndexRao;
import com.aipa.community.module.utils.CommunityScoreUtils;
import com.qy.data.common.domain.redis.TupleLongDouble;
import com.qy.data.common.rao.impl.AbstractGenericIndexRaoImpl;

public class GoodClickIndexRaoImpl extends AbstractGenericIndexRaoImpl<GoodClickIndex, TupleLongDouble> implements GoodClickIndexRao{

	@Override
	protected Object getValue(GoodClickIndex t) {
		return t.getGoodId();
	}

	@Override
	protected GoodClickIndex convert(Object hashKey, Object keyType, TupleLongDouble tuple) {
		GoodClickIndex index = new GoodClickIndex();
		index.setHashKey(hashKey);
		index.setKeyType(keyType);
		index.setGoodId(tuple.getElementId());
		index.setScore((long) tuple.getScore());
		
		//subScore和createTime
		CommunityScoreUtils.fillFromScore(index);
		return index;
	}

}
