package com.aipa.svc.v1.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.aipa.community.module.entity.CommunityNote;
import com.aipa.community.module.entity.CommunityNoteComment;
import com.aipa.community.module.entity.CommunityNoteCommentIndex;
import com.aipa.community.module.entity.CommunityNoteCommentIndexType;
import com.aipa.community.module.entity.CommunityNoteCounter;
import com.aipa.community.module.entity.CommunityNoteIndex;
import com.aipa.community.module.entity.CommunityNoteIndexType;
import com.aipa.community.module.entity.GoodClick;
import com.aipa.community.module.entity.GoodClickIndex;
import com.aipa.community.module.entity.GoodClickIndexType;
import com.aipa.community.module.service.CommunityNoteCommentIndexService;
import com.aipa.community.module.service.CommunityNoteCommentService;
import com.aipa.community.module.service.CommunityNoteCounterService;
import com.aipa.community.module.service.CommunityNoteIndexService;
import com.aipa.community.module.service.CommunityNoteService;
import com.aipa.community.module.service.GoodClickIndexService;
import com.aipa.community.module.service.GoodClickService;
import com.aipa.svc.common.enume.ZanType;
import com.aipa.svc.common.util.DateUtils;
import com.aipa.svc.common.util.PagedListRespData;
import com.aipa.svc.common.vo.CommunityCommentBean;
import com.aipa.svc.common.vo.CommunityCommentDetailBean;
import com.aipa.svc.common.vo.CommunityNoteAddBean;
import com.aipa.svc.common.vo.CommunityNoteBean;
import com.aipa.svc.common.vo.CommunityNoteDetailBean;
import com.aipa.svc.common.vo.CommunityReplyBean;
import com.aipa.svc.common.vo.Reply;
import com.aipa.svc.v1.param.LenConstant;
import com.aipa.user.module.entity.User;
import com.aipa.user.module.entity.UserNoteCollect;
import com.aipa.user.module.service.UserNoteCollectService;
import com.aipa.user.module.service.UserService;

@Component
public class CommunityNoteManager {

	@Resource
	private CommunityNoteService communityNoteService;
	
	@Resource
	private CommunityNoteIndexService communityNoteIndexService;
	
	@Resource
	private CommunityNoteCommentService communityNoteCommentService;
	
	@Resource
	private CommunityNoteCommentIndexService communityNoteCommentIndexService;
	
	@Resource
	private GoodClickService goodClickService;
	
	@Resource
	private GoodClickIndexService goodClickIndexService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private UserNoteCollectService userNoteCollectService;
	
	@Resource
	private CommunityNoteCounterService communityNoteCounterService;
	
	/**
	 * 判断帖子是否存在
	 * @param noteId
	 * @return
	 */
	public boolean isNoteExisted(Long noteId){
		CommunityNote note = this.communityNoteService.findById(noteId);
		if(note == null || note.getDeleted()){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断被回复的记录是否存在
	 * @param recordId
	 * @return
	 */
	public boolean isRecordExisted(Long recordId){
		CommunityNoteComment record = this.communityNoteCommentService.findById(recordId);
		if(record == null || record.getDeleted()){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断取消点赞时，其参数是否ok
	 * @param noteId
	 * @param uid
	 * @param goodId
	 * @return
	 */
	public boolean islegalForNote(Long noteId,Long uid,Long goodId){
		GoodClick t = this.goodClickService.findById(goodId);
		if(t.getUser_id().longValue() != uid.longValue()){
			return false;
		}
		if(t.getObject_id().longValue() != noteId.longValue()){
			return false;
		}
		if(t.getGood_type().intValue() != GoodClickIndexType.note2good.getValue()){
			return false;
		}
		return true;
	}
	
	/**
	 * 发帖
	 * @param bean
	 */
	public void addNote(CommunityNoteAddBean bean){
		CommunityNote entity = new CommunityNote();
		entity.setCategory_id(bean.getCategory_id());
		entity.setContent(bean.getContent());
		List<String> list = bean.getPic_list();
		if(list != null){
			StringBuilder sb = new StringBuilder();
			for(String pic : list){
				if(sb.length() == 0){
					sb.append(pic); //TODO 路径是否需要处理
				}else{
					sb.append(",").append(pic);
				}
			}
			entity.setPictures(sb.toString());
		}
		entity.setTitle(bean.getTitle());
		entity.setUser_id(bean.getUser_id());
		Date date = new Date();
		entity.setCreate_time(date);
		this.communityNoteService.add(entity);
		//添加user2note索引
		CommunityNoteIndex index = new CommunityNoteIndex();
		index.setIndexId(String.valueOf(bean.getUser_id().longValue()));
		index.setIndexType(CommunityNoteIndexType.user2note.getValue());
		index.setNoteId(entity.getId());
		index.setCreateTime(date);
		this.communityNoteIndexService.add(index);
		//添加category2note索引
		index.setIndexId(String.valueOf(bean.getCategory_id().longValue()));
		index.setIndexType(CommunityNoteIndexType.category2note.getValue());
		this.communityNoteIndexService.add(index);
	}
	
	/**
	 * 删帖（软删）
	 * @param uid
	 * @param noteId
	 */
	public void removeNote(Long uid,Long noteId){
		CommunityNote note = this.communityNoteService.findById(noteId);
		if(note != null){
			Long catId = note.getCategory_id();
			note.setDeleted(true);
			this.communityNoteService.update(note, "deleted");
			//删除user2note索引
			CommunityNoteIndex index = new CommunityNoteIndex();
			index.setIndexId(String.valueOf(uid.longValue()));
			index.setIndexType(CommunityNoteIndexType.user2note.getValue());
			index.setNoteId(noteId);
			this.communityNoteIndexService.del(index);
			//删除category2note索引
			index.setIndexId(String.valueOf(catId.longValue()));
			index.setIndexType(CommunityNoteIndexType.category2note.getValue());
			index.setNoteId(noteId);
			this.communityNoteIndexService.del(index);
		}
	}
	
	/**
	 * 评论帖子
	 * @param bean
	 */
	public void commentNote(CommunityCommentBean bean){
		CommunityNoteComment t = new CommunityNoteComment();
		t.setNote_id(bean.getNote_id());
		t.setContent(bean.getContent());
		t.setContent_user_id(bean.getComment_user_id());
		this.communityNoteCommentService.add(t);
		//添加评论索引
		CommunityNoteCommentIndex index = new CommunityNoteCommentIndex();
		index.setIndexId(String.valueOf(t.getNote_id().longValue()));
		index.setIndexType(CommunityNoteCommentIndexType.note2comment.getValue());
		index.setCommentId(t.getId());
		this.communityNoteCommentIndexService.add(index);
	}
	
	/**
	 * 回复评论
	 * @param bean
	 */
	public void replyComment(CommunityReplyBean bean){
		CommunityNoteComment t = new CommunityNoteComment();
		t.setNote_id(bean.getNote_id());
		t.setCommented_id(bean.getRecord_id());
		t.setRoot_comment_id(bean.getComment_id());
		t.setContent(bean.getContent());
		t.setContent_user_id(bean.getReply_user_id());
		//添加回复索引
		CommunityNoteCommentIndex index = new CommunityNoteCommentIndex();
		index.setIndexId(String.valueOf(t.getRoot_comment_id().longValue()));
		index.setIndexType(CommunityNoteCommentIndexType.comment2reply.getValue());
		index.setCommentId(t.getId()); //回复id
		this.communityNoteCommentIndexService.add(index);
	}
	
	
	/**
	 * 删除评论或者回复
	 * @param id
	 */
	public void removeCommentOrReply(Long id){
		CommunityNoteComment entity = this.communityNoteCommentService.findById(id);
		if(entity != null){
			entity.setDeleted(true);
			this.communityNoteCommentService.update(entity, "deleted");
			if(entity.getRoot_comment_id() == null){ //是评论
				CommunityNoteCommentIndex index = new CommunityNoteCommentIndex();
				index.setIndexId(String.valueOf(entity.getNote_id().longValue()));
				index.setIndexType(CommunityNoteCommentIndexType.note2comment.getValue());
				index.setCommentId(id);
				this.communityNoteCommentIndexService.del(index);
			}else{  //是回复
				CommunityNoteCommentIndex index = new CommunityNoteCommentIndex();
				index.setIndexId(String.valueOf(entity.getRoot_comment_id().longValue()));
				index.setIndexType(CommunityNoteCommentIndexType.comment2reply.getValue());
				index.setCommentId(id);
			}
		}
		
	}
	
	
	/**
	 * 为帖子点赞
	 * @param noteId
	 * @param uid
	 * @return 点赞记录ID
	 */
	public Long clickGoodForNote(Long noteId,Long uid){
		GoodClick t = new GoodClick();
		t.setUser_id(uid);
		t.setGood_type(GoodClickIndexType.note2good.getValue());
		t.setObject_id(noteId);
		this.goodClickService.add(t);
		//增加帖子的点赞记录索引
		GoodClickIndex index = new GoodClickIndex();
		index.setIndexId(String.valueOf(noteId.longValue()));
		index.setIndexType(GoodClickIndexType.note2good.getValue());
		index.setGoodId(t.getId());
		this.goodClickIndexService.add(index);
		return t.getId();
	}
	
	/**
	 * 取消点赞
	 * @param noteId
	 * @param uid
	 * @param goodId
	 */
	public void cancelGoodForNote(Long noteId,Long uid,Long goodId){
		//删除点赞记录
		this.goodClickService.del(goodId);
		//删除对应索引
		GoodClickIndex index = new GoodClickIndex();
		index.setIndexId(String.valueOf(noteId.longValue()));
		index.setIndexType(GoodClickIndexType.note2good.getValue());
		index.setGoodId(goodId);
		this.goodClickIndexService.del(index);
	}
	
	/**
	 * 帖子列表
	 * @param catId
	 * @param ps
	 * @param pn
	 * @return
	 */
	public PagedListRespData<CommunityNoteBean> findNote(Long catId,int ps,int pn){
		List<CommunityNoteBean> list = new ArrayList<>();
		CommunityNote queryEntity = new CommunityNote();
		queryEntity.setCategory_id(catId);
		String [] conditions = new String[]{"category_id, deleted"};
		int count = this.communityNoteService.count(queryEntity, null, conditions);
		if(count > 0){
			List<CommunityNote> pageList = this.communityNoteService.find(queryEntity,pn,ps, 0, null, conditions);
			for(CommunityNote i : pageList){
				CommunityNoteBean bean = new CommunityNoteBean();
				if(i.getContent() == null){
					bean.setContent("");
				}else{
					String content = i.getContent();
					if(content.length() > LenConstant.listContentLen){ //只返回给列表一部分内容
						bean.setPart(true); //已截取
						bean.setContent(content.substring(0, LenConstant.listContentLen));
					}else{
						bean.setContent(content);
					}
					bean.setContent(i.getContent() == null ? "":i.getContent());
				}
				bean.setNote_id(i.getId());
				bean.setTime(DateUtils.getDateStr(i.getCreate_time(), DateUtils.formalPattern));
				bean.setTitle(i.getTitle() == null ? "":i.getTitle());
				if(i.getPictures()!= null){
					String []pic = i.getPictures().split(",");
					bean.setPictures(Arrays.asList(pic));
				}
				//发帖人信息
				User user = this.userService.findById(i.getUser_id());
				if(!user.getSex_switch()&&user.getSex() != null){ //公开的
					bean.setSex(user.getSex());
				}else{
					bean.setSex(-1); //未知
				}
				bean.setLocation(user.getLocation() == null ? "":user.getLocation());
				bean.setUser_nickname(user.getNickname()==null ? user.getUsername():user.getNickname());
				bean.setNote_user_id(i.getUser_id());
				bean.setNote_user_headpic(user.getHead_picture()==null ? "":user.getHead_picture());
				//收藏数 TODO 后续根据索引搜索
				UserNoteCollect unc = new UserNoteCollect();
				unc.setNote_id(i.getId());
				int total = userNoteCollectService.count(unc, null, new String[]{"note_id",});
				bean.setShoucang_cnt(total);
				//浏览数
				CommunityNoteCounter counter = this.communityNoteCounterService.findById(i.getId());
				bean.setScan_cnt(counter.getScan_count());
				list.add(bean);
				//评论数 TODO 后续根据索引搜索
				CommunityNoteComment cnc = new CommunityNoteComment();
				cnc.setNote_id(i.getId());
				int commentCnt = this.communityNoteCommentService.count(cnc, null, new String[]{"note_id","deleted"});
				bean.setPinglun_cnt(commentCnt);
				list.add(bean);
			}
		}
		return new PagedListRespData<>(list, pn, ps, count);
	}
	
	/**
	 * 帖子详情
	 * @param noteId
	 * @return
	 */
	public CommunityNoteDetailBean findNoteDetail(Long noteId){
		CommunityNote i = this.communityNoteService.findById(noteId);
		if(i == null){
			return null;
		}
		CommunityNoteDetailBean bean = new CommunityNoteDetailBean();
		bean.setCat_id(i.getCategory_id());
		bean.setContent(i.getContent() == null ? "":i.getContent());
		bean.setNote_id(i.getId());
		bean.setTime(DateUtils.getDateStr(i.getCreate_time(), DateUtils.formalPattern));
		bean.setTitle(i.getTitle() == null ? "":i.getTitle());
		if(i.getPictures()!= null){
			String []pic = i.getPictures().split(",");
			bean.setPictures(Arrays.asList(pic));
		}
		//发帖人信息
		User user = this.userService.findById(i.getUser_id());
		if(!user.getSex_switch()&&user.getSex() != null){ //公开的
			bean.setSex(user.getSex());
		}else{
			bean.setSex(-1); //未知
		}
		bean.setLocation(user.getLocation() == null ? "":user.getLocation());
		bean.setUser_nickname(user.getNickname()==null ? user.getUsername():user.getNickname());
		bean.setNote_user_id(i.getUser_id());
		bean.setNote_user_headpic(user.getHead_picture()==null ? "":user.getHead_picture());
		//点赞数
		GoodClick gc = new GoodClick();
		gc.setGood_type(ZanType.note.getValue());
		gc.setObject_id(noteId);
		int zanCnt = this.goodClickService.count(gc, null, new String[]{"good_type","object_id"});
		bean.setZan_cnt(zanCnt);
		//TODO 点赞人信息，后面加上
		return bean;
	}
	
	
	/**
	 * 评论列表，包含回复
	 * @param noteId
	 * @param ps
	 * @param pn
	 * @return
	 */
	public PagedListRespData<CommunityCommentDetailBean> findComment(Long noteId,int ps,int pn){
		
		List<CommunityCommentDetailBean> list = new ArrayList<>();
		CommunityNoteComment cnc = new CommunityNoteComment();
		cnc.setNote_id(noteId);
		String [] conditions = new String []{"note_id","deleted","root_comment_id"};
		//评论
		int total = this.communityNoteCommentService.count(cnc, null, conditions);
		if(total > 0){
			List<CommunityNoteComment> pagedList = this.communityNoteCommentService.find(cnc, pn, ps, 0, null, conditions);
			for(CommunityNoteComment one: pagedList){
				CommunityCommentDetailBean bean = new CommunityCommentDetailBean();
				bean.setComment_id(one.getId());
				bean.setContent(one.getContent());
				bean.setTime(DateUtils.getDateStr(one.getCreate_time(), DateUtils.formalPattern));
				bean.setComment_user_id(one.getContent_user_id());
				//
				User user = this.userService.findById(one.getContent_user_id());
				//bean.setComment_user_heapic(user.getHead_picture()==null ? "":user.getHead_picture());
				bean.setComment_user_name(user.getNickname()==null ? user.getUsername():user.getNickname());
				//
				CommunityNoteComment queryReply = new CommunityNoteComment();
				queryReply.setRoot_comment_id(one.getId());
				String [] queryConditions = new String []{"root_comment_id","deleted"};
				//回复
				int count = this.communityNoteCommentService.count(queryReply, null, queryConditions);
				if(count > 0){
					List<CommunityNoteComment> replyList = this.communityNoteCommentService.find(queryReply, 0, count, 1, null, queryConditions);
					for(CommunityNoteComment replyBean :replyList){
						Reply reply = new Reply();
						reply.setComment_id(replyBean.getId());
						reply.setFrom_user_id(replyBean.getContent_user_id());
						//查询回复人昵称
						User replyUser = this.userService.findById(replyBean.getContent_user_id());
						reply.setFrom_user_name(replyUser.getNickname() == null? replyUser.getUsername():user.getNickname());
						 //回复的是评论
						if(replyBean.getCommented_id().longValue() == one.getId().longValue()){
							reply.setTo_user_id(one.getContent_user_id());
							reply.setTo_user_name(user.getNickname()==null ? user.getUsername():user.getNickname());
						}else{
						//查询被回复的那条记录信息
							CommunityNoteComment recordUser = this.communityNoteCommentService.findById(replyBean.getCommented_id()); //被回复的记录
							User beRepliedUser = this.userService.findById(recordUser.getContent_user_id());
							reply.setTo_user_id(recordUser.getContent_user_id());
							reply.setTo_user_name(beRepliedUser.getNickname()==null?beRepliedUser.getUsername():beRepliedUser.getNickname());
						}
						reply.setContent(replyBean.getContent());
						reply.setTime(DateUtils.getDateStr(replyBean.getCreate_time(), DateUtils.formalPattern));
						bean.getReply_list().add(reply);
					}
				}
				list.add(bean);
			}
		}
		return new PagedListRespData<>(list, pn, ps, total);
	}
	
	
	public UserService getUserService() {
		return userService;
	}
}
