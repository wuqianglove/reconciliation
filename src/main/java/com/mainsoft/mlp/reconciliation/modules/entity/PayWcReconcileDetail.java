/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import javax.validation.constraints.NotNull;
import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 微信对账明细Entity
 * @author guodongyu
 * @version 2019-03-28
 */
public class PayWcReconcileDetail extends DataEntity<PayWcReconcileDetail> {
	
	private static final long serialVersionUID = 1L;
	private Integer ordinal;		// 明细序号
	private String payTime;		// 交易时间
	private String merchant;		// 商户号码
	private String orderCode;		// 收款流水号
	private String paymentBill;		// 缴费单号
	private String transType;		// 交易类型
	private String transAmount;		// 交易状态
	private String transStatus;		// 交易金额
	private String message;		// 处理消息
	private Integer success;		// 是否经过对账
	
	public PayWcReconcileDetail() {
		super();
	}

	public PayWcReconcileDetail(String id){
		super(id);
	}

	@NotNull(message="明细序号不能为空")
	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}
	
	@Length(min=0, max=30, message="交易时间长度必须介于 0 和 30 之间")
	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
	@Length(min=0, max=15, message="商户号码长度必须介于 0 和 15 之间")
	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	
	@Length(min=0, max=32, message="收款流水号长度必须介于 0 和 32 之间")
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	@Length(min=0, max=20, message="缴费单号长度必须介于 0 和 20 之间")
	public String getPaymentBill() {
		return paymentBill;
	}

	public void setPaymentBill(String paymentBill) {
		this.paymentBill = paymentBill;
	}
	
	@Length(min=0, max=4, message="交易类型长度必须介于 0 和 4 之间")
	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	@Length(min=0, max=12, message="交易状态长度必须介于 0 和 12 之间")
	public String getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	
	@Length(min=0, max=4, message="交易金额长度必须介于 0 和 4 之间")
	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	
	@Length(min=0, max=20, message="处理消息长度必须介于 0 和 20 之间")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@NotNull(message="是否经过对账不能为空")
	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}
	
}