package com.mainsoft.mlp.reconciliation.common.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 数据来源类型枚举类
 * @author chenzw
 * @version 2016-10-10
 */
public enum DataSourceTypeEnum implements IEnum{
	
	/** 用户录入 */
	USER_INPUT("01", "用户录入"),
	/** 申报审批 */
	APPLY_APPROVE("02", "申报审批"),
	/** 内部导入 */
	INNER_INPUT("03", "内部导入"),
	/** 外部导入 */
	OUTER_IMPORT("04", "外部导入");

	/** 构造函数 */
	DataSourceTypeEnum(String code, String desc) {
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
