package com.aipa.svc.common.exception;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
//import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.aipa.svc.common.util.Result;
import com.aipa.svc.common.util.ResultCode;
import com.qy.data.common.exception.CloudPlatformRequestRuntimeException;


public class AipaServerExceptionHandlerResolver extends HandlerExceptionResolverComposite{

	private static final Logger log = LoggerFactory.getLogger("AipaServerExceptionHandlerResolver");
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
	
		Result result = null;
		if(ex instanceof CloudPlatformRequestRuntimeException){
			CloudPlatformRequestRuntimeException exception = (CloudPlatformRequestRuntimeException) ex;
			result = new Result("",exception.getErr(),exception.getMessage());
		} else {
			log.error("",ex);
			result = new Result("", ResultCode.ServerError.getCode(),ResultCode.ServerError.getMsg());
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject(result);
//		PrintWriter pw = null;
//		try {
//			pw = response.getWriter();
//			String err = JsonUtil.getJsonFromObject(result);
//			pw.print(err);
//			pw.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return mav;
	}
	
}
