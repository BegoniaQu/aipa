package com.aipa.user.module.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aipa.user.module.entity.UserNoteCollectIndex;
import com.aipa.user.module.entity.UserNoteCollectIndexType;


public class UserScoreUtils {

private static final Logger logger = LoggerFactory.getLogger("CommunityScoreUtils");
	
	public static final long SECOND_NUMBER = 1000;
	public static final long MOD_NUMBER = 1000000;
	public static final long BIG_MOD_NUMBER = 10000000000L;

	/**
	 * 用户收藏索引
	 * @param userNoteCollectIndex
	 */
	public static void fillScore(UserNoteCollectIndex userNoteCollectIndex) {
		long seconds;
		if (userNoteCollectIndex.getCreateTime() == null) {
			logger.error("Failed to get create time from device instance statistics index");
			return;
		}
		switch (UserNoteCollectIndexType.convert(userNoteCollectIndex.getIndexType())) {
			default:
				seconds = userNoteCollectIndex.getCreateTime().getTime() / SECOND_NUMBER;
				long subScore = userNoteCollectIndex.getSubScore() == null ? 0 : userNoteCollectIndex.getSubScore();
				userNoteCollectIndex.setScore(subScore * BIG_MOD_NUMBER + (seconds % BIG_MOD_NUMBER));
				break;
		}
	}

	public static void fillFromScore(UserNoteCollectIndex userNoteCollectIndex) {
		if (userNoteCollectIndex.getScore() == null) {
			logger.error("Failed to get sub score from device instance statistics index");
			return;
		}
		switch (UserNoteCollectIndexType.convert(userNoteCollectIndex.getIndexType())) {
			default:
				long score = userNoteCollectIndex.getScore();
				userNoteCollectIndex.setSubScore((score / BIG_MOD_NUMBER));
				userNoteCollectIndex.setCreateTime(new Date(score % BIG_MOD_NUMBER * SECOND_NUMBER));
				break;
		}
	}
	
}
