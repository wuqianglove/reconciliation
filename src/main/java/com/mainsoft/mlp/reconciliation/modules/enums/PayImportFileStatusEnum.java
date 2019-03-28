package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 缴费单支付状态枚举
 * @author ZhangSC
 * 2017-04-11
 * */
public enum PayImportFileStatusEnum implements IEnum {
	/** 未处理 */
	NOT_HANDLE("01", "未处理"),
	/** 处理成功 */
	HANDLE_SUCCESS("02", "处理成功"),
	/** 处理失败 */
	HANDLE_FAIL("03", "处理失败");
	
	PayImportFileStatusEnum(String code, String desc) {
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
