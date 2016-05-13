package com.aipa.community.module.rao.impl;

import com.aipa.community.module.entity.CommunityNoteIndex;
import com.aipa.community.module.rao.CommunityNoteIndexRao;
import com.aipa.community.module.utils.CommunityScoreUtils;
import com.qy.data.common.domain.redis.TupleLongDouble;
import com.qy.data.common.rao.impl.AbstractGenericIndexRaoImpl;

public class CommunityNoteIndexRaoImpl extends AbstractGenericIndexRaoImpl<CommunityNoteIndex, TupleLongDouble> implements CommunityNoteIndexRao{

	
	@Override
	protected Object getValue(CommunityNoteIndex t) {
		return t.getNoteId();
	}

	
	@Override
	protected CommunityNoteIndex convert(Object hashKey, Object keyType, TupleLongDouble tuple) {
		CommunityNoteIndex index = new CommunityNoteIndex();
		index.setHashKey(hashKey);
		index.setKeyType(keyType);
		index.setNoteId(tuple.getElementId());
		index.setScore((long) tuple.getScore());
		
		//subScore和createTime
		CommunityScoreUtils.fillFromScore(index);
		return index;
	}

	
	
}
