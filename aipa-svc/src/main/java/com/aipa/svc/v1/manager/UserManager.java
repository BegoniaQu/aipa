package com.aipa.svc.v1.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aipa.community.module.entity.CommunityCategory;
import com.aipa.community.module.entity.CommunityNote;
import com.aipa.community.module.service.CommunityCategoryService;
import com.aipa.community.module.service.CommunityNoteService;
import com.aipa.svc.common.util.AipaTokenUtil;
import com.aipa.svc.common.util.DateUtils;
import com.aipa.svc.common.util.PagedListRespData;
import com.aipa.svc.common.vo.CollectedNoteBean;
import com.aipa.svc.common.vo.InterestedCategoryBean;
import com.aipa.svc.common.vo.UserInfoBean;
import com.aipa.svc.v1.converter.BeanConverter;
import com.aipa.user.module.entity.User;
import com.aipa.user.module.entity.UserCategoryInterest;
import com.aipa.user.module.entity.UserNoteCollect;
import com.aipa.user.module.service.UserCategoryInterestService;
import com.aipa.user.module.service.UserNoteCollectService;
import com.aipa.user.module.service.UserService;

@Component
public class UserManager {

	private static final Logger log = LoggerFactory.getLogger("UserManager");
	
	@Resource
	private UserService userService;
	
	@Resource
	private BeanConverter beanConverter;
	
	@Resource
	private CommunityNoteService communityNoteService;
	
	@Resource
	private UserNoteCollectService userNoteCollectService;
	
	@Resource
	private UserCategoryInterestService userCategoryInterestService;
	
	@Resource
	private CommunityCategoryService communityCategoryService;
	
	/**
	 * 根据主键uid获取用户信息
	 * @param uid
	 * @return
	 */
	public UserInfoBean getById(Long uid){
		User user = this.userService.findById(uid);
		return beanConverter.convert2ForSelf(user);
	}
	
	public User getUser(Long uid){
		User user = this.userService.findById(uid);
		return user;
	}
	
	/**
	 * 查看他人信息
	 * @param uid
	 * @return
	 */
	public UserInfoBean getOtherById(Long uid){
		User user = this.userService.findById(uid);
		if(user == null){
			return null;
		}
		return beanConverter.convert2ForOthers(user);
	}
	
	public void updateUser(User user){
		this.userService.update(user);
	}
	
	/**
	 * 登录校验
	 * @param username
	 * @param password
	 * @return
	 */
	public String getToken(String username,String password){
		User user = new User();
		user.setUsername(username);
		User queryEntity = this.userService.findByUniqueKey(user);
		if(queryEntity != null && !queryEntity.isDeleted() && queryEntity.isEnabled()){
			if(queryEntity.getPassword().equals(password)){
				//生成token
				Long uid = queryEntity.getId();
				user.setId(uid);
				String token = AipaTokenUtil.getToken(uid);
				user.setLatest_login_time(new Date());
				this.userService.update(user, new String[]{"latest_login_time"});
				return token;
			}
		}
		return null;
	}
	
	/**
	 * 根据账号获取账号信息
	 * @param username
	 * @return
	 */
	public User getByUsername(String username){
		User user = new User();
		user.setUsername(username);
		User queryEntity = this.userService.findByUniqueKey(user);
		return queryEntity;
	}
	
	/**
	 * 注册
	 * @param username
	 * @param password
	 */
	public User reg(String username,String password){
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		this.userService.add(user);
		return user;
	}
	
	/**
	 * 用户查看自己收藏的帖子
	 * @param userId
	 * @param ps
	 * @param pn
	 * @return
	 */
	public PagedListRespData<CollectedNoteBean> findCollectedNotePage(Long userId,int ps,int pn){
		List<CollectedNoteBean> list = new ArrayList<>();
		UserNoteCollect queryEntity = new UserNoteCollect();
		queryEntity.setUser_id(userId);
		int count = this.userNoteCollectService.count(queryEntity, null, new String[]{"user_id"});
		if(count > 0){
			List<UserNoteCollect> pageList = this.userNoteCollectService.find(queryEntity,pn,ps, 0, null, new String[]{"user_id"});
			for(UserNoteCollect i : pageList){
				CollectedNoteBean bean = new CollectedNoteBean();
				bean.setId(i.getId());
				CommunityCategory category = this.communityCategoryService.findById(i.getCategory_id());
				bean.setCategory_name(category.getName());
				CommunityNote note = this.communityNoteService.findById(i.getNote_id());
				bean.setContent(note.getContent());
				bean.setNote_id(note.getId());
				bean.setCreate_time(DateUtils.getDateStr(note.getCreate_time(), DateUtils.formalPattern));
				bean.setTitle(note.getTitle());
				String []pic = note.getPictures().split(",");
				bean.setPictures(Arrays.asList(pic));
				list.add(bean);
			}
		}
		return new PagedListRespData<>(list, pn, ps, count);
	}
	
	/**
	 * 收藏帖子
	 * @param userId
	 * @param noteId
	 */
	public void collectNote(Long userId,Long noteId){
		CommunityNote note = this.communityNoteService.findById(noteId);
		
		if(note != null){
			UserNoteCollect queryEntity = new UserNoteCollect();
			queryEntity.setUser_id(userId);
			queryEntity.setNote_id(noteId);
			int count = this.userNoteCollectService.count(queryEntity, null, new String[]{"user_id","note_id"});
			if(count == 0){
				UserNoteCollect t = new UserNoteCollect();
				t.setCategory_id(note.getCategory_id());
				t.setNote_id(noteId);
				t.setUser_id(userId);
				this.userNoteCollectService.add(t);
			}else{
				log.warn("noteId={} already collected",new Object[]{noteId});
			}
		}else{
			log.warn("noteId={} not existed",new Object[]{noteId});
		}
	}
	
	/**
	 * 移除收藏的帖子
	 * @param id
	 */
	public void removeCollectedNote(Long id){
		this.userNoteCollectService.del(id);
	}
	
	/**
	 * 查看感兴趣的分类
	 * @param userId
	 * @return
	 */
	public List<InterestedCategoryBean> findInterestedCategory(Long userId){
		List<InterestedCategoryBean> list = new ArrayList<>();
		UserCategoryInterest queryEntity = new UserCategoryInterest();
		queryEntity.setUser_id(userId);
		int count = this.userCategoryInterestService.count(queryEntity, null, new String[]{"user_id"});
		if(count > 0){
			List<UserCategoryInterest> interestedList = this.userCategoryInterestService.find(queryEntity, 0, count, 1, null, new String[]{"user_id"});
			for(UserCategoryInterest i : interestedList){
				InterestedCategoryBean bean = new InterestedCategoryBean();
				bean.setCategory_id(i.getCategory_id());
				CommunityCategory category = this.communityCategoryService.findById(i.getCategory_id());
				bean.setName(category.getName());
				list.add(bean);
			}
		}
		return list;
	}
	
	/**
	 * 关注感兴趣的分类
	 * @param userId
	 * @param categoryId
	 */
	public void addInsterestCategory(Long userId,Long categoryId){
		CommunityCategory cc = this.communityCategoryService.findById(categoryId);
		if(cc != null){
			UserCategoryInterest queryEntity = new UserCategoryInterest();
			queryEntity.setUser_id(userId);
			queryEntity.setCategory_id(categoryId);
			int count = this.userCategoryInterestService.count(queryEntity, null, new String[]{"user_id","category_id"});
			if(count == 0){
				UserCategoryInterest t = new UserCategoryInterest();
				t.setCategory_id(categoryId);
				t.setUser_id(userId);
				this.userCategoryInterestService.add(t);
			}else{
				log.warn("categoryId={} already interested",new Object[]{categoryId});
			}
		}else{
			log.warn("categoryId={} not existed",new Object[]{categoryId});
		}
		
	}
	
	/**
	 * 移除感兴趣的分类
	 * @param id
	 */
	public void removeInsterestCategory(Long id){
		this.userCategoryInterestService.del(id);
	}
	
	
	public UserService getUserService() {
		return userService;
	}
}
