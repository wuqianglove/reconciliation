package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 支付单支付方式枚举
 * @author ZhangSC
 * 2017-04-11
 * */
public enum PayDefrayBillAvenueEnum implements IEnum {
	/** 中国银联支付 */
	CHINA_PAY("01", "中国银联"),
	/** 东方支付 */
	ECS_PAY("02", "东方电子支付"),
	/** 银联在线支付 */
	UNION_PAY("03", "银联在线"),
	/**微信支付**/
	WECHAT_PAY("05","微信支付"),
	/** 易宝支付 */
	YEE_PAY("06", "易宝支付");
	
	PayDefrayBillAvenueEnum(String code, String desc) {
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
