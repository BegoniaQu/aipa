package com.aipa.svc.common.vo;

import java.util.ArrayList;
import java.util.List;

public class CommunityCategoryInfoBean {

	private Long parent_id;
	private String name;
	private String descr;
	private List<CommunityCategorySubBean> sub_list = new ArrayList<>();

	
	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public List<CommunityCategorySubBean> getSub_list() {
		return sub_list;
	}

	public void setSub_list(List<CommunityCategorySubBean> sub_list) {
		this.sub_list = sub_list;
	}
}
