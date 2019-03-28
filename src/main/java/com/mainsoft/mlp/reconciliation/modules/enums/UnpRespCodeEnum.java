package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 银联应答码枚举类  支撑银联查询交易返回响应对比
 * @author guody
 * @date 2018年3月1日
 */
public enum UnpRespCodeEnum implements IEnum{

	/**
	 * 交易应答码
	 */
	/** 交易成功00 */
	SUCCEED("00", "交易成功"),
	/** 交易成功A6 */
	SUCCEEDOTHER("A6", "交易成功"),
	/** 订单处理中 */
	DISPOSINGONE("03", "订单处理中"),
	/** 订单处理中 */
	DISPOSINGTWO("04", "订单处理中"),
	/** 订单处理中 */
	DISPOSINGTHREE("05", "订单处理中"),
	/** 订单不存在 */
	INEXISTENCE("34", "订单处理中");
	
	
	UnpRespCodeEnum(String code, String desc) {
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
