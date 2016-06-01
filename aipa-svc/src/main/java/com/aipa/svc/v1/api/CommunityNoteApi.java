package com.aipa.svc.v1.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aipa.svc.common.exception.InvalidRequestRuntimeException;
import com.aipa.svc.common.util.Appplt;
import com.aipa.svc.common.util.JsonUtil;
import com.aipa.svc.common.util.ParameterTool;
import com.aipa.svc.common.util.RequestExtract;
import com.aipa.svc.common.util.ResultCode;
import com.aipa.svc.common.vo.CommunityCommentBean;
import com.aipa.svc.common.vo.CommunityNoteAddBean;
import com.aipa.svc.common.vo.CommunityReplyBean;
import com.aipa.svc.common.vo.GoodID;
import com.aipa.svc.v1.manager.CommunityNoteManager;
import com.aipa.svc.v1.param.CommunityParamExtract;
import com.aipa.user.module.service.UserService;

@Controller
@RequestMapping("community/note")
public class CommunityNoteApi extends BaseApi{

	private static final Logger log = LoggerFactory.getLogger("CommunityNoteApi");
	
	@Resource
	private CommunityNoteManager communityNoteManager;
	
	@RequestMapping(value="pub.ap",method = RequestMethod.POST)
	public Object pubNote(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		//
		CommunityNoteAddBean note = JsonUtil.jsonConvert(request, CommunityNoteAddBean.class);
		if(note == null||note.getCategory_id() == null||StringUtils.isEmpty(note.getTitle())){
			throw new InvalidRequestRuntimeException(ResultCode.ParameterError.getMsg(),ResultCode.ParameterError.getCode());
		}
		//要么图片，要么文字
		if(StringUtils.isEmpty(note.getContent())&&note.getPic_list() == null){
			throw new InvalidRequestRuntimeException(ResultCode.ParameterError.getMsg(),ResultCode.ParameterError.getCode());
		}
		note.setUser_id(uid);
		log.info("public note,uid={},appver={},appplt={},ip={},body={}",new Object[]{uid,appver,appplt,ip,JsonUtil.getJsonFromObject(note)});
		//call service
		this.communityNoteManager.addNote(note);
		return "";
	}
	
	@RequestMapping(value="{noteId}/info.ap",method = RequestMethod.DELETE)
	public Object removeNote(@PathVariable Long noteId,HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		log.info("remove note,uid={},noteId={},appver={},appplt={},ip={}",new Object[]{uid,noteId,appver,appplt,ip});
		//call service
		this.communityNoteManager.removeNote(uid, noteId);
		
		
		return "";
	}
	
	@RequestMapping(value="comment.ap",method = RequestMethod.POST)
	public Object commentNote(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		CommunityCommentBean bean = JsonUtil.jsonConvert(request, CommunityCommentBean.class);
		//check params
		if(StringUtils.isEmpty(bean.getContent())||bean.getNote_id() == null){
			throw new InvalidRequestRuntimeException(ResultCode.ParameterError.getMsg(),ResultCode.ParameterError.getCode());
		}
		log.info("comment note,uid={},appver={},appplt={},ip={},body={}",uid,appver,appplt,ip,bean.toString());
		//帖子不存在或者已删除
		if(!this.communityNoteManager.isNoteExisted(bean.getNote_id())){
			throw new InvalidRequestRuntimeException(ResultCode.ParameterError.getMsg(),ResultCode.ParameterError.getCode());
		}
		bean.setComment_user_id(uid);
		//call service
		this.communityNoteManager.commentNote(bean);
		return "";
	}
	
	@RequestMapping(value="comment/reply.ap",method = RequestMethod.POST)
	public Object replyComment(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		CommunityReplyBean bean = JsonUtil.jsonConvert(request, CommunityReplyBean.class);
		//check params
		if(bean.getComment_id() == null||bean.getNote_id() == null ||bean.getRecord_id() == null||StringUtils.isEmpty(bean.getContent())){
			throw new InvalidRequestRuntimeException(ResultCode.ParameterError.getMsg(),ResultCode.ParameterError.getCode());
		}
		log.info("reply comment,uid={},appver={},appplt={},ip={},body={}",uid,appver,appplt,ip,bean.toString());
		//帖子不存在或者已删除
		if(!this.communityNoteManager.isNoteExisted(bean.getNote_id())){
			throw new InvalidRequestRuntimeException(ResultCode.ParameterError.getMsg(),ResultCode.ParameterError.getCode());
		}
		//评论不存在或者已删除
		if(!this.communityNoteManager.isRecordExisted(bean.getRecord_id())){
			throw new InvalidRequestRuntimeException(ResultCode.ParameterError.getMsg(),ResultCode.ParameterError.getCode());
		}
		bean.setReply_user_id(uid);
		this.communityNoteManager.replyComment(bean);
		return "";
	}
	
	@RequestMapping(value="{id}/comment/info.ap",method = RequestMethod.DELETE)
	public Object removComment(@PathVariable Long id,HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		log.info("delete comment or reply,uid={},appver={},appplt={},ip={},id={}",uid,appver,appplt,ip,id);
		this.communityNoteManager.removeCommentOrReply(id);
		return "";
	}
	
	
	@RequestMapping(value="zan.ap",method = RequestMethod.POST)
	public Object clickGoodForNote(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		Long noteId = CommunityParamExtract.getNoteId(request);
		log.info("click good for note,uid={},appver={},appplt={},ip={},noteId={}",uid,appver,appplt,ip,noteId);
		//check params
		if(!this.communityNoteManager.isNoteExisted(noteId)){
			throw new InvalidRequestRuntimeException(ResultCode.ParameterError.getMsg(),ResultCode.ParameterError.getCode());
		}
		Long goodId = this.communityNoteManager.clickGoodForNote(noteId, uid);
		return new GoodID(goodId);
	}
	
	@RequestMapping(value="unzan.ap",method = RequestMethod.DELETE)
	public Object cancelGoodForNote(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//token expired?
		Long uid = RequestExtract.getUID(request);
		boolean expired = checkTokenExpired(uid);
		if(expired){
			throw new InvalidRequestRuntimeException(ResultCode.AuthenticationExpired.getMsg(),ResultCode.AuthenticationExpired.getCode());
		}
		Long noteId = CommunityParamExtract.getNoteId(request);
		Long goodId = CommunityParamExtract.getZanId(request);
		log.info("cancel good for note,uid={},appver={},appplt={},ip={},noteId={},goodId={}",uid,appver,appplt,ip,noteId,goodId);
		//check params
		if(!this.communityNoteManager.islegalForNote(noteId, uid, goodId)){
			throw new InvalidRequestRuntimeException(ResultCode.ParameterError.getMsg(),ResultCode.ParameterError.getCode());
		}
		this.communityNoteManager.cancelGoodForNote(noteId, uid, goodId);
		return "";
	}
	
	
//	@RequestMapping(value="comment/zan.ap",method = RequestMethod.POST)
//	public Object clickGoodForComment(HttpServletRequest request){
//		
//		return "";
//	}
	
	@RequestMapping(value="list.ap",method = RequestMethod.GET)
	public Object findNoteList(HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		//
		Long catId = CommunityParamExtract.getCategoryId(request);
		int pn = RequestExtract.getPageNum(request);
		int ps = RequestExtract.getPageSize(request);
		log.info("note list,appver={},appplt={},ip={},catId={},pn={},ps={}",appver,appplt,ip,catId,pn,ps);
		return this.communityNoteManager.findNote(catId, ps, pn);
	}
	
	@RequestMapping(value="{noteId}/detail.ap",method = RequestMethod.GET)
	public Object getNoteDetail(@PathVariable Long noteId, HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		log.info("note detail,noteId={},appver={},appplt={},ip={}",noteId,appver,appplt,ip);
		
		return this.communityNoteManager.findNoteDetail(noteId);
	}
	
	
	@RequestMapping(value="{noteId}/comment/list.ap",method = RequestMethod.GET)
	public Object findComment(@PathVariable Long noteId,HttpServletRequest request){
		String appver = RequestExtract.getAppver(request);
		String ip = ParameterTool.getIpAddr(request);
		Appplt appplt = RequestExtract.getAppplt(request);
		int pn = RequestExtract.getPageNum(request);
		int ps = RequestExtract.getPageSize(request);
		log.info("note comment list,noteId={},appver={},appplt={},ip={},catId={},pn={},ps={}",noteId,appver,appplt,ip,pn,ps);
		return this.communityNoteManager.findComment(noteId, ps, pn);
	}
	
	
	
	@RequestMapping(value="{noteId}/report.ap",method = RequestMethod.POST)
	public Object report(@PathVariable Long noteId,HttpServletRequest request){
		
		return "";
	}
	
	
	
	@Override
	public UserService getUserService() {
		return this.communityNoteManager.getUserService();
	}
	
	
	
	
}
