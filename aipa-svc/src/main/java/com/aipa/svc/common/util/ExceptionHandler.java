package com.aipa.svc.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.qy.data.common.exception.CloudPlatformRequestRuntimeException;

public class ExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger("ExceptionHandler");
	
	public static Result handler(Exception e){
		if(e instanceof CloudPlatformRequestRuntimeException){
			CloudPlatformRequestRuntimeException ex = (CloudPlatformRequestRuntimeException) e;
			return new Result(null,ex.getErr(),ex.getMessage());
		} else {
			log.error("",e);
			return new Result(null, ResultCode.ServerError.getCode(),ResultCode.ServerError.getMsg());
			
		}
	}
}
