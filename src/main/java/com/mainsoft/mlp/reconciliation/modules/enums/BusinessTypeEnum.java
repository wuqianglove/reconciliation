package com.mainsoft.mlp.reconciliation.modules.enums;

import com.mainsoft.mlp.common.enums.IEnum;

/**
 * 业务类型枚举类
 * @author chenzw
 * @version 2016-10-12
 */
public enum BusinessTypeEnum implements IEnum{
	
	/** 船舶登记 */
	VESSEL_REGISTER("0201", "船舶登记"),
	/** 船员考试 */
	CREW_EXAM("0202", "船员考试"),
	/** 船舶检验 */
	VESSEL_INSPECT("04", "船舶检验"),
	/** 罚没收入 */
	PENALTY_INCOME("05", "罚没收入"),
	/** 港口建设费 */
	PORT_BUILD_FEE("06", "港口建设费"),
	/** 车船税 */
	VEHICLE_AND_VESSEL_TAX("07", "车船税"),
	/** 油污基金 */
	GREASY_DIRT_FUND("08", "油污基金"),
	/** 港口建设费滞纳金 */
	PORT_BUILD_LATE_FEE("0610", "港口建设费滞纳金"),
	/** 油污基金滞纳金 */
	GREASY_DIRT_FUND_LATE_FEE("0802", "油污基金滞纳金");

	/** 构造函数 */
	BusinessTypeEnum(String code, String desc) {
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
