package com.qy.data.common.exception;

/**
 * @author qy
 * @date 2016
 */
public class GenericDaoException extends CloudPlatformRuntimeException {

	private static final long serialVersionUID = 7912532287329469129L;

	public GenericDaoException(String msg) {
		super(msg);
	}
	
	public GenericDaoException(Class<?> cls, String msg) {
		super(cls.getName() + " " + msg);
	}
	
	public GenericDaoException(Class<?> cls, String msg, Throwable cause) {
		super(cls.getName() + " " + msg, cause);
	}
	
	public GenericDaoException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
