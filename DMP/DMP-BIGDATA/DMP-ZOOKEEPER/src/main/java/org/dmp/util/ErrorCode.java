package org.dmp.util;

public class ErrorCode {

	public  String code = "";//错误码
	public  String msg = "";//描述
	public ErrorCode(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
