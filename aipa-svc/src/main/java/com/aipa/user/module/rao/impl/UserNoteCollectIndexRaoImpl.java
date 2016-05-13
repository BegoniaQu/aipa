package com.aipa.user.module.rao.impl;

import com.aipa.user.module.entity.UserNoteCollectIndex;
import com.aipa.user.module.rao.UserNoteCollectIndexRao;
import com.aipa.user.module.utils.UserScoreUtils;
import com.qy.data.common.domain.redis.TupleLongDouble;
import com.qy.data.common.rao.impl.AbstractGenericIndexRaoImpl;

public class UserNoteCollectIndexRaoImpl extends AbstractGenericIndexRaoImpl<UserNoteCollectIndex, TupleLongDouble> implements UserNoteCollectIndexRao{

	@Override
	protected Object getValue(UserNoteCollectIndex t) {
		return t.getCollectId();
	}

	@Override
	protected UserNoteCollectIndex convert(Object hashKey, Object keyType, TupleLongDouble tuple) {
		UserNoteCollectIndex index = new UserNoteCollectIndex();
		index.setHashKey(hashKey);
		index.setKeyType(keyType);
		index.setCollectId(tuple.getElementId());
		index.setScore((long) tuple.getScore());
		//subScore和createTime
		UserScoreUtils.fillFromScore(index);
		return index;
	}

	
	
}
