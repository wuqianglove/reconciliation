/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 银联在线对账明细Entity
 * @author ZhangSC
 * @version 2017-04-10
 */
public class PayUnpReconcileDetail extends DataEntity<PayUnpReconcileDetail> {
	
	private static final long serialVersionUID = 1L;
	private Integer ordinal;		// 明细序号
	private String payTime;		// 交易时间（定长，例如：20061103115959）
	private String merchant;		// 商户号码（定长，例如：808080290000001）
	private String orderCode;		// 订单编号（若为退款、退款撤销交易，该域填入原始支付交易订单号）
	private String transType;		// 交易类型（0001，消费交易；0002，退款；0005，退款撤销（非教育）；）
	private String transAmount;		// 交易金额（单位分，不足位数是左补0，例如：000000000001
	private String transStatus;		// 交易状态（非教育：当TransType=0001 时，该域为1001的表示成功支付,1111表示支付未成功；当TransType=0002时，该域为1003；当TransType=0005时，该域为1005）（教育：当Type为0001时，1001表示支付成功，其余均为支付失败；当Type为0002时，2表示退款成功，8为退款失败；）
	private String transDate;		// 商户日期（例如：20061103）（非教育平台特有
	private String gateCode;		// 交易网关（例如：0001）
	private String currency;		// 交易货币（156）
	private String cpDate;		// CP日期（例如：20061103）（非教育平台特有）
	private String cpSeqId;		// CP流水号(非教育平台（例如：000123）教育平台是16位)
	private String priv1;		// 商户私有域（缴费单号）
	private String checkValue;		// 签名
	private String message;		// 处理消息
	private Integer success;		// 是否经过对账
	private String version;		// 版本（教育平台特有
	
	public PayUnpReconcileDetail() {
		super();
	}

	public PayUnpReconcileDetail(String id){
		super(id);
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}
	
	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	public String getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	
	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	
	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	
	public String getGateCode() {
		return gateCode;
	}

	public void setGateCode(String gateCode) {
		this.gateCode = gateCode;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCpDate() {
		return cpDate;
	}

	public void setCpDate(String cpDate) {
		this.cpDate = cpDate;
	}
	
	public String getCpSeqId() {
		return cpSeqId;
	}

	public void setCpSeqId(String cpSeqId) {
		this.cpSeqId = cpSeqId;
	}
	
	public String getPriv1() {
		return priv1;
	}

	public void setPriv1(String priv1) {
		this.priv1 = priv1;
	}
	
	public String getCheckValue() {
		return checkValue;
	}

	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}