package com.aipa.svc.common.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.aipa.svc.common.util.AipaTokenUtil;
import com.aipa.svc.common.util.JsonUtil;
import com.aipa.svc.common.util.RequestExtract;
import com.aipa.svc.common.util.Result;
import com.aipa.svc.common.util.ResultCode;

public class AuthFilter implements Filter{

	private List<String> excludedUrlList = new ArrayList<>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String urls = filterConfig.getInitParameter("excludedUrls");
		String [] strs = urls.split(",");
		for(String i : strs){
			excludedUrlList.add(i.trim());
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		String uri = req.getRequestURI();
		System.out.println(uri);
		boolean noNeedAuth = false;
		for(String url : excludedUrlList){
			if(uri.contains(url)){
				noNeedAuth = true;
				break;
			}
		}
		if(noNeedAuth){
			chain.doFilter(request, response);
			return;
		}
		//token
		String token = RequestExtract.getTk(req);
		if(token == null){
			PrintWriter pw = response.getWriter();
			Result result = new Result(null,ResultCode.AuthenticationError.getCode(),ResultCode.AuthenticationError.getMsg());
			String s = JsonUtil.getJsonFromObject(result);
			pw.write(s);
			pw.flush();
			return;
		}
		//token verify
		String str = AipaTokenUtil.verifyToken(token);
		if(str == null){
			PrintWriter pw = response.getWriter();
			Result result = new Result(null,ResultCode.AuthenticationError.getCode(),ResultCode.AuthenticationError.getMsg());
			String s = JsonUtil.getJsonFromObject(result);
			pw.write(s);
			pw.flush();
			return;
		}
		
		Long uid = Long.parseLong(str);
		request.setAttribute("uid", uid);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}

}
