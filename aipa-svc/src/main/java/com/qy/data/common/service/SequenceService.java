package com.qy.data.common.service;

public interface SequenceService {
	public AtomicSequence getSequence(String type, int batchSize);
}
