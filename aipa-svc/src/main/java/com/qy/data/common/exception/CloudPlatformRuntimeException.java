package com.qy.data.common.exception;

/**
 * 
 * @author qy
 *
 */
public class CloudPlatformRuntimeException extends RuntimeException{

	private static final long serialVersionUID = 4777011887086274817L;
	
	private String reason;

	public CloudPlatformRuntimeException(String msg) {
		this(msg, "");
	}
	
	public CloudPlatformRuntimeException(String msg, Throwable cause) {
		this(msg, "", cause);
	}
	
	public CloudPlatformRuntimeException(String msg, String reason) {
		super(msg);
		this.reason = reason;
	}
	
	public CloudPlatformRuntimeException(String msg, String reason, Throwable cause) {
		super(msg, cause);
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
}
