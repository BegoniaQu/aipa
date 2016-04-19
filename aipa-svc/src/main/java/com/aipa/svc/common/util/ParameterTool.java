package com.aipa.svc.common.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aipa.svc.common.exception.InvalidRequestRuntimeException;
import com.qy.data.common.util.StringUtil;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;



/**
 * @author qy
 *
 */
public final class ParameterTool {
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ParameterTool.class);
	
	public static final String REQUEST_IF_NONE_MATCH = "If-None-Match";
	public static final String REQUEST_COOKIE = "Cookie";
	public static final String RESPONSE_ETAG = "ETag";
	public static final String RESPONSE_CACHE = "response_cache";
	
	// instantiation forbid
	private ParameterTool() {
	}

	public static Integer getParameterInteger(HttpServletRequest request,
			String name, Integer defaultValue) {
		Integer i = defaultValue;
		try {
			String value = request.getParameter(name);
			if( !StringUtil.isEmpty(value) ) {
				i = Integer.valueOf(value);
			}
		} catch (RuntimeException e) {
			throw new InvalidRequestRuntimeException("request parameter format error, ", name + " format error", e);
		}
		return i;
	}

	public static Boolean getParameterBoolean(HttpServletRequest request,
			String name, Boolean defaultValue) {		
		Boolean b = defaultValue;
		try {
			String value = request.getParameter(name);	
			if(!StringUtil.isEmpty(value)){
				b = Boolean.valueOf(value);				
			}
		} catch (RuntimeException e) {
			throw new InvalidRequestRuntimeException("request parameter format error, ", name + " format error", e);
		}
		return b;
	}
	
	/**
	 * 获得boolean值。0表示false。 1 表示true
	 * @param request
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static Boolean getParameterBooleanDigtal(HttpServletRequest request,
			String name, Boolean defaultValue) {	
		try {
			String value = request.getParameter(name);	
			if(StringUtil.isEmpty(value)){
				return defaultValue;
			}
			
			if("0".equals(value)){
				return false;
			} else {
				return true;
			}
		} catch (RuntimeException e) {
			throw new InvalidRequestRuntimeException("request parameter format error, ", name + " format error", e);
		}

	}

	public static Long getParameterLong(HttpServletRequest request,
			String name, Long defaultValue) {
		Long l = defaultValue;
		try {
			String value = request.getParameter(name);	
			if(!StringUtil.isEmpty(value)){
				l = Long.valueOf(value);				
			}
		} catch (RuntimeException e) {
			throw new InvalidRequestRuntimeException("request parameter format error, ", name + " format error", e);
		}
		return l;
	}

	public static Float getParameterFloat(HttpServletRequest request,
			String name, Float defaultValue) {
		Float f = defaultValue;
		try {
			String value = request.getParameter(name);	
			if(!StringUtil.isEmpty(value)){
				f = Float.valueOf(value);				
			}
		} catch (Exception e) {
			throw new InvalidRequestRuntimeException("request parameter format error, ", name + " format error", e);
		}
		return f;
	}

	public static Double getParameterDouble(HttpServletRequest request,
										  String name, Double defaultValue) {
		Double d  = defaultValue;
		try {
			String value = request.getParameter(name);
			if(!StringUtil.isEmpty(value)){
				d = Double.valueOf(value);
			}
		} catch (Exception e) {
			throw new InvalidRequestRuntimeException("request parameter format error, ", name + " format error", e);
		}
		return d;
	}

	public static String getParameterString(HttpServletRequest request,
			String name, String defaultValue) {
		String s = request.getParameter(name);
		if (StringUtil.isEmpty(s)) {
			s = defaultValue;
		} else {
			s = s.trim();
		}
		return s;
	}
	
	/**
	 * 参数必须存在，不然抛出异常
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getParameterString(HttpServletRequest request, String name){
		String s = request.getParameter(name);
		if(StringUtil.isEmpty(s)){
			throw new InvalidRequestRuntimeException("param:" + name + " not exist");
		}
		return s;
	}
	
	public static String getAttrString(HttpServletRequest request,
			String name, String defaultValue) {
		String s = (String) request.getAttribute(name);
		if(StringUtil.isEmpty(s)){
			s = defaultValue;
		} else{
			s = s.trim();
		}

		return s;
	}
	
	public static Long getAttrLong(HttpServletRequest request,
			String name, Long defaultValue) {
		Object obj = request.getAttribute(name);
		Long l = LongConvert.getConvert().getValue(obj);
		if(l == null) {
			l = defaultValue;
		}
		
		return l;
	}
	
	public static Object getAttrObject(HttpServletRequest request,
			String name, Object defaultValue) {
		Object obj = request.getAttribute(name);
		if(obj == null) {
			obj = defaultValue;
		}
		return obj;
	}
	
	public static String getRequestVersion(HttpServletRequest request){
		return request.getHeader(REQUEST_IF_NONE_MATCH);
	}
	
	public static String getRequestCookieStr(HttpServletRequest request){
		return request.getHeader(REQUEST_COOKIE);
	}
	
	public static void setResponseVersion(HttpServletResponse response, String version){
		response.setHeader(ParameterTool.RESPONSE_ETAG, version);
	}
	
	public static String getUri(HttpServletRequest request) {		
		return request.getRequestURI();
	}
	
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr();
        }
        if(ip != null && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }
    
    public static String getHost(HttpServletRequest request) {
        String host = request.getHeader("Host");
        return host;
    }
    
    
    public static void setResponseNoCache(HttpServletResponse response){   	
    	setResponseExpire(response, null, 0L);
    }
	
	public static Integer getPort(HttpServletRequest request) {
    	String strPort = null;
    	try {
    		strPort = request.getHeader("X-Remote-Port");
        	if(!StringUtil.isEmpty(strPort)){
        		return Integer.valueOf(strPort);
        	}
		} catch (Exception e) {
			logger.warn("remote_port error, remote_port={}", strPort, e);
		}

        return request.getRemotePort();
    }
    
    public static void setResponseExpire(HttpServletResponse response, HttpServletRequest request, long seconds){
    	
    	if( seconds <= 0l ) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addDateHeader("Expires", 1L);
    	} else {
	    	long now = new Date().getTime();
	    	
	    	response.setDateHeader("Last-Modified", now);
	    	response.setDateHeader("Expires", now + TimeUnit.SECONDS.toMillis(seconds));
	    	response.setHeader("Cache-Control", "public, max-age=" + seconds);
	    	setAttrResponseCache(request);
    	}
    }
    
    private static void setAttrResponseCache(HttpServletRequest request){
		request.setAttribute(RESPONSE_CACHE, true);
	}
	
	public static boolean isResponseCache(HttpServletRequest request){
		Object b = request.getAttribute(RESPONSE_CACHE);
		if( b == null ) {
			return false;
		} else {
			return (Boolean) b;
		}
	}
	
	/**
     * get LAST_MODIFIED from response, but no to compare
     * @param response
     * @return
     */
    public static String getLastModified(HttpResponse response){
    	Header modifyTime = response.getFirstHeader(HttpHeaders.LAST_MODIFIED);
        if(null != modifyTime){
        	return modifyTime.getValue();
        } else{
        	return null;
        }
    }
/**
     * get ETAG from response, but no to compare
     * @param response
     * @return
     */
    public static String getEtag(HttpResponse response){
    	Header etag = response.getFirstHeader(HttpHeaders.ETAG);
        if(null != etag){
        	return etag.getValue();
        } else{
        	return null;
        }
    }
    
    public static boolean isCdn(HttpServletRequest request){
		String host = ParameterTool.getHost(request);
		if( !StringUtil.isEmpty(host) && host.startsWith("apicdn")) {
			return true;
		} else {
			return false;
		}
	}
    
    public static String getURL(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }
}
