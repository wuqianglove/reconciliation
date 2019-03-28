package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 查询交易结果状态枚举类 支撑网银查询接口
 * @author guody
 * @date 2018年3月1日
 */
public enum QueryRespStauseEnum implements IEnum{

	
	/** 交易成功01 */
	SUCCEED("01", "交易成功"),
	/** 交易处理中 */
	DISPOSING("02", "交易处理中，请稍后再试"),
	/** 交易失败 */
	FAILED("03", "交易失败"),
	/** 订单不存在 */
	INEXISTENCE("04", "订单不存在"),
	/** 查询方法失败，请检查查询报文是否正确 */
	QUERYFAILED("05", "查询方法失败，请检查查询报文是否正确"),
	/** 验证签名失败 */
	SIGNFAILED("06", "验证签名失败"),
	/** 查询接口不通 */
	NETWORKLOSE("07", " 调用银联查询接口不通 请稍后再试");
	
	
	
	QueryRespStauseEnum(String code, String desc) {
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
