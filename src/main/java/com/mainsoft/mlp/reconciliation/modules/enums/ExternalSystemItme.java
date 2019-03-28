package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

public enum ExternalSystemItme implements IEnum{
	
	
	/** 船员考试系统 */
	EXAMSYSTEM("01", "船员考试系统"),
	/** 罚没收入系统 */
	PENALTYSYSTEM("02", "罚没收入系统");
	
	

	/** 构造函数 */
	ExternalSystemItme(String code, String desc) {
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
