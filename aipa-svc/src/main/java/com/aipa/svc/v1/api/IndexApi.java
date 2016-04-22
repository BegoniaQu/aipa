package com.aipa.svc.v1.api;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexApi {
	
	/**
	 * @RequestMapping后，返回值通常解析为跳转路径，加上@responsebody后返回结果不会被解析为跳转路径，而是直接写入HTTP response body
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"index.ap"},method = RequestMethod.GET)
	public User toIndex(HttpServletRequest request,HttpServletResponse response){
		User user = new User();
		user.setName("quyang");
		user.setPwd("1234");
		return user;
	}
	
}
