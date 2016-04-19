package com.aipa.svc.common.exception;



import org.springframework.http.HttpStatus;

import com.aipa.svc.common.util.ResultBean;
import com.qy.data.common.exception.CloudPlatformRequestRuntimeException;


/**
 * @author qy
 * @date 2016
 */
public class UnAuthorizedNoConsistRuntimeException extends CloudPlatformRequestRuntimeException {

	private static final long serialVersionUID = 7229833627236807221L;

	public UnAuthorizedNoConsistRuntimeException(String msg) {
		super(msg, ResultBean.UNAUTHORIZED_NO_CONSIST, HttpStatus.UNAUTHORIZED);
	}
	
	public UnAuthorizedNoConsistRuntimeException(String msg, Throwable cause) {
		super(msg, ResultBean.UNAUTHORIZED_NO_CONSIST, HttpStatus.UNAUTHORIZED, cause);
	}
	
	public UnAuthorizedNoConsistRuntimeException(String msg, String reason) {
		super(msg, reason, ResultBean.UNAUTHORIZED_NO_CONSIST, HttpStatus.UNAUTHORIZED);
	}
	
	public UnAuthorizedNoConsistRuntimeException(String msg, String reason, Throwable cause) {
		super(msg, reason, ResultBean.UNAUTHORIZED_NO_CONSIST, HttpStatus.UNAUTHORIZED, cause);
	}
	
	public UnAuthorizedNoConsistRuntimeException(String msg, int err, HttpStatus httpStatus) {
		super(msg, err, httpStatus);
	}
	
	public UnAuthorizedNoConsistRuntimeException(String msg, int err, HttpStatus httpStatus, Throwable cause) {
		super(msg, err, httpStatus, cause);
	}
	
	public UnAuthorizedNoConsistRuntimeException(String msg, String reason, int err, HttpStatus httpStatus) {
		super(msg, reason, err, httpStatus);
	}
	
	public UnAuthorizedNoConsistRuntimeException(String msg, String reason, int err, HttpStatus httpStatus, Throwable cause) {
		super(msg, reason, err, httpStatus, cause);
	}

}
