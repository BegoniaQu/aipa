package com.aipa.svc.v1.api;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aipa.svc.common.enume.ImgBusiType;
import com.aipa.svc.common.exception.InvalidRequestRuntimeException;
import com.aipa.svc.common.util.Appplt;
import com.aipa.svc.common.util.ParameterTool;
import com.aipa.svc.common.util.ResultCode;
import com.aipa.svc.v1.manager.QiniuManager;
import com.aipa.svc.v1.param.ImgParamExtract;
import com.aipa.user.module.service.UserService;

@Controller
@RequestMapping("img")
public class QiniuApi extends BaseApi{
	
	private static final Logger log = LoggerFactory.getLogger("QiniuApi");
	
	@Resource
	private QiniuManager qiniuManager;
	
	
	@RequestMapping(value="tk.ap",method = RequestMethod.GET)
	public Object getToken(HttpServletRequest request){
		String appver = ImgParamExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = ImgParamExtract.getAppplt(request);
		//token expired?
		Long uid = ImgParamExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		//img
		String picName = ImgParamExtract.getImgName(request);
		int imgBusiType = ImgParamExtract.getImgBusiType(request);
		if(ImgBusiType.get(imgBusiType) == null){
			throw new InvalidRequestRuntimeException(ResultCode.ParameterError.getMsg(),ResultCode.ParameterError.getCode());
		}
		log.info("get img token,picName={},imgBusiType={},uid={},appver={},appplt={},ip={}",new Object[]{picName,imgBusiType,uid,appver,appplt,ip});
		//call service
		return this.qiniuManager.reqToken(picName,imgBusiType,uid);
		
	}
	

	@Override
	public UserService getUserService() {
		return qiniuManager.getUserService();
	}

	
	
}
