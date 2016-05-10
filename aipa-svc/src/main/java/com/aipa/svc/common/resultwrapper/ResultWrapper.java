package com.aipa.svc.common.resultwrapper;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.aipa.svc.common.util.Result;
import com.aipa.svc.common.util.ResultCode;

public class ResultWrapper extends HandlerInterceptorAdapter{

	
	private static final Result ok = new Result("", ResultCode.OK.getCode(),  ResultCode.OK.getMsg());
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(null == modelAndView){
		} else if(null == modelAndView.getModel() || modelAndView.getModel().isEmpty()){
			modelAndView.addObject(ok);
		}else{
			Map<String, Object> map = modelAndView.getModel();
			for (Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof BindingResult){
					continue;
				}
				modelAndView.addObject(key,new Result(value, ResultCode.OK.getCode(),  ResultCode.OK.getMsg()));
			}
		}
		
	}
}
