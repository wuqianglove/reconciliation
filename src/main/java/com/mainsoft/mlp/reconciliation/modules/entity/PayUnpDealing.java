/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 银联在线交易记录Entity
 * @author ZhangSC
 * @version 2017-04-10
 */
public class PayUnpDealing extends DataEntity<PayUnpDealing> {
	
	private static final long serialVersionUID = 1L;
	private String defrayBill;		// 支付单编码
	private String merchant;		// 商户号码
	private String orderCode;		// 订单编码
	private String amount;		// 交易金额
	private String transType;		// 交易类型（0001，支付或查询； 0002，退费）
	private String status;		// 交易状态（1001，支付交易成功；退费：1，退款提交成功，3，退款成功，8，退款失败）
	private String checkValue;		// 签名验证（256位字符）
	private String priv1;		// 商户私有域
	private String transDate;		// 交易日期
	private String currency;		// 货币类型
	private String gateCode;		// 支付网关
	private String responseCode;		// 应答编码（通常为0）
	private String processDate;		// 处理日期 (8位数字)
	private String sendTime;		// 应答时间（该域段不参与数字签名，6位数字，格式hhmmss）
	private String message;		// 应答信息
	private String shareData;		// 分账数据
	private String shareType;		// 分账类型
	private String payTime;		// 支付时间
	private String refundNum;		// 退款次数
	
	public PayUnpDealing() {
		super();
	}
	
	public PayUnpDealing(String merchant, String orderCode,String sendTime,
			String amount, String transType, String status, String checkValue,
			String priv1, String transDate, String currency, String gateCode, 
			String processDate, String payTime){
		super();
		this.sendTime = sendTime;
		this.merchant = merchant;
		this.orderCode = orderCode;
		this.amount = amount;
		this.transType = transType;
		this.status = status;
		this.priv1 = priv1;
		this.checkValue = checkValue;
		this.transDate = transDate;
		this.currency = currency;
		this.gateCode = gateCode;
		this.processDate = processDate;
		this.payTime = payTime;
	}

	public PayUnpDealing(String merchant, String orderCode,String sendTime,
			String amount, String transType, String status, 
			String priv1, String transDate, String currency, String gateCode, 
			String processDate, String payTime){
		super();
		this.sendTime = sendTime;
		this.merchant = merchant;
		this.orderCode = orderCode;
		this.amount = amount;
		this.transType = transType;
		this.status = status;
		this.priv1 = priv1;		
		this.transDate = transDate;
		this.currency = currency;
		this.gateCode = gateCode;
		this.processDate = processDate;
		this.payTime = payTime;
	}
	
	public PayUnpDealing(String id){
		super(id);
	}

	public String getDefrayBill() {
		return defrayBill;
	}

	public void setDefrayBill(String defrayBill) {
		this.defrayBill = defrayBill;
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
	
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getCheckValue() {
		return checkValue;
	}

	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}
	
	public String getPriv1() {
		return priv1;
	}

	public void setPriv1(String priv1) {
		this.priv1 = priv1;
	}
	
	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getGateCode() {
		return gateCode;
	}

	public void setGateCode(String gateCode) {
		this.gateCode = gateCode;
	}
	
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	public String getProcessDate() {
		return processDate;
	}

	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}
	
	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getShareData() {
		return shareData;
	}

	public void setShareData(String shareData) {
		this.shareData = shareData;
	}
	
	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
	}
	
	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
	public String getRefundNum() {
		return refundNum;
	}

	public void setRefundNum(String refundNum) {
		this.refundNum = refundNum;
	}
	
}