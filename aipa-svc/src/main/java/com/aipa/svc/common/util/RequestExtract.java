package com.aipa.svc.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.aipa.svc.common.auth.AuthorizationFilter;
import com.aipa.svc.common.exception.InvalidRequestRuntimeException;
import com.aipa.svc.common.exception.UnAuthorizedErrorRuntimeException;
import com.aipa.svc.common.exception.UnAuthorizedNoConsistRuntimeException;
import com.aipa.svc.common.exception.UnAuthorizedNoTokenRuntimeException;
import com.qy.data.common.util.StringUtil;


/**
 * @author qy
 * @date 2016
 */
public class RequestExtract {
	private static final String REQUEST_PARAM_APPPLT = "appplt";
	private static final String REQUEST_PARAM_APPVER = "appver";
	private static final String REQUEST_PARAM_ACTION = "action";//分页方式，0 瀑布式  1 分页式
	private static final String REQUEST_PARAM_PAGE_SIZE = "pageSize";
	private static final String REQUEST_PARAM_PAGE_NUM = "page";
	private static final String REQUEST_PARAM_NEXT_TOKEN = "nt";
	private static final String REQUEST_PARAM_PREVIOUS_TOKEN = "pt";
	private static final String REQUEST_PARAM_ASC = "asc";
	private static final String REQUEST_PARAM_USER_NAME = "user_name";
	private static final String REQUEST_PARAM_USER_ID = "user_id";
	public static final String REQUEST_PARAM_USER_TOKEN = "tk";
	private static final String REQUEST_PARAM_SIGN = "sign";
	//private static final String REQUEST_PARAM_IDS = "ids";
	
	public static final String REQUEST_ATTR_USER_NAME = AuthorizationFilter.KEY_USER_NAME;
	public static final String REQUEST_ATTR_USER_TOKEN = AuthorizationFilter.KEY_AUTH_TOKEN;
	private static final String REQUEST_ATTR_SIGN = AuthorizationFilter.KEY_AUTH_SIGNATURE;
	
	public static final int DEFAULT_PAGE_SIZE = 10;
	public static final int DEFAULT_PAGE_NUM = 0;
	
	/**
	 * user_name必须在参数中存在
	 * @param request
	 * @return
	 */
	public static String getUserNameMust(HttpServletRequest request){
		return ParameterTool.getParameterString(request, REQUEST_PARAM_USER_NAME);
	}
	
	public static String getUserIdMust(HttpServletRequest request){
		return ParameterTool.getParameterString(request, REQUEST_PARAM_USER_ID);
	}
	
	
	public static String getUserNameAttr(HttpServletRequest request){
		return ParameterTool.getParameterString(request, REQUEST_PARAM_USER_NAME);
	}
	
	public static String getUserName(HttpServletRequest request){
		return ParameterTool.getParameterString(request, REQUEST_PARAM_USER_NAME, null);
	}
	
	public static List<Long> getIds(HttpServletRequest request, String param){
		String strId = ParameterTool.getParameterString(request, param, null);
		if(StringUtil.isEmpty(strId)){
			throw new InvalidRequestRuntimeException("param error, ids=" + strId);
		}
		
		String[] strFeedIds = strId.split(",");
		if(strFeedIds.length == 0){
			throw new InvalidRequestRuntimeException("param error, ids=" + strId);
		}
		
		List<Long> ids = new ArrayList<Long>(strFeedIds.length);
		try {
			for (String str : strFeedIds) {
				if(StringUtil.isEmpty(str)){
					continue;
				}
				
				ids.add(Long.valueOf(str));
			}
		} catch (Exception e) {
			throw new InvalidRequestRuntimeException("param error, ids=" + strId, e);
		}
		
		if(ids.isEmpty()){
			throw new InvalidRequestRuntimeException("param error, ids=" + strId);
		}

		return ids;
	}
	
	public static List<Integer> getIntegerIds(HttpServletRequest request, String param){
		String strId = ParameterTool.getParameterString(request, param, null);
		if(StringUtil.isEmpty(strId)){
			throw new InvalidRequestRuntimeException("param error, ids=" + strId);
		}
		
		String[] strFeedIds = strId.split(",");
		if(strFeedIds.length == 0){
			throw new InvalidRequestRuntimeException("param error, ids=" + strId);
		}
		
		List<Integer> ids = new ArrayList<Integer>(strFeedIds.length);
		try {
			for (String str : strFeedIds) {
				if(StringUtil.isEmpty(str)){
					continue;
				}
				
				ids.add(Integer.valueOf(str));
			}
		} catch (Exception e) {
			throw new InvalidRequestRuntimeException("param error, ids=" + strId, e);
		}
		
		if(ids.isEmpty()){
			throw new InvalidRequestRuntimeException("param error, ids=" + strId);
		}

		return ids;
	}
	
	public static List<String> getStringIds(HttpServletRequest request, String param){
		String strId = ParameterTool.getParameterString(request, param, null);
		if(StringUtil.isEmpty(strId)){
			throw new InvalidRequestRuntimeException("param error, " + param + "=" + strId);
		}
		
		String[] strFeedIds = strId.split(",");
		if(strFeedIds.length == 0){
			throw new InvalidRequestRuntimeException("param error, " + param + "=" + strId);
		}
		
		List<String> ids = new ArrayList<String>(strFeedIds.length);
		try {
			for (String str : strFeedIds) {
				if(StringUtil.isEmpty(str)){
					continue;
				}
				
				ids.add(str);
			}
		} catch (Exception e) {
			throw new InvalidRequestRuntimeException("param error, " + param + "=" + strId);
		}
		
		if(ids.isEmpty()){
			throw new InvalidRequestRuntimeException("param error, ids=" + strId);
		}

		return ids;
	}
	
	/**
	 * 返回false表示瀑布式，返回true，表示分页式
	 * @param request
	 * @return
	 */
	public static boolean getPageAction(HttpServletRequest request){
		return ParameterTool.getParameterBooleanDigtal(request, REQUEST_PARAM_ACTION, true);
	}
	

	public static int getPageSize(HttpServletRequest request){
		int ps= ParameterTool.getParameterInteger(request, REQUEST_PARAM_PAGE_SIZE, DEFAULT_PAGE_SIZE);
		if(ps <=0){
			throw new InvalidRequestRuntimeException("ps error," + REQUEST_PARAM_PAGE_SIZE+ "=" + ps); 
		}
		
		return ps;
	}
	
	public static int getAsc(HttpServletRequest request){
		return ParameterTool.getParameterInteger(request, REQUEST_PARAM_ASC, 0);
	}
	
	public static int getAsc(HttpServletRequest request, int defaultValue){
		return ParameterTool.getParameterInteger(request, REQUEST_PARAM_ASC, defaultValue);
	}
	
	public static int getPageNum(HttpServletRequest request){
		int pn = ParameterTool.getParameterInteger(request, REQUEST_PARAM_PAGE_NUM, DEFAULT_PAGE_NUM);
		if(pn<0){
			throw new InvalidRequestRuntimeException("pn error," + REQUEST_PARAM_PAGE_NUM+ "=" + pn); 
		}
		return pn;
	}
	
	public static Long getNextToken(HttpServletRequest request){
		String str = ParameterTool.getParameterString(request, REQUEST_PARAM_NEXT_TOKEN, null);
		if(StringUtil.isEmpty(str)){
			return null;
		} else {
			return Long.valueOf(str);
		}
	}
	
	public static Long getPreviousToken(HttpServletRequest request){
		String str = ParameterTool.getParameterString(request, REQUEST_PARAM_PREVIOUS_TOKEN, null);
		if(StringUtil.isEmpty(str)){
			return null;
		} else {
			return Long.valueOf(str);
		}
	}
	
	public static Appplt getAppplt(HttpServletRequest request){
		String from = ParameterTool.getParameterString(request, REQUEST_PARAM_APPPLT, null);
		if(StringUtil.isEmpty(from)){
			throw new InvalidRequestRuntimeException("param error, appplt=" + from);
		}
		
		Appplt deviceFrom = null;
		try {
			deviceFrom = Appplt.valueOf(from);
		} catch (Exception e) {
			throw new InvalidRequestRuntimeException("param error, appplt=" + from, e);
		}

		return deviceFrom;
	}
	
	public static List<String> getApppltSStrs(HttpServletRequest request){
		String strfroms = ParameterTool.getParameterString(request, REQUEST_PARAM_APPPLT, null);
		if(StringUtil.isEmpty(strfroms)){
			return null;
		}
		
		String[] froms = strfroms.split(",");
		List<String> appplts = new ArrayList<String>(froms.length);
		for (String from : froms) {
			if(!StringUtil.isEmpty(from)){
				appplts.add(from);
			}
		}
		
		return appplts;
	}
	
	public static String getAppver(HttpServletRequest request){
		return ParameterTool.getParameterString(request, REQUEST_PARAM_APPVER, null);
	}
	
	public static String getSign(HttpServletRequest request){
		String sign = ParameterTool.getAttrString(request, REQUEST_ATTR_SIGN, null);
		if (StringUtil.isEmpty(sign)) {
			sign = ParameterTool.getParameterString(request, REQUEST_PARAM_SIGN, null);
		}
		return sign;
	}
	
	/**
	 * 不存在返回null
	 * @param request
	 * @return
	 */
	public static String getAuthUserName(HttpServletRequest request){
		return ParameterTool.getAttrString(request, REQUEST_ATTR_USER_NAME, null);
	}

	public static String authUserName(HttpServletRequest request){
		String userNameAttr = getAuthUserName(request);
		
		if(StringUtil.isEmpty(userNameAttr)){
			throw new UnAuthorizedNoTokenRuntimeException("no token");
		}
		
		return userNameAttr;
	}
	
	public static String getUserToken(HttpServletRequest request) {
		String tk = ParameterTool.getAttrString(request, REQUEST_ATTR_USER_TOKEN, null);
		if (StringUtil.isEmpty(tk)) {
			tk = ParameterTool.getParameterString(request, REQUEST_PARAM_USER_TOKEN, null);
		}
		return tk;
	}
	
	public static void verifyUserName(HttpServletRequest request, String userName){
		String userNameAttr = authUserName(request);
		
		if(!userName.equalsIgnoreCase(userNameAttr)){
			throw new UnAuthorizedNoConsistRuntimeException("user name in token not consistent with param, token="
					+ userNameAttr+ ", param=" + userName);
		}
	}
	
	public static String getUrlForSign(HttpServletRequest request) {
		Map<String, String> map = new TreeMap<String, String>();
		Enumeration<String> paraNames = request.getParameterNames();
		for (Enumeration<String> e = paraNames; e.hasMoreElements();) {
			String key = e.nextElement().toString();
			String value = request.getParameter(key);
			// 去掉sign本身
			if (!REQUEST_PARAM_SIGN.equalsIgnoreCase(key)) {
				map.put(key, value);
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append(request.getRequestURL().toString()).append("?");
		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			sb.append(key).append(map.get(key));
		}
		return sb.toString();
	}
	
	private static String getStrForSign(String method, String url, String json, String token, String clientPrvKey, String userPrvApiKey) {
		StringBuilder sb = new StringBuilder();
		sb.append(method);
		sb.append(url);
		if (!StringUtil.isEmpty(json)) {
			sb.append(json);
		}
		if (!StringUtil.isEmpty(token)) {
			sb.append(token);
		}
		sb.append(clientPrvKey);
		if (!StringUtil.isEmpty(userPrvApiKey)) {
			sb.append(userPrvApiKey);
		}
		return sb.toString();
	}
	

	/**
	 * 	做复杂的签名，需要apiKey
	 * @param request
	 * @param clientPrvKey
	 * @param userPrvApiKey
	 * @param userAuthMust
	 * @return
	 */
	public static void verifySign(HttpServletRequest request, String clientPrvKey, String userPrvApiKey) {
		String method = request.getMethod().toUpperCase();
		String json = ServletUtils.readStringFromRequest(request);
		String rawStrBeforeSign = null;
		String url = getUrlForSign(request);
		String token = getUserToken(request);
		rawStrBeforeSign = getStrForSign(method, url, json, token, clientPrvKey, userPrvApiKey);
		
		String signed = MD5Util.encode(rawStrBeforeSign);
		if (signed.equalsIgnoreCase(getSign(request))) {
			return ;
		}
		throw new UnAuthorizedNoTokenRuntimeException("incorrect sign");
	}
	
	/**
	 * 获取签名后的结果
	 * @param request
	 * @param clientPrvKey
	 * @param userPrvApiKey
	 * @param userAuthMust
	 * @return
	 */
	public static String getSign(HttpServletRequest request, String clientPrvKey, String userPrvApiKey) {
		String method = request.getMethod().toUpperCase();
		String json = ServletUtils.readStringFromRequest(request);
		String rawStrBeforeSign = null;
		String url = getUrlForSign(request);
		String token = getUserToken(request);
		rawStrBeforeSign = getStrForSign(method, url, json, token, clientPrvKey, userPrvApiKey);
		
		String signed = MD5Util.encode(rawStrBeforeSign);
		return signed;
	}
	
	/**
	 * 做简单的签名，不需要apiKey
	 * @param request
	 * @param clientPrvKey
	 * @param userAuthMust
	 * @return
	 */
	public static boolean verifySimpleSign(HttpServletRequest request, String clientPrvKey, boolean userAuthMust) {
		String method = request.getMethod().toUpperCase();
		String json = ServletUtils.readStringFromRequest(request);
		String rawStrBeforeSign = null;
		String url = getUrlForSign(request);
		if (userAuthMust) {
			String token = getUserToken(request);
			rawStrBeforeSign = getStrForSign(method, url, json, token, clientPrvKey, token);
		} else {
			rawStrBeforeSign = getStrForSign(method, url, json, null, clientPrvKey, null);
		}
		
		String signed = MD5Util.encode(rawStrBeforeSign);
		if (signed.equalsIgnoreCase(getSign(request))) {
			return true;
		}
		return false;
	}
	
	public static void verifySign(HttpServletRequest request){
		//TODO:做签名验证
		String sign = ParameterTool.getAttrString(request, REQUEST_ATTR_USER_NAME, null);
		
		if(StringUtil.isEmpty(sign)){
			throw new UnAuthorizedErrorRuntimeException("sign error");
		}
	}
	
	public static String verifySignAndUserId(HttpServletRequest request){
		verifySign(request);
		return authUserName(request);
	}
	
	public static String verifyAuthUserName(HttpServletRequest request) {
		String userId =  getUserIdMust(request);
		verifyUserName(request, userId); //username 和 userid 是一回事； login_name则是用户...； nick_name是用户在服务内的...
		return userId;
	}
	public static final void main(String[] args) {
		String method = "DELETE";
		String json = "";
		String rawStrBeforeSign = null;
		String url = "http://121.41.104.80:8080/topic/v1/topic/info/post/1273901?apppltiphappver2.1.4tk446d1cabb1e5ec6a0691186af6a8e9067cb2a089c058b9b2eee5a25ad9e000ffa8da5e67188b78c8b3295956448d49401e24968460dae04435af9b4f1718057a6b36ecf91f75760c69204824b21ad88ef0d192f0abb0128fca27bcfcfcb6734424b7c3ef72683ed25d54c634c2ae42c3692151720a9fa24f3b173d683d13ab0cuser_id6534636625295881710";
		String token = "446d1cabb1e5ec6a0691186af6a8e9067cb2a089c058b9b2eee5a25ad9e000ffa8da5e67188b78c8b3295956448d49401e24968460dae04435af9b4f1718057a6b36ecf91f75760c69204824b21ad88ef0d192f0abb0128fca27bcfcfcb6734424b7c3ef72683ed25d54c634c2ae42c3692151720a9fa24f3b173d683d13ab0c";
		rawStrBeforeSign = getStrForSign(method, url, json, token, "0578aac9l733cg41b4fb1fdsfad978e0a6fe", "a88a6d17-96d7-4273-8");
		
		String signed = MD5Util.encode(rawStrBeforeSign);
		System.out.println(signed);
		System.out.println("<14234234>".replace("<", "").replace(">", ""));
		@SuppressWarnings("unused")
		BigDecimal data = new BigDecimal(1.23);
	}
}
