package com.mainsoft.mlp.reconciliation.common.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 编码生成业务枚举
 * @author chenzw
 * 2017-04-26
 * */
public enum CodeTargetEnum implements IEnum {
	
	/** 用于缴费单业务 */
	PAY_PAYMENT_BILL("SEQ_PAYMENT_BILL", "PP", "缴费单业务"),
	/** 用于支付单业务 */
	PAY_DEFRAY_BILL("SEQ_DEFRAY_BILL", "PD", "支付单业务");
	
	
	/** 编码 */
	private String code;
	
	/** 目标业务序列名前缀 */
	private String target;
	
	/** 说明 */
	private String desc;
	
	/** 构造函数 */
	CodeTargetEnum(String target, String code, String desc) {
		this.target = target;
		this.code = code;
		this.desc = desc;
	}
	

	/**
	 * 获取代码
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
	
	/**
	 * 获取目标业务表名
	 * */
	public String getTarget() {
		return this.target;
	}

}
