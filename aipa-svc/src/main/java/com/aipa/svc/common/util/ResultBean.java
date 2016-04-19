package com.aipa.svc.common.util;


/**
 * 
 * @author qy
 *
 */
public class ResultBean {
	public static final Integer OK = 0;
	public static final Integer CREATEED = 0;
	public static final Integer NOT_MODIFY = 0;
	public static final Integer UNAUTHORIZED = 401;
	public static final Integer UNAUTHORIZED_NOTOKEN = 4011;
	public static final Integer UNAUTHORIZED_ERROR = 4012;
	public static final Integer UNAUTHORIZED_EXPIRE = 4013;
	public static final Integer UNAUTHORIZED_NO_CONSIST = 4014;
	public static final Integer NOT_FOUND = 404;
	public static final Integer INVALID_REQUST = 422;
	public static final Integer SYS_ERROR = 500;
	
	public static final ResultBean SUCCESS_OK = new ResultBean(null, OK,"success");
	
	//mediasvc
	public static final Integer INVALID_LIVE_STATUS_ERR_CODE = 1;
	public static final Integer ACCESS_JUMP_FAILED_ERR_CODE = 2;
	public static final Integer NO_AVALIABLE_STREAM_ERR_CODE = 3;
	
	//livecontrol
	public static final Integer LIVE_CHANNEL_EXHAUSTED_ERR_CODE = 1;
	public static final Integer INVALID_LIVE_CONTROL_ERR_CODE = 2;
	public static final Integer NO_STAREAM_ONCEOK_ERR_CODE = 3;
	
	//zookeeper
	public static final Integer ZOOKEEPER_TIMEOUT_ERR_CODE = 1;
	
	//vodsvc
	public static final Integer DUPLICATE_RECODE_ERR_CODE = 1;
	
	private Object data;
	
	private Integer err;
	
	private String msg;
	
	public ResultBean() { }
	
	public ResultBean(Object data, Integer err) {
		this.data = data;
		this.err = err;
	}
	
	public ResultBean(Object data, Integer err, String msg) {
		this.data = data;
		this.err = err;
		this.msg = msg;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Integer getErr() {
		return err;
	}
	public void setErr(Integer err) {
		this.err = err;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ResultBean [data=" + data + ", err=" + err + "]";
	}
}
