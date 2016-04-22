package com.qy.data.common.exception;

/**
 * @author qy
 * @date 2016
 */
public class RedisRuntimeException extends CloudPlatformRuntimeException {

	private static final long serialVersionUID = -8381946107398995340L;

	public RedisRuntimeException(String msg) {
		super(msg);
	}
	
	public RedisRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public RedisRuntimeException(String string, Class<?> entityClass) {
		super(entityClass.getName() + " " + string);
	}

}
