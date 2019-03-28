package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 交易流水交易状态枚举
 * @author ZhangSC
 * 2017-04-11
 * */
public enum PaymentDealingStatusEnum implements IEnum {
	/** 支付成功 */
	PAYMENT_SUCCESS("1001","支付成功");
	
	PaymentDealingStatusEnum(String code, String desc) {
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
