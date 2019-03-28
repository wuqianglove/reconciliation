/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 支付单Entity
 * @author ZhangSC
 * @version 2017-04-01
 */
public class PayDefrayBill extends DataEntity<PayDefrayBill> {
	
	private static final long serialVersionUID = 1L;
	private String paymentBill;		// 缴费单编码
	private Date finishTime;		// 支付完成时间
	private String status;		// 支付状态（01，尚未支付；02，已经支付；03，交易失败；04，已经受理）
	private Date submitTime;		// 支付发起时间
	private String merchant;		// 商户号码
	private String avenue;		// 支付途径（01，中国银联；02，东方电子；03，银联在线 ）
	private String orderCode;		// 订单编码
	private BigDecimal revenue;     //支付金额
	private String paymentBillIds; //缴费单编码（多个）
	private Date beginCreateDate;//开始创建时间
	private Date endCreateDate; //结束创建时间
	private String payDefrayPaymentStatus; //支付单与缴费单关联表支付状态
	
	private String payDefrayPaymentManage; //支付单与缴费单关联表支付单处理状态
	
	//2017.11.06 支付单表中新添 关于退款的三个字段
	private String refundFlag;//退款标记
	private String refundStatus;//退款处理状态
	private Date refundDate;//退款处理时间
	private List<String> payDefrayBillList;
	private List<String> orderCodeList;  //订单编码集合
	private String district;		// 所属辖区(2017.11.30 guodongyu 在支付单表中添加辖区字段)	
	private String businessType;      //业务类型	
	//2018.02.28 guodongyu 支付网关
	private String gateCode;
	
	public PayDefrayBill() {
		super();
	}

	public PayDefrayBill(String id){
		super(id);
	}

	public String getPaymentBill() {
		return paymentBill;
	}

	public void setPaymentBill(String paymentBill) {
		this.paymentBill = paymentBill;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	
	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	
	public String getAvenue() {
		return avenue;
	}

	public void setAvenue(String avenue) {
		this.avenue = avenue;
	}
	
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public BigDecimal getRevenue() {
		return revenue;
	}

	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}

	public String getPaymentBillIds() {
		return paymentBillIds;
	}

	public void setPaymentBillIds(String paymentBillIds) {
		this.paymentBillIds = paymentBillIds;
	}

	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}

	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public String getPayDefrayPaymentStatus() {
		return payDefrayPaymentStatus;
	}

	public void setPayDefrayPaymentStatus(String payDefrayPaymentStatus) {
		this.payDefrayPaymentStatus = payDefrayPaymentStatus;
	}

	public String getPayDefrayPaymentManage() {
		return payDefrayPaymentManage;
	}

	public void setPayDefrayPaymentManage(String payDefrayPaymentManage) {
		this.payDefrayPaymentManage = payDefrayPaymentManage;
	}

	public String getRefundFlag() {
		return refundFlag;
	}

	public void setRefundFlag(String refundFlag) {
		this.refundFlag = refundFlag;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}

	public List<String> getPayDefrayBillList() {
		return payDefrayBillList;
	}

	public void setPayDefrayBillList(List<String> payDefrayBillList) {
		this.payDefrayBillList = payDefrayBillList;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getGateCode() {
		return gateCode;
	}

	public void setGateCode(String gateCode) {
		this.gateCode = gateCode;
	}

    public List<String> getOrderCodeList() {
        return orderCodeList;
    }

    public void setOrderCodeList(List<String> orderCodeList) {
        this.orderCodeList = orderCodeList;
    }
}