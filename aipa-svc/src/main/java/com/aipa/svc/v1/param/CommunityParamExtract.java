package com.aipa.svc.v1.param;

import javax.servlet.http.HttpServletRequest;

import com.aipa.svc.common.util.ParameterTool;
import com.aipa.svc.common.util.RequestExtract;

public class CommunityParamExtract extends RequestExtract{

	private static final String CATEGORY_NAME = "category_name";
	private static final String CATEGORY_DESCR = "descr";
	private static final String PARENT_ID = "parent_id";
	
	public static final String getCategoryName(HttpServletRequest request){
		return ParameterTool.getParameterString(request, CATEGORY_NAME);
	}
	
	public static final String getCategoryDescr(HttpServletRequest request){
		return ParameterTool.getParameterString(request, CATEGORY_DESCR, null);
	}
	
	public static final Long getParentId(HttpServletRequest request){
		return ParameterTool.getParameterLong(request, PARENT_ID, null);
	}
}
