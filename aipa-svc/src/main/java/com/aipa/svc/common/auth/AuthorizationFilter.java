package com.aipa.svc.common.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.aipa.svc.common.exception.UnAuthorizedErrorRuntimeException;
import com.aipa.svc.common.util.JsonUtil;
import com.aipa.svc.common.util.KafkaUtil;
import com.aipa.svc.common.util.ParameterTool;
import com.aipa.svc.common.util.ResultBean;
import com.aipa.svc.common.util.ResultCode;
import com.aipa.svc.common.util.ServletUtils;
import com.aipa.svc.common.util.TokenUtil_RE;
import com.qy.data.common.exception.CloudPlatformRequestRuntimeException;
import com.qy.data.common.util.StringUtil;



/**
 * authMethod 需要做权限验证的method，为null表示表
import com.qy.data.common.util.StringUtil;示都需要做
 * @author humortian
 * @date 2013-8-13
 */
public class AuthorizationFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);
	
	public static final String KEY_USER_NAME = "__UserName";

//	public static final String KEY_HOST = "__Host";

	public static final String KEY_AUTH_TOKEN = "tk";
	public static final String KEY_AUTH_SIGNATURE = "sign";

	private Set<String> authMethod;	//需要做权限验证，为null表示表示都需要做
	
	protected List<Pattern> excludePatterns;

	@Override
	public void init(FilterConfig config) throws ServletException {
		String excludePatternsStr = config.getInitParameter("excludePatterns");
		if(!StringUtil.isEmpty(excludePatternsStr)){
			String[] excludePatternsStrArr = StringUtils.split(excludePatternsStr, "\n");
			List<Pattern> patternList = new ArrayList<Pattern>();
			for (String excludePatternStr : excludePatternsStrArr) {
				excludePatternStr = StringUtils.strip(excludePatternStr);
				if(StringUtil.isEmpty(excludePatternStr)) continue;
				patternList.add(Pattern.compile(excludePatternStr.trim()));
			}
			excludePatterns = patternList;
		}
		setAuthMethod(config.getInitParameter("authMethod"));
	}
	
	public boolean matchExcludePatterns(String path){
		if(excludePatterns != null){
			for (Pattern excludePattern : excludePatterns) {
				if(excludePattern.matcher(path).matches()) return true;
			}
		}
		return false;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String path = ((HttpServletRequest) request).getRequestURI();
		String contextName = ((HttpServletRequest) request).getContextPath();
		if(!StringUtil.isEmpty(contextName)){
			if(path.startsWith(contextName)) path = path.substring(contextName.length());
		}
		if(matchExcludePatterns(path)){
			chain.doFilter(request, response);
		    return;
		} 
//		FilterRequestWrapper requestWrpper = new FilterRequestWrapper((HttpServletRequest)request);
		
		HttpServletRequest requestWrpper = (HttpServletRequest)request;
		
		try {
			//判断是否是需要做权限验证的method
			String method = requestWrpper.getMethod().toUpperCase();
			if ((null != authMethod && !authMethod.contains(method)) || validate(requestWrpper)) {
				KafkaUtil.kafkaTrace(requestWrpper);
				chain.doFilter(requestWrpper, response);
			} else {
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} catch (IOException e) {
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.warn("auth filter error. url={}", ParameterTool.getURL(requestWrpper) + requestWrpper.getQueryString(), e);
		} catch (ServletException e) {
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.warn("auth filter error. url={}", ParameterTool.getURL(requestWrpper)  + requestWrpper.getQueryString(), e);
		} catch (Exception ex) {
			ResultBean resBean = new ResultBean();

			if (ex instanceof CloudPlatformRequestRuntimeException) {
				logger.warn(ex.getMessage(), ex);
				CloudPlatformRequestRuntimeException e = (CloudPlatformRequestRuntimeException) ex;
				resBean.setErr(e.getErr());
				String msg = e.getMessage();
				if(StringUtil.isEmpty(msg)){
					ResultCode resultCode = ResultCode.getResultCode(e.getErr());
					if(resultCode != null) msg = resultCode.getMsg();
				}
				httpResponse.setStatus(e.getHttpStatus().value());
				resBean.setMsg(e.getReason() + "  " + msg);
				ServletUtils.sendTextResponse(httpResponse, e.getHttpStatus().value(), JsonUtil.getJsonFromObject(resBean));
			} else {
				logger.error(ex.getMessage(), ex);
				resBean.setErr(ResultBean.SYS_ERROR);
				httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				resBean.setMsg(ex.getMessage());
				
				ServletUtils.sendTextResponse(httpResponse, HttpStatus.INTERNAL_SERVER_ERROR.value(), JsonUtil.getJsonFromObject(resBean));
			}
		}
	}

	public void destroy() {}

	private static final int TOKEN_INDX=0;

	private boolean validate(HttpServletRequest request) throws IOException {
		parseSignature(request);
		String token = request.getParameter(KEY_AUTH_TOKEN);
		if (StringUtil.isEmpty(token)) {
			String authString = request.getHeader("Authorization");
			if (StringUtil.isEmpty(authString)) {
				//如果token存在,才去校验,不存在,不校验
				return true;
			}
			
			String[] auths = parseAuth(authString);

			if (auths[TOKEN_INDX] != null) {
				return validatePassportToken(URLDecoder.decode(auths[TOKEN_INDX],"UTF-8"), request);
			} else {
				throw new UnAuthorizedErrorRuntimeException("no authorization");
			}

		} else {
			return validatePassportToken(URLDecoder.decode(token,"UTF-8"), request);
		}
	}
	
	private void parseSignature(HttpServletRequest request) throws IOException {
		String sig = request.getHeader("Signature");
		if (StringUtil.isEmpty(sig)) {
			return;
		} else {
			request.setAttribute(KEY_AUTH_SIGNATURE, sig);
		}
	}	

	private static final int USER_NAME_INDX = 0;
	@SuppressWarnings("unused")
	private static final int EXPIRE_TIME_INDX = 1;
	private boolean validatePassportToken(String token, HttpServletRequest request) {
		String[] fields=null;
		try {
			String origData = TokenUtil_RE.decryptToken(token);
	        fields = origData.split("&");
		} catch (Exception e) {
			logger.warn("error token, token={}, url={}, from={}, version={}, ip={}", 
					token, ParameterTool.getURL(request),  
					request.getParameter("appplt"), request.getParameter("appver"), ParameterTool.getIpAddr(request), e);
			throw new UnAuthorizedErrorRuntimeException("error token");
		}
		
//		long time = Long.parseLong(fields[EXPIRE_TIME_INDX]);
//		if (time < System.currentTimeMillis()) {
//			logger.warn("expired token, username={}, url={}, appplt={}, appver={}, ip={}", 
//					new Object[]{fields[USER_NAME_INDX], ParameterTool.getURL(request), 
//					request.getParameter("appplt"), request.getParameter("appver"), ParameterTool.getIpAddr(request)});
//			throw new UnAuthorizedExpireRuntimeException("expire time=" + time);
//		}
		
		request.setAttribute(KEY_AUTH_TOKEN, token);
		request.setAttribute(KEY_USER_NAME, fields[USER_NAME_INDX]);

		return true;
	}

	private String[] parseAuth(String authString) throws UnsupportedEncodingException {
		String[] auths = authString.split(" ");
		String[] ret = new String[5];
		for(String auth : auths) {
			String[] tmp = auth.split("=");
			if (tmp.length != 2) {
				continue;
			}
			if(KEY_AUTH_TOKEN.equals(tmp[0])) {
				ret[TOKEN_INDX] = tmp[1];
				break;
			}
		}
		return ret;
	}

	public void setAuthMethod(String strAuthMethod) {
		if(StringUtil.isEmpty(strAuthMethod)){
			return;
		}
		
		String[] methods = strAuthMethod.split(",");
		authMethod = new HashSet<String>(Arrays.asList(methods));
	}
}