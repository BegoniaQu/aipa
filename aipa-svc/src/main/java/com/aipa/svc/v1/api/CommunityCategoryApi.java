package com.aipa.svc.v1.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aipa.svc.v1.manager.CommunityCategoryManager;
import com.aipa.svc.v1.param.CommunityParamExtract;

@Controller
@RequestMapping("community/category")
public class CommunityCategoryApi {
	
	private static final Logger log = LoggerFactory.getLogger("CommunityCategoryApi");
	
	@Resource
	private CommunityCategoryManager communityCategoryManager;
	
	@RequestMapping(value = "add.ap",method = RequestMethod.POST)
	public Object addCategory(HttpServletRequest request){
		String categoryName = CommunityParamExtract.getCategoryName(request);
		String descr = CommunityParamExtract.getCategoryDescr(request);
		Long parentId = CommunityParamExtract.getParentId(request);
		log.info("add category,categoryName={},descr={},parentId={}",new Object[]{categoryName,descr,parentId});
		this.communityCategoryManager.add(categoryName, descr, parentId);
		return "";
		
	}

	@RequestMapping(value = "{id}/upt.ap",method = RequestMethod.POST)
	public Object updateCategory(@PathVariable Long id, HttpServletRequest request){
		String categoryName = CommunityParamExtract.getCategoryName(request);
		String descr = CommunityParamExtract.getCategoryDescr(request);
		Long parentId = CommunityParamExtract.getParentId(request);
		log.info("update category,id={},categoryName={},descr={},parentId={}",new Object[]{id,categoryName,descr,parentId});
		this.communityCategoryManager.update(id, categoryName, descr, parentId);
		return "";
	}
	
	@RequestMapping(value = "list.ap",method = RequestMethod.GET)
	public Object findAll(){
		return this.communityCategoryManager.findAndClassify();
	}
	
	@RequestMapping(value = "super.ap",method = RequestMethod.GET)
	public Object findParent(){
		return this.communityCategoryManager.getParentCategory();
	}
	
	
}
