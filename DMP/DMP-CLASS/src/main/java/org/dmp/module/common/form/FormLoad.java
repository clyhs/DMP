package org.dmp.module.common.form;

public class FormLoad<T> {
	private Boolean success = Boolean.FALSE;
	private T data;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}