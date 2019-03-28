/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mainsoft.mlp.common.utils.DateUtils;
import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 银行在线对账Entity
 * @author Guody
 * @version 2017-09-12
 */
public class PayReconcile extends DataEntity<PayReconcile> {
	
	private static final long serialVersionUID = 1L;
	private String channel;		// 缴费渠道（01:Chinapay，02:第三方支付,03:银联在线Unionpay,04:中行终端,05:微信支付，06,POS机,07:东方支付）
	private String merchant;		// 商户编号
	private Integer payTotalCount;		// 全部支付笔数
	private BigDecimal payTotalAmount;		// 支付金额
	private Integer paySuccessCount;		// 成功支付笔数
	private BigDecimal paySuccessAmount;		// 成功支付金额
	private Integer refundCount;		// 退款笔数
	private BigDecimal refundAmount;		// 退款金额
	private Integer refundCancelCount;		// 退款撤销笔数
	private BigDecimal refundCancelAmount;		// 退款撤销金额
	private Integer splitType;		// 是否分账
	private Date transDate;		// 交易日期
	private Date beginTransDate; //开始交易时间
	private Date endTransDate;	 //结束交易时间
	private Integer success;		// 是否对账完成
	private String message;		// 处理消息
	private Integer sync;		//是否同步到内网
	private List<PayReconcileDetail> payReconcileDetailList;
	private Map<String, String> requestMap;
	
	//东方支付相关字段
	
	
	public PayReconcile() {
		super();
	}

	public PayReconcile(String id){
		super(id);
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
	
	public Integer getPayTotalCount() {
		return payTotalCount;
	}

	public void setPayTotalCount(Integer payTotalCount) {
		this.payTotalCount = payTotalCount;
	}
	
	public BigDecimal getPayTotalAmount() {
		return payTotalAmount;
	}

	public void setPayTotalAmount(BigDecimal payTotalAmount) {
		this.payTotalAmount = payTotalAmount;
	}
	
	public Integer getPaySuccessCount() {
		return paySuccessCount;
	}

	public void setPaySuccessCount(Integer paySuccessCount) {
		this.paySuccessCount = paySuccessCount;
	}
	
	public BigDecimal getPaySuccessAmount() {
		return paySuccessAmount;
	}

	public void setPaySuccessAmount(BigDecimal paySuccessAmount) {
		this.paySuccessAmount = paySuccessAmount;
	}
	
	public Integer getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(Integer refundCount) {
		this.refundCount = refundCount;
	}
	
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	public Integer getRefundCancelCount() {
		return refundCancelCount;
	}

	public void setRefundCancelCount(Integer refundCancelCount) {
		this.refundCancelCount = refundCancelCount;
	}
	
	public BigDecimal getRefundCancelAmount() {
		return refundCancelAmount;
	}

	public void setRefundCancelAmount(BigDecimal refundCancelAmount) {
		this.refundCancelAmount = refundCancelAmount;
	}
	
	public Integer getSplitType() {
		return splitType;
	}

	public void setSplitType(Integer splitType) {
		this.splitType = splitType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	
	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<PayReconcileDetail> getPayReconcileDetailList() {
		return payReconcileDetailList;
	}

	public void setPayReconcileDetailList(
			List<PayReconcileDetail> payReconcileDetailList) {
		this.payReconcileDetailList = payReconcileDetailList;
	}

	public Integer getSync() {
		return sync;
	}

	public void setSync(Integer sync) {
		this.sync = sync;
	}

	public Date getBeginTransDate() {
		return beginTransDate;
	}

	public void setBeginTransDate(Date beginTransDate) {
		this.beginTransDate = DateUtils.getMinDateOfDay(beginTransDate);
	}

	public Date getEndTransDate() {
		return endTransDate;
	}

	public void setEndTransDate(Date endTransDate) {
		this.endTransDate = DateUtils.getMinDateOfTheNextDay(endTransDate);
	}

	public Map<String, String> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(Map<String, String> requestMap) {
		this.requestMap = requestMap;
	}
	
}