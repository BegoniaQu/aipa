package com.qy.data.common.dao.impl;

import com.qy.data.common.dao.SequenceDao;

/**
 * @author qy
 * @date 2016
 */
public class SequenceDaoImpl extends BaseDaoImpl implements SequenceDao {
	public static final String SEQUENCE_TABLE_NAME = "sequence";
	public static final String TYPE = "type";
	public static final String LAST_ID = "lastid";
	public static final String UPDATE_TIME = "update_time";

	@Override
	public long getSequenceRange(String type, int batchSize) {
		long nextId = jdbcTemplate.queryForObject(FIND_BEGIN_ID, Long.class, type);
		jdbcTemplate.update(UPDATE_ID, nextId + batchSize, type);
		return nextId + 1;
	}

	private static final String FIND_BEGIN_ID = "SELECT " + LAST_ID + " FROM " + SEQUENCE_TABLE_NAME + 
			" WHERE " + TYPE + "=? FOR UPDATE";
	
	private static final String UPDATE_ID = "UPDATE " + SEQUENCE_TABLE_NAME + " SET " + LAST_ID + 
			"=? WHERE " + TYPE + "=? ";
}
