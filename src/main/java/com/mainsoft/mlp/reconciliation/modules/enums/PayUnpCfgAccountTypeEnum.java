package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * unionpay用户类型枚举
 * @author ZhangSC
 * 2017-04-11
 * */
public enum PayUnpCfgAccountTypeEnum implements IEnum {
	/** 企业 */
	COMPANY("01", "企业"),
	/** 个人 */
	INDIVIDUAL("02", "个人");
	
	PayUnpCfgAccountTypeEnum(String code, String desc) {
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
