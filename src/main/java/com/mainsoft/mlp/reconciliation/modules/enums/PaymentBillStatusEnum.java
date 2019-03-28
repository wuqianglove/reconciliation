package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 缴费单支付状态枚举
 * @author ZhangSC
 * 2017-04-11
 * */
public enum PaymentBillStatusEnum implements IEnum {
	/** 未支付 */
	NOT_PAYMENT("01", "未支付"),
	/** 已支付 */
	ALREADY_PAID("02", "已支付"),
	/** 已作废 */
	ALREADY_INVALID("03", "已作废"),
	/** 无需支付 */
	UNNEED_PAY("04", "无需支付");
	
	PaymentBillStatusEnum(String code, String desc) {
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
