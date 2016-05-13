package com.aipa.svc.common.vo;

public class CommunityCommentBean {

	private Long note_id;
	private Long comment_user_id;
	private String content;
	public Long getNote_id() {
		return note_id;
	}
	public void setNote_id(Long note_id) {
		this.note_id = note_id;
	}
	public Long getComment_user_id() {
		return comment_user_id;
	}
	public void setComment_user_id(Long comment_user_id) {
		this.comment_user_id = comment_user_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("note_id=").append(note_id);
		sb.append(",content=").append(content);
		return sb.toString();
	}
}
