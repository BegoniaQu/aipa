package com.aipa.svc.v1.manager;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.aipa.svc.common.config.ThirdConfig;
import com.aipa.svc.common.enume.ImgBusiType;
import com.aipa.svc.common.vo.ImgToken;
import com.aipa.user.module.service.UserService;
import com.qiniu.util.Auth;

@Component
public class QiniuManager {
	
	@Resource
	private UserService userService;
	
	
	public ImgToken reqToken(String picName,int imgBusiType,Long uid){
		String extensionName = picName.substring(picName.lastIndexOf(".") + 1); 	// 获取图片的扩展名
		//拼接图片路径
		StringBuilder sb = new StringBuilder();
		sb.append(uid);
		sb.append("_").append(System.currentTimeMillis());
		sb.append("." + extensionName);
		String key = ImgBusiType.get(imgBusiType).name()+File.separator+sb.toString();
		Auth auth = Auth.create(ThirdConfig.QINIU_AK,ThirdConfig.QINIU_SK);
		String token = auth.uploadToken(ThirdConfig.QINIU_BUCKET, key,ThirdConfig.QINIU_TOKEN_EXPIRED,null);
		ImgToken obj = new ImgToken();
		obj.setPath(sb.toString());
		obj.setTk(token);
		obj.setDomain(ThirdConfig.domain);
		return obj;
	}
	
	
	public UserService getUserService() {
		return userService;
	}
}
