package com.mainsoft.mlp.reconciliation.common.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 调用信息的枚举类
 * @author gaosong
 * 2018.09.4
 * */

public enum ClientCallerEnum implements IEnum{
	
	/** 信息管理服务调用者 */
	CMS_CLIENT_CALLER("cmsClientCaller", "信息管理服务调用者"),
	
	/** 舱单服务调用者 */
	DPS_CLIENT_CALLER("dpsClientCaller", "舱单服务调用者"),
	
	/** 基础数据服务调用者 */
	BAS_CLIENT_CALLER("basClientCaller", "基础数据服务调用者"),
	
	/** 备案调用服务调用者 */
	ACP_CLIENT_CALLER("acpClientCaller", "备案调用服务调用者"),
	
	/** 保税调用服务调用者 */
	BND_CLIENT_CALLER("bndClientCaller", "备案调用服务调用者"),
	;

	/** 构造函数 */
	ClientCallerEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	/** 编码 */
	private String code;
	
	/** 描述 */
	private String desc;
	
	/**
	 * 获取编码
	 * */
	@Override
	public String getCode() {
		return this.code;
	}

	/**
	 * 获取描述
	 * */
	@Override
	public String getDesc() {
		return this.desc;
	}
}
