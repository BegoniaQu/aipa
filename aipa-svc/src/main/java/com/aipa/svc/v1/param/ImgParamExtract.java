package com.aipa.svc.v1.param;

import javax.servlet.http.HttpServletRequest;

import com.aipa.svc.common.util.ParameterTool;
import com.aipa.svc.common.util.RequestExtract;

public class ImgParamExtract  extends RequestExtract{
	
	private static final String REQUEST_PARAM_IMG ="pic_name";
	
	private static final String REQUEST_PARAM_IMG_TYPE = "imgBusiType";
	
	public static String getImgName(HttpServletRequest request){
		return ParameterTool.getParameterString(request, REQUEST_PARAM_IMG);
	}
	public static int getImgBusiType(HttpServletRequest request){
		return ParameterTool.getParameterInteger(request, REQUEST_PARAM_IMG_TYPE,-1);
	}
}
