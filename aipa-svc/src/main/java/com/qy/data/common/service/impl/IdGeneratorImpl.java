package com.qy.data.common.service.impl;

import com.qy.data.common.service.AtomicSequence;
import com.qy.data.common.service.IdGenerator;
import com.qy.data.common.service.SequenceService;


/**
 * @author qy
 * @date 2016
 */
public class IdGeneratorImpl implements IdGenerator{
	protected SequenceService sequenceService;
	protected String type;
	protected int batchSize;
	
	protected AtomicSequence idSeq;
	
	public void init(){
		idSeq = sequenceService.getSequence(type, batchSize);
	}
	
	@Override
	public long getId(){
		return idSeq.next();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public SequenceService getSequenceService() {
		return sequenceService;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}
}
