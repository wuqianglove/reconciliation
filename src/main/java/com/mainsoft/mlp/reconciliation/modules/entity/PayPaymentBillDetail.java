/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import java.math.BigDecimal;
import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 缴费单明细Entity
 * @author ZhangSC
 * @version 2017-03-29
 */
public class PayPaymentBillDetail extends DataEntity<PayPaymentBillDetail> {
	
	private static final long serialVersionUID = 1L;
	private Integer ordinal;		// 明细序号（从1开始）
	private String manifest;		// 舱单编码
	private String sheet;		// 提运单号
	private BigDecimal revenue;		// 缴费金额
	private String intent;		// 业务类型（01，出口国外；02，国外进口；03，国内出口；04，国内进口）
	private Integer container;		// 货物类型（0，散杂货；1，集装箱）
	private String consignee;		// 收货人
	private String shipper;		// 托运人
	private String vesselName;		// 船舶名称
	private String voyage;		// 航次编号
	
	
	public PayPaymentBillDetail() {
		super();
	}

	public PayPaymentBillDetail(String id){
		super(id);
	}

	public PayPaymentBillDetail(String id, Integer ordinal, String manifest, String sheet, BigDecimal revenue, String intent, Integer container, String consignee, String shipper, String vesselName, String voyage){
		super();
		this.id = id;
		this.ordinal = ordinal;
		this.manifest = manifest;
		this.sheet = sheet;
		this.revenue = revenue;
		this.intent = intent;
		this.container = container;
		this.consignee = consignee;
		this.shipper = shipper;
		this.vesselName = vesselName;
		this.voyage = voyage;
	}
	
	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}
	
	public String getManifest() {
		return manifest;
	}

	public void setManifest(String manifest) {
		this.manifest = manifest;
	}
	
	public String getSheet() {
		return sheet;
	}

	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	
	public BigDecimal getRevenue() {
		return revenue;
	}

	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}
	
	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}
	
	public Integer getContainer() {
		return container;
	}

	public void setContainer(Integer container) {
		this.container = container;
	}
	
	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	
	public String getShipper() {
		return shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}
	
	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}
	
	public String getVoyage() {
		return voyage;
	}

	public void setVoyage(String voyage) {
		this.voyage = voyage;
	}

}