package com.aipa.svc.common.exception;



import org.springframework.http.HttpStatus;

import com.aipa.svc.common.util.ResultBean;
import com.qy.data.common.exception.CloudPlatformRequestRuntimeException;


/**
 * 
 * @author qy
 *
 */
public class UnAuthorizedNoTokenRuntimeException extends CloudPlatformRequestRuntimeException {

	private static final long serialVersionUID = 7229833627236807221L;

	public UnAuthorizedNoTokenRuntimeException(String msg) {
		super(msg, ResultBean.UNAUTHORIZED_NOTOKEN, HttpStatus.UNAUTHORIZED);
	}
	
	public UnAuthorizedNoTokenRuntimeException(String msg, Throwable cause) {
		super(msg, ResultBean.UNAUTHORIZED_NOTOKEN, HttpStatus.UNAUTHORIZED, cause);
	}
	
	public UnAuthorizedNoTokenRuntimeException(String msg, String reason) {
		super(msg, reason, ResultBean.UNAUTHORIZED_NOTOKEN, HttpStatus.UNAUTHORIZED);
	}
	
	public UnAuthorizedNoTokenRuntimeException(String msg, String reason, Throwable cause) {
		super(msg, reason, ResultBean.UNAUTHORIZED_NOTOKEN, HttpStatus.UNAUTHORIZED, cause);
	}
	
	public UnAuthorizedNoTokenRuntimeException(String msg, int err, HttpStatus httpStatus) {
		super(msg, err, httpStatus);
	}
	
	public UnAuthorizedNoTokenRuntimeException(String msg, int err, HttpStatus httpStatus, Throwable cause) {
		super(msg, err, httpStatus, cause);
	}
	
	public UnAuthorizedNoTokenRuntimeException(String msg, String reason, int err, HttpStatus httpStatus) {
		super(msg, reason, err, httpStatus);
	}
	
	public UnAuthorizedNoTokenRuntimeException(String msg, String reason, int err, HttpStatus httpStatus, Throwable cause) {
		super(msg, reason, err, httpStatus, cause);
	}

}
