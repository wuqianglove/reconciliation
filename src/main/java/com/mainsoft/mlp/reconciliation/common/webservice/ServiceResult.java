package com.mainsoft.mlp.reconciliation.common.webservice;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 服务结果对象
 * 
 * @author czz
 * 
 */
public class ServiceResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 返回成功
	 */
	public static final String RESULT_CODE_SUCCESS = "0";

	private String status;	// 返回状态 0:请求成功 1:系统异常 2:业务异常 3:验证签名失败 4:请求超时
	private String message;	// 返回信息
	private Object data;	// 返回数据

	public ServiceResult() {
	}

	public ServiceResult(String status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	@JsonIgnore()
	public boolean isFailure(){
		return !this.status.equals(ServiceResult.RESULT_CODE_SUCCESS);
	}
	
	@JsonIgnore()
	public boolean isSuccess(){
		return this.status.equals(ServiceResult.RESULT_CODE_SUCCESS);
	}
}
