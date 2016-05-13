package com.aipa.community.module.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aipa.community.module.entity.CommunityNoteCommentIndex;
import com.aipa.community.module.entity.CommunityNoteCommentIndexType;
import com.aipa.community.module.entity.CommunityNoteIndex;
import com.aipa.community.module.entity.CommunityNoteIndexType;
import com.aipa.community.module.entity.GoodClickIndex;
import com.aipa.community.module.entity.GoodClickIndexType;

public class CommunityScoreUtils {

	
	private static final Logger logger = LoggerFactory.getLogger("CommunityScoreUtils");
	
	public static final long SECOND_NUMBER = 1000;
	public static final long MOD_NUMBER = 1000000;
	public static final long BIG_MOD_NUMBER = 10000000000L;

	/**
	 * 帖子索引
	 * @param communityNoteIndex
	 */
	public static void fillScore(CommunityNoteIndex communityNoteIndex) {
		long seconds;
		if (communityNoteIndex.getCreateTime() == null) {
			logger.error("Failed to get create time from device instance statistics index");
			return;
		}
		switch (CommunityNoteIndexType.convert(communityNoteIndex.getIndexType())) {
			default:
				seconds = communityNoteIndex.getCreateTime().getTime() / SECOND_NUMBER;
				long subScore = communityNoteIndex.getSubScore() == null ? 0 : communityNoteIndex.getSubScore();
				communityNoteIndex.setScore(subScore * BIG_MOD_NUMBER + (seconds % BIG_MOD_NUMBER));
				break;
		}
	}

	public static void fillFromScore(CommunityNoteIndex communityNoteIndex) {
		if (communityNoteIndex.getScore() == null) {
			logger.error("Failed to get sub score from device instance statistics index");
			return;
		}
		switch (CommunityNoteIndexType.convert(communityNoteIndex.getIndexType())) {
			default:
				long score = communityNoteIndex.getScore();
				communityNoteIndex.setSubScore((score / BIG_MOD_NUMBER));
				communityNoteIndex.setCreateTime(new Date(score % BIG_MOD_NUMBER * SECOND_NUMBER));
				break;
		}
	}

	
	/**
	 * 帖子评论回复索引
	 * @param communityNoteCommentIndex
	 */
	public static void fillScore(CommunityNoteCommentIndex communityNoteCommentIndex) {
		long seconds;
		if (communityNoteCommentIndex.getCreateTime() == null) {
			logger.error("Failed to get create time from device instance statistics index");
			return;
		}
		switch (CommunityNoteCommentIndexType.convert(communityNoteCommentIndex.getIndexType())) {
			default:
				seconds = communityNoteCommentIndex.getCreateTime().getTime() / SECOND_NUMBER;
				long subScore = communityNoteCommentIndex.getSubScore() == null ? 0 : communityNoteCommentIndex.getSubScore();
				communityNoteCommentIndex.setScore(subScore * BIG_MOD_NUMBER + (seconds % BIG_MOD_NUMBER));
				break;
		}
	}

	public static void fillFromScore(CommunityNoteCommentIndex communityNoteCommentIndex) {
		if (communityNoteCommentIndex.getScore() == null) {
			logger.error("Failed to get sub score from device instance statistics index");
			return;
		}
		switch (CommunityNoteCommentIndexType.convert(communityNoteCommentIndex.getIndexType())) {
			default:
				long score = communityNoteCommentIndex.getScore();
				communityNoteCommentIndex.setSubScore((score / BIG_MOD_NUMBER));
				communityNoteCommentIndex.setCreateTime(new Date(score % BIG_MOD_NUMBER * SECOND_NUMBER));
				break;
		}
	}
	
	
	/**
	 * 点赞索引
	 * @param goodClickIndex
	 */
	public static void fillScore(GoodClickIndex goodClickIndex) {
		long seconds;
		if (goodClickIndex.getCreateTime() == null) {
			logger.error("Failed to get create time from device instance statistics index");
			return;
		}
		switch (GoodClickIndexType.convert(goodClickIndex.getIndexType())) {
			default:
				seconds = goodClickIndex.getCreateTime().getTime() / SECOND_NUMBER;
				long subScore = goodClickIndex.getSubScore() == null ? 0 : goodClickIndex.getSubScore();
				goodClickIndex.setScore(subScore * BIG_MOD_NUMBER + (seconds % BIG_MOD_NUMBER));
				break;
		}
	}

	public static void fillFromScore(GoodClickIndex goodClickIndex) {
		if (goodClickIndex.getScore() == null) {
			logger.error("Failed to get sub score from device instance statistics index");
			return;
		}
		switch (GoodClickIndexType.convert(goodClickIndex.getIndexType())) {
			default:
				long score = goodClickIndex.getScore();
				goodClickIndex.setSubScore((score / BIG_MOD_NUMBER));
				goodClickIndex.setCreateTime(new Date(score % BIG_MOD_NUMBER * SECOND_NUMBER));
				break;
		}
	}
}
