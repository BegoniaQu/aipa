package com.qy.data.common.dao.annotation;

/**
 * @author qy
 */
public enum PartitionType {
	NONE, // no partition, all documents stored to collection name   
    FIXED, // partition by field, hash to fixed number of partitions
    DISCRETE, // partition by field, each value has its own collection
}
