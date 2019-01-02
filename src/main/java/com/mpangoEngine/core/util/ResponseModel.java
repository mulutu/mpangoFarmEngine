package com.mpangoEngine.core.util;

public class ResponseModel {
	int status;
    String message;
    Object result;
    
    public ResponseModel() {}
    
    public ResponseModel(int status_, String msg_) {
    	status =  status_;
    	message =  msg_;
    	//result = obj_;
    }
    
    
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}

}
