package com.aipa.svc.v1.api;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aipa.svc.common.util.ResultBean;
import com.aipa.svc.common.util.ResultCode;
import com.aipa.svc.v1.manager.UserManager;

@Controller
@RequestMapping("sign")
public class SignApi {
	
	@Resource
	private UserManager userManager;
	
	@ResponseBody
	@RequestMapping(value="signIn.ap",method = RequestMethod.POST)
	public Object signIn(@RequestParam("username") String username,@RequestParam("pwd") String password){
		
		String token = this.userManager.getToken(username, password);
		if(token == null){
			return new ResultBean(null, ResultCode.AuthenticationFailure.getCode(),ResultCode.AuthenticationFailure.getMsg());
		}else{
			return new ResultBean(token,ResultCode.OK.getCode());
		}
	}
	
}
