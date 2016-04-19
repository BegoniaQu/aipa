package com.aipa.svc.common.exception;



import org.springframework.http.HttpStatus;

import com.aipa.svc.common.util.ResultBean;
import com.qy.data.common.exception.CloudPlatformRequestRuntimeException;


/**
 * @author qy
 * @date 2016
 */
public class UnAuthorizedErrorRuntimeException extends CloudPlatformRequestRuntimeException {

	private static final long serialVersionUID = 7229833627236807221L;

	public UnAuthorizedErrorRuntimeException(String msg) {
		super(msg, ResultBean.UNAUTHORIZED_ERROR, HttpStatus.UNAUTHORIZED);
	}
	
	public UnAuthorizedErrorRuntimeException(String msg, Throwable cause) {
		super(msg, ResultBean.UNAUTHORIZED_ERROR, HttpStatus.UNAUTHORIZED, cause);
	}
	
	public UnAuthorizedErrorRuntimeException(String msg, String reason) {
		super(msg, reason, ResultBean.UNAUTHORIZED_ERROR, HttpStatus.UNAUTHORIZED);
	}
	
	public UnAuthorizedErrorRuntimeException(String msg, String reason, Throwable cause) {
		super(msg, reason, ResultBean.UNAUTHORIZED_ERROR, HttpStatus.UNAUTHORIZED, cause);
	}
	
	public UnAuthorizedErrorRuntimeException(String msg, int err, HttpStatus httpStatus) {
		super(msg, err, httpStatus);
	}
	
	public UnAuthorizedErrorRuntimeException(String msg, int err, HttpStatus httpStatus, Throwable cause) {
		super(msg, err, httpStatus, cause);
	}
	
	public UnAuthorizedErrorRuntimeException(String msg, String reason, int err, HttpStatus httpStatus) {
		super(msg, reason, err, httpStatus);
	}
	
	public UnAuthorizedErrorRuntimeException(String msg, String reason, int err, HttpStatus httpStatus, Throwable cause) {
		super(msg, reason, err, httpStatus, cause);
	}

}
