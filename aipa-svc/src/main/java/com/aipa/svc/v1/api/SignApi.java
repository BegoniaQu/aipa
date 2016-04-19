package com.aipa.svc.v1.api;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aipa.svc.v1.manager.SignManager;

@Controller
@RequestMapping("aipa/sign")
public class SignApi {

	@Resource
	private SignManager signManager;
	
}
