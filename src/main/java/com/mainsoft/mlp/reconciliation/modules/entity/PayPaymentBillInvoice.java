/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import java.math.BigDecimal;



import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 缴费单收据Entity
 * @author ZhangSC
 * @version 2017-03-29
 */
public class PayPaymentBillInvoice extends DataEntity<PayPaymentBillInvoice> {
	
	private static final long serialVersionUID = 1L;
	private String species;		// 收据类型
	private BigDecimal numeral;		// 收据票号
	private String status;		// 收据状态（01，空白；02，填用；03，作废；04，丢失）
	private String payer;		// 缴费人
	private BigDecimal revenue;		// 金额合计
	
	private String oldSpecies;
	private BigDecimal oldNumeral;	
	public PayPaymentBillInvoice() {
		super();
	}

	public PayPaymentBillInvoice(String id){
		super(id);
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}
	
	public BigDecimal getNumeral() {
		return numeral;
	}

	public void setNumeral(BigDecimal numeral) {
		this.numeral = numeral;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}
	
	public BigDecimal getRevenue() {
		return revenue;
	}

	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}

	public String getOldSpecies() {
		return oldSpecies;
	}

	public void setOldSpecies(String oldSpecies) {
		this.oldSpecies = oldSpecies;
	}

	public BigDecimal getOldNumeral() {
		return oldNumeral;
	}

	public void setOldNumeral(BigDecimal oldNumeral) {
		this.oldNumeral = oldNumeral;
	}
	
	
}