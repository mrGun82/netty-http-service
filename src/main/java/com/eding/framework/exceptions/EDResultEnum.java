package com.eding.framework.exceptions;
/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 14:34
 */
public enum EDResultEnum {
	SUCCESS("200","ok"),
	FAILED("400","请求失败"),
	UNAUTHORIZED("401","非法请求"),
	ERROR("500","不知名错误"),
	ERROR_NULL("501","空指针异常"),
	ERROR_CLASS_CAST("502","类型转换异常"),
	ERROR_RUNTION("503","运行时异常"),
	ERROR_IO("504","上传文件异常"),
	ERROR_MOTHODNOTSUPPORT("505","请求方法错误"),
    ERROR_PARAMETER_NOT_MATCH("506","参数不匹配"),
	ERROR_HTTP_METHOD_NOT_SUPPORTED("507","参数不匹配"),


	//参数
	PARAMETER_NULL("10001","缺少参数或值为空"),
	TRIGGER_GROUP_AND_NAME_SAME("10002","组名和名称已存在"),



	//账户
	ACCOUNT_LOCK("20001","账号已锁定"),
	ACCOUNT_NOT_FOUND("20002","找不到账户信息"),
	ACCOUNT_PASSWARD_ERROR("20003","用户名密码错误"),
	ACCOUNT_EXIST("20004","账号已存在"),

	//权限
	AUTH_NOT_HAVE("30001","没有权限"),



	FILE_IS_NULL("40001","文件为空"),

	TASK_IS_RUNING("50001","任务已启动，无法再起启动"),
	TASK_IS_PAUSE("50002","任务暂停，只可继续执行"),
	TASK_NOT_RUNING("50003","任务未执行，无法暂停"),
	;
	
	private String message;
	private String status;
	
	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}
	private EDResultEnum(String status, String message) {
		this.message = message;
		this.status = status;
	}

	
}
