package com.aipa.svc.v1.param;

import javax.servlet.http.HttpServletRequest;

import com.aipa.svc.common.util.ParameterTool;
import com.aipa.svc.common.util.RequestExtract;

public class UserParamExtract extends RequestExtract{

	
	
	private static final String REQUEST_PARAM_USERNAME = "username";
	private static final String REQUEST_PARAM_PWD = "pwd";
	private static final String REQUEST_PARAM_UID = "uid";
	
	private static final String REQUEST_PARAM_NOTE_ID = "note_id";
	
	private static final String REQUEST_PARAM_COLLECT_ID = "collect_id";
	
	private static final String REQUEST_PARAM_CATEGORY_ID = "category_id";
	
	private static final String REQUEST_PARAM_INTETEST_ID ="interest_id";
	
	
	public static String getUserName(HttpServletRequest request){
		return ParameterTool.getParameterString(request, REQUEST_PARAM_USERNAME);
	}
	public static String getPwd(HttpServletRequest request){
		return ParameterTool.getParameterString(request, REQUEST_PARAM_PWD);
	}
	
	public static Long getUid(HttpServletRequest request){
		return ParameterTool.getParameterLong(request, REQUEST_PARAM_UID);
	}
	
	public static Long getNoteId(HttpServletRequest request){
		return ParameterTool.getParameterLong(request, REQUEST_PARAM_NOTE_ID);
	}
	
	public static Long getCollectId(HttpServletRequest request){
		return ParameterTool.getParameterLong(request, REQUEST_PARAM_COLLECT_ID);
	}
	
	public static Long getCategoryId(HttpServletRequest request){
		return ParameterTool.getParameterLong(request, REQUEST_PARAM_CATEGORY_ID);
	}
	
	public static Long getInterestId(HttpServletRequest request){
		return ParameterTool.getParameterLong(request, REQUEST_PARAM_INTETEST_ID);
	}
	
}
