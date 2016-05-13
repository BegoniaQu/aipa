package com.aipa.community.module.rao.impl;

import com.aipa.community.module.entity.CommunityNoteCommentIndex;
import com.aipa.community.module.rao.CommunityNoteCommentIndexRao;
import com.aipa.community.module.utils.CommunityScoreUtils;
import com.qy.data.common.domain.redis.TupleLongDouble;
import com.qy.data.common.rao.impl.AbstractGenericIndexRaoImpl;

public class CommunityNoteCommentIndexRaoImpl extends AbstractGenericIndexRaoImpl<CommunityNoteCommentIndex, TupleLongDouble> 
implements CommunityNoteCommentIndexRao{

	@Override
	protected Object getValue(CommunityNoteCommentIndex t) {
		return t.getCommentId();
	}

	@Override
	protected CommunityNoteCommentIndex convert(Object hashKey, Object keyType, TupleLongDouble tuple) {
		CommunityNoteCommentIndex index = new CommunityNoteCommentIndex();
		index.setHashKey(hashKey);
		index.setKeyType(keyType);
		index.setCommentId(tuple.getElementId());
		index.setScore((long) tuple.getScore());
		
		//subScore和createTime
		CommunityScoreUtils.fillFromScore(index);
		return index;
	}

}
