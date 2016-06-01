package com.aipa.svc.common.vo;

public class Reply {

	//
	private Long from_user_id;
	//private String from_user_heapic;
	private String from_user_name;
	
	private Long to_user_id;
	private String to_user_name;
	
	//内容
	private Long comment_id;
	private String content;
	private String time;
	
	
	public Long getFrom_user_id() {
		return from_user_id;
	}
	public void setFrom_user_id(Long from_user_id) {
		this.from_user_id = from_user_id;
	}
//	public String getFrom_user_heapic() {
//		return from_user_heapic;
//	}
//	public void setFrom_user_heapic(String from_user_heapic) {
//		this.from_user_heapic = from_user_heapic;
//	}
	public String getFrom_user_name() {
		return from_user_name;
	}
	public void setFrom_user_name(String from_user_name) {
		this.from_user_name = from_user_name;
	}
	public Long getTo_user_id() {
		return to_user_id;
	}
	public void setTo_user_id(Long to_user_id) {
		this.to_user_id = to_user_id;
	}
	public String getTo_user_name() {
		return to_user_name;
	}
	public void setTo_user_name(String to_user_name) {
		this.to_user_name = to_user_name;
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
	
}
