/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 关联支付单与缴费单Entity
 * @author guody
 * @version 2017-10-18
 */
public class PayDefrayPayment extends DataEntity<PayDefrayPayment> {
	
	private static final long serialVersionUID = 1L;
	private String defrayId;		// 支付单编码（PD-辖区编码-九位序数）
	private String paymentId;		// 缴费单编码（PP-辖区编码-九位序数）
	private String status;		// 支付单状态（01，支付成功；02，需要退款）
	private String manage;		// 处理状态（01，）未处理；02已处理
	
	public PayDefrayPayment() {
		super();
	}

	public PayDefrayPayment(String id){
		super(id);
	}

	public String getDefrayId() {
		return defrayId;
	}

	public void setDefrayId(String defrayId) {
		this.defrayId = defrayId;
	}
	
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getManage() {
		return manage;
	}

	public void setManage(String manage) {
		this.manage = manage;
	}
	
}