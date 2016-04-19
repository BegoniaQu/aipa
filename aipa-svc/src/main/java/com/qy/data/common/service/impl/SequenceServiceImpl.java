package com.qy.data.common.service.impl;

import com.qy.data.common.dao.SequenceDao;
import com.qy.data.common.service.AtomicSequence;
import com.qy.data.common.service.SequenceService;


/**
 * @author qy
 * 
 */
public class SequenceServiceImpl implements SequenceService {

	private SequenceDao sequenceDao;
	
	@Override
	public AtomicSequence getSequence(String type, int batchSize) {
		return new AtomicSequenceImpl(type, batchSize);
	}
	
	protected long getLastId(String type, int batchSize) {
		return sequenceDao.getSequenceRange(type, batchSize);
	}
	
	public void setSequenceDao(SequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}


	public class AtomicSequenceImpl implements AtomicSequence{
		private String type;
		private int batchSize;
		
		private long startId;
		private long maxSeq;
		
		public AtomicSequenceImpl(String type, int batchSize) {
			this.type = type;
			this.batchSize = batchSize;
			startId = getLastId(type, batchSize);
        	maxSeq = startId + batchSize;
		}


		@Override
		public synchronized long next() {
			long current =  startId++;
	        if (current < maxSeq)
	        	return current;
	        else {
	        	startId = getLastId(type, batchSize);
	        	maxSeq = startId + batchSize;
	        	return next();
			}
		}
		
	}
}


