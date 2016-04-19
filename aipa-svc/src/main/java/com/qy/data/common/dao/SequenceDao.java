package com.qy.data.common.dao;

public interface SequenceDao {
	long getSequenceRange(String type, int batchSize);
}
