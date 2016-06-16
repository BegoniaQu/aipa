package com.aipa.svc.v1.api;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.aipa.svc.common.util.ResultCode;
import com.aipa.svc.common.vo.OtherUserInfoBean;
import com.aipa.svc.common.vo.UserInfoBean;
import com.aipa.svc.common.vo.UserInfoParam;
import com.aipa.svc.v1.manager.UserManager;
import com.aipa.svc.v1.param.LenConstant;
import com.aipa.svc.v1.param.UserParamExtract;
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
	
	@RequestMapping(value = "{uid}/scan.ap",method = RequestMethod.GET)
	public Object scanUserInfo(@PathVariable Long uid, HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//Long id = UserParamExtract.getUid(request);
		log.info("scan uid={} info,appver={},appplt={},ip={}",new Object[]{uid,appver,appplt,ip});
		OtherUserInfoBean bean = this.userManager.getOtherById(uid);
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
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		//参数object
		UserInfoParam uip = JsonUtil.jsonConvert(request, UserInfoParam.class);
		uip.setUid(uid);
		//verify params
		if(uip.getLocation() != null){
			if(uip.getLocation().length() > LenConstant.locationLen){
				throw new InvalidRequestRuntimeException("所在地长度不合法");
			}
		}
		if(uip.getMarital_status() != null){
			if(Marital.get(uip.getMarital_status()) == null){
				throw new InvalidRequestRuntimeException("marital not right");
			}
		}
		if(uip.getNickname() != null){
			if(uip.getNickname().length() > LenConstant.nicknameLen){
				throw new InvalidRequestRuntimeException("昵称长度不合法");
			}
		}
		if(uip.getSex() != null){
			if(Sex.get(uip.getSex()) == null){
				throw new InvalidRequestRuntimeException("sex not right");
			}
		}
		if(uip.getSex_orient() != null){
			if(SexOrient.get(uip.getSex_orient()) == null){
				throw new InvalidRequestRuntimeException("sexorient not right");
			}
		}
		log.info("update uid={} info,appver={},appplt={},ip={},body={}",uid,appver,appplt,ip,JsonUtil.getJsonFromObject(uip));
		//call service
		this.userManager.updateUser(uip);
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
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
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
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		Long noteId = UserParamExtract.getNoteId(request);
		log.info("collect note,noteId={},uid={},appver={},appplt={},ip={}",new Object[]{uid,noteId,appver,appplt,ip});
		this.userManager.collectNote(uid, noteId);
		return "";
	}
	
	@RequestMapping(value = "collectedNote/{id}/info.ap",method = RequestMethod.DELETE)
	public Object removeCollectedNote(@PathVariable Long id, HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		//Long collectId = UserParamExtract.getCollectId(request);
		log.info("remove collected note, collectId = {},uid={},appver={},appplt={},ip={}",new Object[]{id,uid,appver,appplt,ip});
		//call service
		this.userManager.removeCollectedNote(uid,id);
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
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
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
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		Long categoryId = UserParamExtract.getCategoryId(request);
		log.info("add interested category,categoryId={},uid={},appver={},appplt={},ip={}",new Object[]{categoryId,uid,appver,appplt,ip});
		this.userManager.addInsterestCategory(uid, categoryId);
		return "";
	}
	
	@RequestMapping(value = "interestedCat/{id}/info.ap",method = RequestMethod.DELETE)
	public Object removeInterestedCategory(@PathVariable Long id, HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		//Long interestId = UserParamExtract.getInterestId(request);
		log.info("remove interested category, interestId ={},uid={},appver={},appplt={},ip={}",new Object[]{id,uid,appver,appplt,ip});
		this.userManager.removeInsterestCategory(id);
		return "";
	}
	
	@Override
	public UserService getUserService() {
		return this.userManager.getUserService();
	}
	
	
}
