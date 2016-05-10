package com.aipa.svc.v1.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aipa.svc.common.exception.InvalidRequestRuntimeException;
import com.aipa.svc.common.util.Appplt;
import com.aipa.svc.common.util.ParameterTool;
import com.aipa.svc.common.util.RequestExtract;
import com.aipa.svc.common.util.ResultCode;
import com.aipa.svc.common.vo.Token;
import com.aipa.svc.v1.manager.UserManager;
import com.aipa.svc.v1.param.LenConstant;
import com.aipa.svc.v1.param.UserParamExtract;
import com.aipa.user.module.entity.User;

@Controller
@RequestMapping("sign")
public class SignApi {
	
	private static final Logger log = LoggerFactory.getLogger("SignApi");
	
	@Resource
	private UserManager userManager;
	
	
	@RequestMapping(value="signIn.ap",method = RequestMethod.POST)
	public Object signIn(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		String username = UserParamExtract.getUserName(request);
		String password = UserParamExtract.getPwd(request);
		log.info("sign in,username={},appver={},appplt={},ip={}",new Object[]{username,appver,appplt,ip});
		String token = this.userManager.getToken(username, password);
		if(token == null){
			log.warn("sign in,username={} login failed",new Object[]{username});
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationFailure.getMsg(),ResultCode.AuthenticationFailure.getCode());
		}else{
			log.info("sign in,username={} login success",new Object[]{username});
			return new Token(token);
		}
	}
	
	@RequestMapping(value="signUp.ap",method = RequestMethod.POST)
	public Object signUp(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		String username = UserParamExtract.getUserName(request);
		String password = UserParamExtract.getPwd(request);
		log.info("sign up,appver={},appplt={},ip={}",new Object[]{appver,appplt,ip});
		if(username.length() > LenConstant.usernameLen){
			throw new InvalidRequestRuntimeException("账户长度不合法");
		}
		if(password.length() < LenConstant.pwdMinLen ||password.length() > LenConstant.pwdMaxLen){
			throw new InvalidRequestRuntimeException("密码长度不合法");
		}
		User user = this.userManager.getByUsername(username);
		if(user != null){
			log.info("sign up,username={} existed",new Object[]{username});
			throw new InvalidRequestRuntimeException(ResultCode.ExistedUser.getMsg(), ResultCode.ExistedUser.getCode());
		}
		user = this.userManager.reg(username, password);
		log.info("sign up,username={} reg ok,uid = {}",new Object[]{username,user.getId()});
		return "";
	}
	
	
}
