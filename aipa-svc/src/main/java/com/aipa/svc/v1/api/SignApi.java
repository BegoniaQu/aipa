package com.aipa.svc.v1.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aipa.svc.common.util.Appplt;
import com.aipa.svc.common.util.ExceptionHandler;
import com.aipa.svc.common.util.ParameterTool;
import com.aipa.svc.common.util.RequestExtract;
import com.aipa.svc.common.util.Result;
import com.aipa.svc.common.util.ResultCode;
import com.aipa.svc.common.vo.Token;
import com.aipa.svc.v1.manager.UserManager;
import com.aipa.user.module.entity.User;

@Controller
@RequestMapping("sign")
public class SignApi {
	
	private static final Logger log = LoggerFactory.getLogger("SignApi");
	
	@Resource
	private UserManager userManager;
	
	@ResponseBody
	@RequestMapping(value="signIn.ap",method = RequestMethod.POST)
	public Object signIn(@RequestParam("username") String username,@RequestParam("pwd") String password,HttpServletRequest request){
		try {
			String appver = RequestExtract.getAppver(request);
			String ip = ParameterTool.getIpAddr(request);
			Appplt appplt = RequestExtract.getAppplt(request);
			
			log.info("username ={} login,appver={},appplt={},ip={},verify user",new Object[]{username,appver,appplt,ip});
			String token = this.userManager.getToken(username, password);
			if(token == null){
				log.warn("user: {} login failed",new Object[]{username});
				return new Result(null, ResultCode.AuthenticationFailure.getCode(),ResultCode.AuthenticationFailure.getMsg());
			}else{
				log.info("user: {} login success",new Object[]{username});
				return new Result(new Token(token),ResultCode.OK.getCode(),ResultCode.OK.getMsg());
			}
		} catch (Exception e) {
			return ExceptionHandler.handler(e);
		}
	}
	
	@ResponseBody
	@RequestMapping(value="signUp.ap",method = RequestMethod.POST)
	public Object signUp(@RequestParam("username") String username,@RequestParam("pwd") String password,HttpServletRequest request){
		try {
			String appver = RequestExtract.getAppver(request);
			String ip = ParameterTool.getIpAddr(request);
			Appplt appplt = RequestExtract.getAppplt(request);
			
			log.info("username ={} reg,appver={},appplt={},ip={}",new Object[]{username,appver,appplt,ip});
			User user = this.userManager.getByUsername(username);
			if(user != null){
				return new Result(null, ResultCode.ExistedUser.getCode(),ResultCode.ExistedUser.getMsg());
			}
			this.userManager.reg(username, password);
			return new Result(null,ResultCode.OK.getCode(),ResultCode.OK.getMsg());
		} catch (Exception e) {
			return ExceptionHandler.handler(e);
		}
	}
	
	
}
