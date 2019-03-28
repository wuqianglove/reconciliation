package com.mainsoft.mlp.reconciliation.common.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 业务状态类型枚举类
 * @author cuiyl
 * @version 2016-12-14
 */
public enum LclRuleCfgDistrictEnum implements IEnum{
	
	/** 人工调整主从 */
	MANUAL("MANUAL", "人工调整主从"),
	
	/** 先出现为主箱 */
	APPEAR("APPEAR", "先出现为主箱"),
	
	/** 先收费为主箱 */
	CHARGE("CHARGE", "先收费为主箱");

	/** 构造函数 */
	LclRuleCfgDistrictEnum(String code, String desc) {
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
