package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 对账逻辑相关枚举类
 * @author guody
 * @date 2018年6月12日
 */
public enum PaymentTypeForImportFileEnum implements IEnum{
	/** 港建费 */
	PORTPAYMENT("01", "港建费"),
	/** 规费 */
	FEESPAYMENT("02", "规费");
	
	

	/** 构造函数 */
	PaymentTypeForImportFileEnum(String code, String desc) {
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
