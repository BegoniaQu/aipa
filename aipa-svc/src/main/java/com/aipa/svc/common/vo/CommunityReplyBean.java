package com.aipa.svc.common.vo;

public class CommunityReplyBean {

	private Long note_id;
	private Long record_id;	//被回复的记录Id
	private Long comment_id; //评论Id（root）
	private String content;
	private Long reply_user_id;
	
	public Long getNote_id() {
		return note_id;
	}
	public void setNote_id(Long note_id) {
		this.note_id = note_id;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getReply_user_id() {
		return reply_user_id;
	}
	public void setReply_user_id(Long reply_user_id) {
		this.reply_user_id = reply_user_id;
	}
	public Long getRecord_id() {
		return record_id;
	}
	public void setRecord_id(Long record_id) {
		this.record_id = record_id;
	}
	public Long getComment_id() {
		return comment_id;
	}
	public void setComment_id(Long comment_id) {
		this.comment_id = comment_id;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("note_id=").append(note_id);
		sb.append(",record_id=").append(record_id);
		sb.append(",comment_id=").append(comment_id);
		sb.append(",content=").append(content);
		return sb.toString();
	}
	
}
