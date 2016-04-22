package com.qy.data.common.exception;

import org.springframework.http.HttpStatus;


/**
 * 
 * @author qy
 *
 */
public class CloudPlatformRequestRuntimeException extends CloudPlatformRuntimeException{

	private static final long serialVersionUID = 4777011887086274817L;
	
	private final String reason;
	
	private final int err;  // use the code in ResultCode
	private final HttpStatus httpStatus;

	public CloudPlatformRequestRuntimeException(String msg, int err, HttpStatus httpStatus) {
		this(msg, "", err, httpStatus);
	}
	
	public CloudPlatformRequestRuntimeException(String msg, int err, HttpStatus httpStatus, Throwable cause) {
		this(msg, "", err, httpStatus, cause);
	}
	
	public CloudPlatformRequestRuntimeException(String msg, int err) {
		this(msg, "", err, HttpStatus.OK);
	}
	
	public CloudPlatformRequestRuntimeException(String msg, String reason, int err) {
		this(msg, reason, err, HttpStatus.OK);
	}
	
	public CloudPlatformRequestRuntimeException(String msg, String reason, int err, HttpStatus httpStatus) {
		super(msg);
		this.reason = reason;
		this.err = err;
		this.httpStatus = httpStatus;
	}
	
	public CloudPlatformRequestRuntimeException(String msg, String reason, int err, HttpStatus httpStatus, Throwable cause) {
		super(msg, cause);
		this.reason = reason;
		this.err = err;
		this.httpStatus = httpStatus;
	}

	public String getReason() {
		return reason;
	}

	public int getErr() {
		return err;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
