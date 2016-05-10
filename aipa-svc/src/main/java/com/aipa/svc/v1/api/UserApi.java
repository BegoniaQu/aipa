package com.aipa.svc.v1.api;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aipa.svc.common.enume.Marital;
import com.aipa.svc.common.enume.Sex;
import com.aipa.svc.common.enume.SexOrient;
import com.aipa.svc.common.exception.InvalidRequestRuntimeException;
import com.aipa.svc.common.util.Appplt;
import com.aipa.svc.common.util.JsonUtil;
import com.aipa.svc.common.util.ParameterTool;
import com.aipa.svc.common.util.RequestExtract;
import com.aipa.svc.common.util.Result;
import com.aipa.svc.common.util.ResultCode;
import com.aipa.svc.common.vo.UserInfoBean;
import com.aipa.svc.common.vo.UserInfoParam;
import com.aipa.svc.v1.manager.UserManager;
import com.aipa.svc.v1.param.LenConstant;
import com.aipa.svc.v1.param.UserParamExtract;
import com.aipa.user.module.entity.User;
import com.aipa.user.module.service.UserService;

@Controller
@RequestMapping("user")
public class UserApi extends BaseApi{

	private static final Logger log = LoggerFactory.getLogger("UserApi");
	
	@Resource
	private UserManager userManager;
	
	
	@RequestMapping(value = "info.ap",method = RequestMethod.GET)
	public Object getUserInfo(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		log.info("get uid={} info,appver={},appplt={},ip={}",new Object[]{uid,appver,appplt,ip});
		UserInfoBean bean = this.userManager.getById(uid);
		return bean;
	}
	
	@RequestMapping(value = "scan.ap",method = RequestMethod.GET)
	public Object scanUserInfo(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		Long id = UserParamExtract.getUid(request);
		log.info("scan uid={} info,appver={},appplt={},ip={}",new Object[]{id,appver,appplt,ip});
		UserInfoBean bean = this.userManager.getOtherById(id);
		return bean;
	}
	
	@RequestMapping(value = "info.ap",method = RequestMethod.POST)
	public Object modifyUserInfo(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			return new Result(null,ResultCode.AuthenticationExpired.getCode(),ResultCode.AuthenticationExpired.getMsg());
		}
		log.info("update uid={} info,appver={},appplt={},ip={}",new Object[]{uid,appver,appplt,ip});
		//参数object
		UserInfoParam uip = JsonUtil.jsonConvert(request, UserInfoParam.class);
		//compose
		User user = this.userManager.getUser(uid);
		//verify params
		if(uip.getAge() != null){
			Short ageShort = Short.parseShort(String.valueOf(uip.getAge()));
			user.setAge(ageShort);
		}
		if(uip.getAge_switch() != null){
			user.setAge_switch(uip.getAge_switch());
		}
		if(uip.getHead_picture() != null){
			user.setHead_picture(uip.getHead_picture()); //TODO 需要截取,以及是否是七牛的图片
		}
		if(uip.getLocation() != null){
			if(uip.getLocation().length() > LenConstant.locationLen){
				throw new InvalidRequestRuntimeException("所在地长度不合法");
			}
			user.setLocation(uip.getLocation());
		}
		if(uip.getMarital_status() != null){
			if(Marital.get(uip.getMarital_status()) == null){
				throw new InvalidRequestRuntimeException("marital not right");
			}
			Short ms = Short.parseShort(String.valueOf(uip.getMarital_status())); 
			user.setMarital_status(ms);
		}
		if(uip.getMarital_status_switch() != null){
			user.setMarital_status_switch(uip.getMarital_status_switch() );
		}
		if(uip.getNickname() != null){
			if(uip.getNickname().length() > LenConstant.nicknameLen){
				throw new InvalidRequestRuntimeException("昵称长度不合法");
			}
			user.setNickname(uip.getNickname());
			
		}
		if(uip.getSex() != null){
			if(Sex.get(uip.getSex()) == null){
				throw new InvalidRequestRuntimeException("sex not right");
			}
			Short sexShort = Short.parseShort(String.valueOf(uip.getSex())); 
			user.setSex(sexShort);
		}
		if(uip.getSex_switch() != null){
			user.setSex_switch(uip.getSex_switch());
		}
		if(uip.getSex_orient() != null){
			if(SexOrient.get(uip.getSex_orient()) == null){
				throw new InvalidRequestRuntimeException("sexorient not right");
			}
			Short so = Short.parseShort(String.valueOf(uip.getSex_orient())); 
			user.setSex_orient(so);
		}
		if(uip.getSex_orient_switch() != null){
			user.setSex_orient_switch(uip.getSex_orient_switch());
		}
		user.setUpdate_time(new Date());
		this.userManager.updateUser(user);
		return "";
	}
	
	
	@RequestMapping(value = "collectedNote/list.ap",method = RequestMethod.GET)
	public Object findCollectedNotePage(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		int ps = RequestExtract.getPageSize(request);
		int pn = RequestExtract.getPageNum(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			return new Result(null,ResultCode.AuthenticationExpired.getCode(),ResultCode.AuthenticationExpired.getMsg());
		}
		log.info("find collected notes, uid={},appver={},appplt={},ip={}",new Object[]{uid,appver,appplt,ip});
		return this.userManager.findCollectedNotePage(uid, ps, pn);
	}
	
	
	@RequestMapping(value = "collectedNote/info.ap",method = RequestMethod.POST)
	public Object collectNote(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			return new Result(null,ResultCode.AuthenticationExpired.getCode(),ResultCode.AuthenticationExpired.getMsg());
		}
		Long noteId = UserParamExtract.getNoteId(request);
		log.info("collect note,noteId={},uid={},appver={},appplt={},ip={}",new Object[]{uid,noteId,appver,appplt,ip});
		this.userManager.collectNote(uid, noteId);
		return "";
	}
	
	@RequestMapping(value = "collectedNote/cancel.ap",method = RequestMethod.POST)
	public Object removeCollectedNote(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			return new Result(null,ResultCode.AuthenticationExpired.getCode(),ResultCode.AuthenticationExpired.getMsg());
		}
		Long collectId = UserParamExtract.getCollectId(request);
		log.info("remove collected note, collectId = {},uid={},appver={},appplt={},ip={}",new Object[]{collectId,uid,appver,appplt,ip});
		this.userManager.removeCollectedNote(collectId);
		return "";
	}
	
	@RequestMapping(value = "interestedCat/list.ap",method = RequestMethod.GET)
	public Object findInterestedCategory(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			return new Result(null,ResultCode.AuthenticationExpired.getCode(),ResultCode.AuthenticationExpired.getMsg());
		}
		log.info("find interested categories,uid={},appver={},appplt={},ip={}",new Object[]{uid,appver,appplt,ip});
		return this.userManager.findInterestedCategory(uid);
	}
	
	
	@RequestMapping(value = "interestedCat/info.ap",method = RequestMethod.POST)
	public Object addInterestedCategory(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			return new Result(null,ResultCode.AuthenticationExpired.getCode(),ResultCode.AuthenticationExpired.getMsg());
		}
		Long categoryId = UserParamExtract.getCategoryId(request);
		log.info("add interested category,categoryId={},uid={},appver={},appplt={},ip={}",new Object[]{categoryId,uid,appver,appplt,ip});
		this.userManager.addInsterestCategory(uid, categoryId);
		return "";
	}
	
	@RequestMapping(value = "interestedCat/cancel.ap",method = RequestMethod.POST)
	public Object removeInterestedCategory(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			return new Result(null,ResultCode.AuthenticationExpired.getCode(),ResultCode.AuthenticationExpired.getMsg());
		}
		Long interestId = UserParamExtract.getInterestId(request);
		log.info("remove interested category, interestId ={},uid={},appver={},appplt={},ip={}",new Object[]{interestId,uid,appver,appplt,ip});
		this.userManager.removeInsterestCategory(interestId);
		return "";
	}
	
	
	@Override
	public UserService getUserService() {
		return this.userManager.getUserService();
	}
	
	
}
