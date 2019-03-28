/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;


import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mainsoft.mlp.common.utils.DateUtils;
import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 银联在线对账Entity
 * @author ZhangSC
 * @version 2017-04-10
 */
public class PayUnpReconcile extends DataEntity<PayUnpReconcile> {
	
	private static final long serialVersionUID = 1L;
	private String merchant;		// 商户编号
	private Integer payTotalCount;		// 全部支付笔数
	private Double payTotalAmount;		// 支付金额
	private Integer paySuccessCount;		// 成功支付笔数
	private Double paySuccessAmount;		// 成功支付金额
	private Integer refundCount;		// 退款笔数
	private Double refundAmount;		// 退款金额
	private Integer refundCancelCount;		// 退款撤销笔数
	private Double refundCancelAmount;		// 退款撤销金额
	private Integer splitType;		// 是否分账
	private Date transDate;		// 交易日期
	private Integer success;		// 是否对账完成
	private String message;		// 处理消息
	
	private List<PayUnpReconcileDetail> payUnpReconcileDetailList;
	
	//2018.04.13 zhanghz 增加 手工对账时间段   用于手工对账
	private Date reconcileDateStart;
	private Date reconcileDateEnd;
	private String district;  //手工对账传入的辖区编码
	private String paymentType; //对账文件的业务类型（01：港建费，02：规费）
	private String districtName;  //辖区名称
	
	
	
	

	public PayUnpReconcile() {
		super();
	}

	public PayUnpReconcile(String id){
		super(id);
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public Date getReconcileDateStart() {
		return reconcileDateStart;
	}

	public void setReconcileDateStart(Date reconcileDateStart) {
		this.reconcileDateStart = DateUtils.getMinDateOfDay(reconcileDateStart);		
	}

	public Date getReconcileDateEnd() {
		return reconcileDateEnd;
	}

	public void setReconcileDateEnd(Date reconcileDateEnd) {		
		this.reconcileDateEnd = DateUtils.getMinDateOfTheNextDay(reconcileDateEnd);
	}
	public Integer getPayTotalCount() {
		return payTotalCount;
	}

	public void setPayTotalCount(Integer payTotalCount) {
		this.payTotalCount = payTotalCount;
	}
	
	public Double getPayTotalAmount() {
		return payTotalAmount;
	}

	public void setPayTotalAmount(Double payTotalAmount) {
		this.payTotalAmount = payTotalAmount;
	}
	
	public Integer getPaySuccessCount() {
		return paySuccessCount;
	}

	public void setPaySuccessCount(Integer paySuccessCount) {
		this.paySuccessCount = paySuccessCount;
	}
	
	public Double getPaySuccessAmount() {
		return paySuccessAmount;
	}

	public void setPaySuccessAmount(Double paySuccessAmount) {
		this.paySuccessAmount = paySuccessAmount;
	}
	
	public Integer getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(Integer refundCount) {
		this.refundCount = refundCount;
	}
	
	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	public Integer getRefundCancelCount() {
		return refundCancelCount;
	}

	public void setRefundCancelCount(Integer refundCancelCount) {
		this.refundCancelCount = refundCancelCount;
	}
	
	public Double getRefundCancelAmount() {
		return refundCancelAmount;
	}

	public void setRefundCancelAmount(Double refundCancelAmount) {
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

	public List<PayUnpReconcileDetail> getPayUnpReconcileDetailList() {
		return payUnpReconcileDetailList;
	}

	public void setPayUnpReconcileDetailList(
			List<PayUnpReconcileDetail> payUnpReconcileDetailList) {
		this.payUnpReconcileDetailList = payUnpReconcileDetailList;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	
}