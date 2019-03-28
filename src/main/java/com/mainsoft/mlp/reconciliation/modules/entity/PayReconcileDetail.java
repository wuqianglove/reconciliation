/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import java.util.Date;

import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 银行在线对账明细Entity
 * @author Guody
 * @version 2017-09-12
 */
public class PayReconcileDetail extends DataEntity<PayReconcileDetail> {
	
	private static final long serialVersionUID = 1L;
	private Integer ordinal;		// 明细序号
	private String payTime;		// 交易时间（定长，例如：20061103115959）
	private String channel;		// 缴费渠道（01:Chinapay，02:第三方支付,03:银联在线Unionpay,04:中行终端,05:微信支付，06,POS机,07:东方支付）
	private String merchant;		// 商户号码（定长，例如：808080290000001）
	private String bizTypeCode;		// 业务类型编码
	private String bizTypeName;		// 业务类型名称
	private String payerCode;		// 付款方组织机构代码
	private String payerName;		// 付款方名称
	private String payeeCode;		// 收款方组织机构代码
	private String payeeAccount;		// 收款账号
	private String accountBank;		// 开户银行
	private String accountName;		// 开户人姓名
	private String payerAccount;		// 付款方付款账号
	private String orderCode;		// 订单编号（若为退款、退款撤销交易，该域填入原始支付交易订单号）
	private String transType;		// 交易类型（0001，消费交易；0002，退款；0005，退款撤销（非教育）；）
	private String transAmount;		// 交易金额（单位分，不足位数是左补0，例如：000000000001
	private String transStatus;		// 交易状态（非教育：当TransType=0001 时，该域为1001的表示成功支付,1111表示支付未成功；当TransType=0002时，该域为1003；当TransType=0005时，该域为1005）（教育：当Type为0001时，1001表示支付成功，其余均为支付失败；当Type为0002时，2表示退款成功，8为退款失败；）
	private String transDate;		// 商户日期（例如：20061103）（非教育平台特有
	private String gateCode;		// 交易网关（例如：0001）
	private String currency;		// 交易货币（156）
	private String cpDate;		// CP日期（例如：20061103）（非教育平台特有）
	private String paymentNo;		// 支付号（东方支付特有），如果存在补付的情况，沿用前次的支付号
	private String cpSeqId;		// CP流水号(非教育平台（例如：000123）教育平台是16位)
	private String priv1;		// 商户私有域（缴费单号）
	private String checkValue;		// 签名
	private String message;		// 处理消息
	private Integer success;		// 是否经过对账
	private String version;		// 版本（教育平台特有
	private Integer sync;		//是否同步到内网
	
	//东方账单相关字段
	private Date sendTime;  //报文发送时间
	
	public PayReconcileDetail() {
		super();
	}

	public PayReconcileDetail(String id){
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
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	
	public String getBizTypeCode() {
		return bizTypeCode;
	}

	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}
	
	public String getBizTypeName() {
		return bizTypeName;
	}

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}
	
	public String getPayerCode() {
		return payerCode;
	}

	public void setPayerCode(String payerCode) {
		this.payerCode = payerCode;
	}
	
	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	
	public String getPayeeCode() {
		return payeeCode;
	}

	public void setPayeeCode(String payeeCode) {
		this.payeeCode = payeeCode;
	}
	
	public String getPayeeAccount() {
		return payeeAccount;
	}

	public void setPayeeAccount(String payeeAccount) {
		this.payeeAccount = payeeAccount;
	}
	
	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getPayerAccount() {
		return payerAccount;
	}

	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
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
	
	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
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

	public Integer getSync() {
		return sync;
	}

	public void setSync(Integer sync) {
		this.sync = sync;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
}