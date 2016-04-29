package com.aipa.svc.v1.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aipa.svc.common.util.AipaTokenUtil;
import com.aipa.svc.common.util.Appplt;
import com.aipa.svc.common.util.ExceptionHandler;
import com.aipa.svc.common.util.ParameterTool;
import com.aipa.svc.common.util.RequestExtract;
import com.aipa.svc.common.util.Result;
import com.aipa.svc.common.util.ResultCode;
import com.aipa.svc.v1.manager.UserManager;
import com.aipa.user.module.entity.User;
import com.aipa.user.module.service.UserService;

@Controller
@RequestMapping("user")
public class UserApi extends BaseApi{

	private static final Logger log = LoggerFactory.getLogger("UserApi");
	
	@Resource
	private UserManager userManager;
	
	@ResponseBody
	@RequestMapping(value = "info",method = RequestMethod.GET)
	public Object getUserInfo(HttpServletRequest request){
		try {
			String token = RequestExtract.getTk(request);
			if(token == null){
				return new Result(null,ResultCode.AuthenticationError.getCode(),ResultCode.AuthenticationError.getMsg());
			}
			String str = AipaTokenUtil.verifyToken(token);
			if(str == null){
				return new Result(null,ResultCode.AuthenticationError.getCode(),ResultCode.AuthenticationError.getMsg());
			}
			Long uid = Long.parseLong(str);
			
			String appver = RequestExtract.getAppver(request);
			String ip = ParameterTool.getIpAddr(request);
			Appplt appplt = RequestExtract.getAppplt(request);
			//token expired?
			boolean expired = checkTokenExpired(uid);
			if(expired){
				return new Result(null,ResultCode.AuthenticationExpired.getCode(),ResultCode.AuthenticationExpired.getMsg());
			}
			log.info("uid={},modify,appver={},appplt={},ip={},verify user",new Object[]{uid,appver,appplt,ip});
			User user = this.userManager.getById(uid);
			
			//TODO
			return null;
		} catch (Exception e) {
			return ExceptionHandler.handler(e);
		}
		
	}

	
	
	
	
	
	@Override
	public UserService getUserService() {
		return this.userManager.getUserService();
	}

	
	
}
