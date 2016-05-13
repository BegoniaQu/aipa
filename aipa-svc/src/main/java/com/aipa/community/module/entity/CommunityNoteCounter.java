package com.aipa.community.module.entity;

import java.io.Serializable;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.dao.annotation.Id;
import com.qy.data.common.dao.annotation.Table;
import com.qy.data.common.rao.annotation.RedisField;
import com.qy.data.common.rao.annotation.RedisId;

@Table(name="tb_note_counter")
public class CommunityNoteCounter implements Serializable{

	private static final long serialVersionUID = 7285217579208028597L;
	
	@Id
	@RedisId
	private Long note_id;  		//帖子ID
	
	@Column
	@RedisField
	private Integer scan_count;		//浏览次数
	
	
	public Long getNote_id() {
		return note_id;
	}
	public void setNote_id(Long note_id) {
		this.note_id = note_id;
	}
	public Integer getScan_count() {
		return scan_count;
	}
	public void setScan_count(Integer scan_count) {
		this.scan_count = scan_count;
	}
	
}
