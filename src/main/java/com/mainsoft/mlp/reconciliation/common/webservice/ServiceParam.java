package com.mainsoft.mlp.reconciliation.common.webservice;

import java.io.Serializable;

/**
 * 服务参数对象
 * 
 * @author czz
 * @version 2016-11-12
 * 
 */
public class ServiceParam implements Serializable {

	private static final long serialVersionUID = 1L;

	private String passport;	// DPS_Passport对象序列化后的JSON字符串
	private String paramList;	// 参数集合序列化后的JSON字符串

	// private String checkValue;// 将paramList加密后字符串

	public ServiceParam() {
	}

	public ServiceParam(String passport, String paramList) {
		this.passport = passport;
		this.paramList = paramList;
		// this.checkValue = checkValue;

	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	// public String getCheckValue() {
	// return checkValue;
	// }
	//
	// public void setCheckValue(String checkValue) {
	// this.checkValue = checkValue;
	// }

	public String getParamList() {
		return paramList;
	}

	public void setParamList(String paramList) {
		this.paramList = paramList;
	}

}
