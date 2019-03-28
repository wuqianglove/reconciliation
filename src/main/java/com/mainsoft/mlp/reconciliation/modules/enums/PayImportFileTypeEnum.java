package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 对账文件类型枚举
 * @author ZhangSC
 * 2017-04-11
 * */
public enum PayImportFileTypeEnum implements IEnum {
	/** 中国银联教育平台 */
	CHINA_SPLIT_PAY("0101", "中国银联教育平台"),
	/** 中国银联非教育平台 */
	CHINA_NOSPLIT_PAY("0102", "中国银联非教育平台"),
	/** 东方电子 */
	ECS_PAY("02", "东方电子"),
	/** 银联在线 */
	UNION_PAY("03", "银联在线"),
    /** 中行终端 */
    BOC_PAY("04", "中行终端"),
    /** 微信支付 */
    WECHAT_PAY("05", "微信支付");
	
	PayImportFileTypeEnum(String code, String desc) {
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
