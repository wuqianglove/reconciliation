package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 缴费单业务类型枚举类
 * @author ZhangSC
 * 2017-04-27
 * */
public enum PayPaymentBillBusinessTypeEnum implements IEnum {
	/**港口建设费 */
	PORTCONSTRUCTIONFEE("01", "港口建设费"),
	/**罚没收入费 */
	PENALTYANDCONFISCATORYINCOME("02", "罚没收入费"),
	/**船员考试费 */
	EXAMINATIONFORTHECREW("03", "船员考试费"),
	/**注册验船师 */
	REGISTEREDSURVEYOR("04", "注册验船师费"),
	/**注册验船师 */
	OILPOLLUTION("05", "油污基金收入费");
	
	PayPaymentBillBusinessTypeEnum(String code, String desc) {
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
