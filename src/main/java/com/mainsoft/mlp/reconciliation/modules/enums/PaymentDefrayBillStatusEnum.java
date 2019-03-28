package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 缴费单支付状态枚举
 * @author ZhangSC
 * 2017-04-11
 * */
public enum PaymentDefrayBillStatusEnum implements IEnum {
	/** 未支付 */
	NOT_PAYMENT("01", "未支付"),
	/** 已支付 */
	ALREADY_PAID("02", "已支付"),
	/** 支付失败 */
	PAID_FAIL("03", "支付失败"),
	/** 已经受理 */
	ALREADY_ACCEPT("04", "已经受理");
	
	PaymentDefrayBillStatusEnum(String code, String desc) {
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
