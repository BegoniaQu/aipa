package com.aipa.svc.v1.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.aipa.community.module.entity.CommunityCategory;
import com.aipa.svc.common.enume.Marital;
import com.aipa.svc.common.enume.Sex;
import com.aipa.svc.common.enume.SexOrient;
import com.aipa.svc.common.vo.CommunityCategoryParentBean;
import com.aipa.svc.common.vo.UserInfoBean;
import com.aipa.user.module.entity.User;

@Component
public class BeanConverter {

	public List<CommunityCategoryParentBean> convert(List<CommunityCategory> list){
		List<CommunityCategoryParentBean> parentList = new ArrayList<>();
		for(CommunityCategory one : list){
			CommunityCategoryParentBean bean = new CommunityCategoryParentBean();
			bean.setName(one.getName());
			bean.setParent_id(one.getId());
			parentList.add(bean);
		}
		return parentList;
	}
	
	public UserInfoBean convert2ForSelf(User user){
		UserInfoBean bean = new UserInfoBean();
		bean.setAge(user.getAge());
		bean.setAge_switch(user.getAge_switch());
		bean.setHead_picture(user.getHead_picture());//TODO 需要加上七牛的路径
		bean.setLocation(user.getLocation());
		
		bean.setMarital_status(user.getMarital_status());
		bean.setMarital_status_switch(user.getMarital_status_switch());
		Integer marital_statusI = Integer.parseInt(user.getMarital_status().toString());
		bean.setMarital_status_str(Marital.get(marital_statusI).name());
		
		bean.setNickname(user.getNickname());
		
		bean.setSex(user.getSex());
		bean.setSex_switch(user.getSex_switch());
		Integer sexI = Integer.parseInt(user.getSex().toString());
		bean.setSex_str(Sex.get(sexI).name());
		
		bean.setSex_orient(user.getSex_orient());
		bean.setSex_orient_switch(user.getSex_orient_switch());
		Integer sex_orientI = Integer.parseInt(user.getSex_orient().toString());
		bean.setSex_orient_str(SexOrient.get(sex_orientI).name());
		
		return bean;
	}
	
	
	public UserInfoBean convert2ForOthers(User user){
		UserInfoBean bean = new UserInfoBean();
		bean.setHead_picture(user.getHead_picture());//TODO 需要加上七牛的路径
		bean.setLocation(user.getLocation());
		bean.setNickname(user.getNickname());
		if(!user.getAge_switch()){
			bean.setAge(user.getAge());
		}
		if(!user.getMarital_status_switch()){
			Integer marital_statusI = Integer.parseInt(user.getMarital_status().toString());
			bean.setMarital_status_str(Marital.get(marital_statusI).name());
		}
		if(!user.getSex_switch()){
			Integer sexI = Integer.parseInt(user.getSex().toString());
			bean.setSex_str(Sex.get(sexI).name());
		}
		if(!user.getSex_orient_switch()){
			Integer sex_orientI = Integer.parseInt(user.getSex_orient().toString());
			bean.setSex_orient_str(SexOrient.get(sex_orientI).name());
		}
		return bean;
	}
	
	
}
