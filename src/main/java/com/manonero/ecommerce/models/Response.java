package com.manonero.ecommerce.models;

public class Response {
	private Object data;
	private String message;
	private Boolean isSuccess;
	public Response(Object data, String message, Boolean isSuccess) {
		super();
		this.data = data;
		this.message = message;
		this.isSuccess = isSuccess;
	}
	public Response(Boolean isSuccess) {
		super();
		this.isSuccess = isSuccess;
	}
	public Response(Object data, Boolean isSuccess) {
		super();
		this.data = data;
		this.isSuccess = isSuccess;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
