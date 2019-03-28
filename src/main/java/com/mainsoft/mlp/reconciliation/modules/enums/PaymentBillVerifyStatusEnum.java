package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 缴费单对账状态枚举
 * @author ZhangSC
 * 2017-04-11
 * */
public enum PaymentBillVerifyStatusEnum implements IEnum {
	/** 尚未对账 */
	NOT_VERIFY("01", "尚未对账"),
	/** 对账成功 */
	VERIFIED_SUCCESS("02", "对账成功"),
	/** 对账失败 */
	VERIFIED_FAIL("03", "对账失败");
	
	PaymentBillVerifyStatusEnum(String code, String desc) {
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
