package com.aipa.svc.v1.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.aipa.community.module.entity.CommunityCategory;
import com.aipa.community.module.service.CommunityCategoryService;
import com.aipa.svc.common.vo.CommunityCategoryInfoBean;
import com.aipa.svc.common.vo.CommunityCategoryParentBean;
import com.aipa.svc.common.vo.CommunityCategorySubBean;
import com.aipa.svc.v1.converter.BeanConverter;

@Component
public class CommunityCategoryManager {

	@Resource
	private CommunityCategoryService communityCategoryService;
	
	@Resource
	private BeanConverter beanConverter;
	
	
	/**
	 * 获取社区分类结构
	 * @return
	 */
	public List<CommunityCategoryInfoBean> findAndClassify(){
		List<CommunityCategoryInfoBean> classifiedList = new ArrayList<>();
		//find all 
		List<CommunityCategory> list = findAll();
		for(CommunityCategory cc : list){
			CommunityCategoryInfoBean arg0 = new CommunityCategoryInfoBean();
			if(cc.getParent_id() == null){
				arg0.setDescr(cc.getDescr());
				arg0.setName(cc.getName());
				arg0.setParent_id(cc.getId());
				classifiedList.add(arg0);
			}else{
				for(CommunityCategoryInfoBean one : classifiedList){
					if(cc.getParent_id() == one.getParent_id()){
						CommunityCategorySubBean sub = new CommunityCategorySubBean();
						sub.setDescr(cc.getDescr());
						sub.setName(cc.getName());
						sub.setId(cc.getId());
						sub.setParent_id(cc.getParent_id());
						one.getSub_list().add(sub);
					}
				}
			}
		}
		return classifiedList;
	}
	
	
	private List<CommunityCategory> findAll(){
		CommunityCategory cc = new CommunityCategory();
		String [] conditions = new String[]{};
		int total = this.communityCategoryService.count(cc, null, conditions);
		if(total > 0){
			return this.communityCategoryService.find(cc, 0, total, 1, null, conditions);
		}
		return null;
	}
	
	/**
	 * 获取父分类，后台用
	 * @return
	 */
	public List<CommunityCategoryParentBean> getParentCategory(){
		CommunityCategory cc = new CommunityCategory();
		//条件
		String [] conditions = new String[]{"parent_id"};
		int count = this.communityCategoryService.count(cc, null, conditions);
		if(count > 0){
			List<CommunityCategory> list = this.communityCategoryService.find(cc, 0, count, 1, null, conditions);
			return beanConverter.convert(list);
		}
		return null;
	}
	
	/**
	 * 增加分类
	 * @param categoryName
	 * @param descr
	 * @param parentId
	 */
	public void add(String categoryName,String descr,Long parentId){
		CommunityCategory cc = new CommunityCategory();
		cc.setName(categoryName);
		if(descr != null){
			cc.setDescr(descr);
		}
		if(parentId != null){
			cc.setParent_id(parentId);
		}
		this.communityCategoryService.add(cc);
	}
	
	/**
	 * 修改分类
	 * @param id
	 * @param categoryName
	 * @param descr
	 * @param parentId
	 */
	public void update(Long id,String categoryName,String descr,Long parentId){
		//self
		CommunityCategory entity = communityCategoryService.findById(id);
		List<String> props = new ArrayList<>();
		if(categoryName != null){
			entity.setName(categoryName);
			props.add("name");
		}
		if(descr != null){
			entity.setDescr(descr);
			props.add("descr");
		}
		if(parentId != null){
			entity.setParent_id(parentId);
			props.add("parent_id");
		}
		this.communityCategoryService.update(entity,props.toArray(new String[props.size()]));
	}
	
	/**
	 * 感兴趣的分类计数器
	 * @param id
	 */
	public void incrementInterestCnt(Long id){
		CommunityCategory entity = new CommunityCategory();
		entity.setId(id);
		this.communityCategoryService.increment(entity, "user_interest_cnt");
	}
	
}
