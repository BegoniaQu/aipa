package com.aipa.svc.common.vo;

import java.util.ArrayList;
import java.util.List;

public class CommunityCommentDetailBean {

	private Long comment_user_id;
	private String comment_user_heapic;
	private String comment_user_name;
	
	private Long comment_id;
	private String content;
	private String time;
	
	private List<Reply> reply_list = new ArrayList<>();

	public Long getComment_user_id() {
		return comment_user_id;
	}
	public void setComment_user_id(Long comment_user_id) {
		this.comment_user_id = comment_user_id;
	}
	public String getComment_user_heapic() {
		return comment_user_heapic;
	}
	public void setComment_user_heapic(String comment_user_heapic) {
		this.comment_user_heapic = comment_user_heapic;
	}
	public String getComment_user_name() {
		return comment_user_name;
	}
	public void setComment_user_name(String comment_user_name) {
		this.comment_user_name = comment_user_name;
	}
	public Long getComment_id() {
		return comment_id;
	}
	public void setComment_id(Long comment_id) {
		this.comment_id = comment_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<Reply> getReply_list() {
		return reply_list;
	}
	public void setReply_list(List<Reply> reply_list) {
		this.reply_list = reply_list;
	}
	
}
