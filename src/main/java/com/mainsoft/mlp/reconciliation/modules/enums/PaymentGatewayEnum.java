package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 支付网关枚举类 支撑银联查询方法
 * @author guody
 * @date 2018年3月1日
 */
public enum PaymentGatewayEnum implements IEnum{
	/** 中国银联卡支付 */
	CHINA_CARD_PAY("0101", "中国银联卡支付"),
	/** 中国银联企业网银 */
	CHINA_COMPANY_PAY("0102", "中国银联企业网银"),
	/** 银联在线卡支付 b2c */
	UNION_CARD_PAY("000201", "银联在线卡支付"),
	/** 银联在线企业网银 b2b */
	UNION_COMPANY_PAY("000202", "银联在线企业网银"),
	/** 东方支付 */
	ECSPAY("03", "东方电子支付"),
	/** 易宝支付 */
	YEEPAY("06", "易宝支付");
	PaymentGatewayEnum(String code, String desc) {
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
