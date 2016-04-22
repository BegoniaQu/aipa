package com.aipa.svc.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public enum ResultCode {
	OK(0, ""),

	/********************************
	 **  Client Errors Definition  **
	 ********************************/
	
	ClientError(400000, "客户端错误！"),
	ClientVersionNotSupported(400001, "客户端版本不支持，请更新！"),  // 这个错误码用于强制升级
	
	Unauthorized(401000, "未登录！"),
	AuthenticationError(401001, "身份校验错误！"),
	AuthenticationFailure(401002, "身份校验失败！"),
	AuthenticationExpired(401003, "身份过期！"),
	VerifySignatureFailure(401004, "请求校验失败！"),
	
	ActionNotAuthorized(401100, "权限验证失败！"),
	
	Forbidden(403000, "禁止访问！"),
	TooFrquentRequest(403001, "请求过于频繁！"),
	NotFound(404000, "没有找到该请求对象！"),
	MethodNotAllowed(405000, "不允许该方法！"),
	InvalidOperation(406000, "无效操作！"),
	NotSupportOperation(406001, "不支持的操作！"),

	ParameterError(418000, "参数错误！"),
	ParameterMissing(418001, "参数缺失！"),
	ParameterFormatError(418002, "参数格式错误！"),
	ParameterLengthInvalid(418003, "参数长度错误！"),
	ParameterValueError(418004, "参数值错误！"),
	ParameterInconsistant(418004, "参数不一致错误！"),
	DoesNotSupportRequiredResponseFormat(418008, "请求内容格式不支持！"),

	TargetRefused(421000, "被请求对象拒绝！"),
	TargetClosed(421001, "该请求对象已关闭！"),
	TargetAlreadyIn(421002, "该请求对象已经存在！"),
	TargetFull(421003, "该请求对象已满！"),
	TargetNotExists(421004, "该请求对象不存在！"),
	TargetProcessing(421005, "该请求对象正在被处理！"),
	TargetNotSupport(421006, "该请求对象不被支持"),
	TargetExhausted(4210087, "该请求对象已经耗尽"),
	
	
	// Circle reserved code 431000-431999
	CircleRoleAlreadyExist(431000, "该用户在圈子内已存在职务"),
	CircleRoleNotExist(431001, "该用户在圈子内不存在该职务"),
	CircleUserNotExist(431002, "该用户不存在"),
	CircleForbidOnManager(431003, "圈子管理员无法被封禁"),
	CircleForbidFailure(431004, "封禁失败"),
	CircleUnforbidFailure(431005, "解除封禁失败"),
	CircleUserNotInFollow(431006, "该用户未加入圈子"),
	CircleReachManagerLimit(431007, "管理员到达人数上限"),
	CircleUnknownAuditType(431008, "未知管理员日志类型"),
	CircleQuestionReachMonthLimit(431009, "用户提问数到达本月上限"),
	CircleQuestionRsReachMonthLimit(431010, "用户同问数到达本月上限"),

	
	/********************************
	 **  Server Errors Definition  **
	 ********************************/
	ServerError(500000, "服务器错误！", HttpStatus.INTERNAL_SERVER_ERROR),
	ServerOverload(500001, "服务器过载！"),
	UnderMaintance(550000, "服务器正在维护！"),
	UnderConstruction(551000, "正在建设！"),
	MessageServerError(556000, "消息服务器错误！"),
	ResourceInsufficient(566000, "资源不足！"),
	DataError(570000, "数据错误"),
	AcquireLockError(571000, "操作失败"),
	
	/********************************
	 **  Business Errors Definition  **
	 ********************************/
	BusinessError(600000, "业务失败")  // 6 + 2位业务号 + 3位业务自身错误码
	
	;
	
	private int code;
	private String msg;
	private HttpStatus httpStatus = HttpStatus.OK;
	
	private static Map<Integer, ResultCode> map = new HashMap<Integer, ResultCode>();
	static{
		for(ResultCode resultCode : ResultCode.values()){
			map.put(resultCode.getCode(), resultCode);
		}
	}
	
	private ResultCode(int code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	private ResultCode(int code, String msg, HttpStatus httpStatus){
		this.code = code;
		this.msg = msg;
		this.httpStatus = httpStatus;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public static ResultCode getResultCode(int code){
		return map.get(code);
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
