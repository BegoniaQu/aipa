package com.aipa.svc.v1.param;

import javax.servlet.http.HttpServletRequest;

import com.aipa.svc.common.util.ParameterTool;
import com.aipa.svc.common.util.RequestExtract;

public class CommunityParamExtract extends RequestExtract{

	private static final String CATEGORY_NAME = "category_name";
	private static final String CATEGORY_DESCR = "descr";
	private static final String PARENT_ID = "parent_id";
	
	private static final String REQUEST_PARAM_CATEGORY_ID = "cat_id";
	
	private static final String REQUEST_PARAM_NOTE_ID = "note_id";
	private static final String REQUEST_PARAM_COMMENT_ID = "comment_id";
	
	private static final String REQUEST_PARAM_ZAN_ID = "zan_id";
	
	public static final String getCategoryName(HttpServletRequest request){
		return ParameterTool.getParameterString(request, CATEGORY_NAME);
	}
	
	public static final String getCategoryDescr(HttpServletRequest request){
		return ParameterTool.getParameterString(request, CATEGORY_DESCR, null);
	}
	
	public static final Long getParentId(HttpServletRequest request){
		return ParameterTool.getParameterLong(request, PARENT_ID, null);
	}
	
	public static final Long getNoteId(HttpServletRequest request){
		return ParameterTool.getParameterLong(request, REQUEST_PARAM_NOTE_ID);
	}
	
	public static final Long getCommentId(HttpServletRequest request){
		return ParameterTool.getParameterLong(request, REQUEST_PARAM_COMMENT_ID);
	}
	
	public static Long getCategoryId(HttpServletRequest request){
		return ParameterTool.getParameterLong(request, REQUEST_PARAM_CATEGORY_ID);
	}
	
	public static Long getZanId(HttpServletRequest request){
		return ParameterTool.getParameterLong(request, REQUEST_PARAM_ZAN_ID);
	}
}
