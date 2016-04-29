package com.aipa.svc.common.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
//import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.aipa.svc.common.util.JsonUtil;
import com.aipa.svc.common.util.ResultBean;
import com.aipa.svc.common.util.ResultCode;


public class AipaServerExceptionHandlerResolver extends HandlerExceptionResolverComposite{

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		//return super.resolveException(request, response, handler, ex);
//		ModelAndView mav = new ModelAndView();
//		ResultBean rb = new ResultBean(null,ResultCode.ServerError.getCode(),ResultCode.ServerError.getMsg());
//		mav.addObject(rb);
//		mav.setViewName("error");
//		return mav;
		
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			ResultBean rb = new ResultBean(null,ResultCode.ServerError.getCode(),ResultCode.ServerError.getMsg());
			String err = JsonUtil.getJsonFromObject(rb);
			pw.print(err);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
