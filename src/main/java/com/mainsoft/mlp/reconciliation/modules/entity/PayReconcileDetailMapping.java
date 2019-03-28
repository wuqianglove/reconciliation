package com.mainsoft.mlp.reconciliation.modules.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.mainsoft.mlp.common.utils.DateUtils;
import com.mainsoft.mlp.reconciliation.common.config.Global;

/**
 * 支付对账entity
 * @author ZhangSC
 *
 */
public class PayReconcileDetailMapping implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;		//队长详情编号
	private Integer ordinal;		// 明细序号
	private String district;   //辖区编码
	private String districtName;   //辖区名称
	private Date transDate;		//交易日期
	private String transDateStr;  //交易日期字符串
	private BigDecimal revenue;   //支付金额
	private String revenueStr;    //支付金额字符串
	private String avenue;       //支付途径
	private String merId;		//商户号码
	private String orderId;		//订单id
	private String seqId;		//流水号
	private String paymentBill;	   //缴费单id
	private Integer success;			//对账状态
	private Date handlerDate;  		//操作时间
	private String message;  		//处理消息
	
	private Date beginReconcileDate;	//开始对账时间
	private Date endReconcileDate; 		//结束对账时间
	
	private String paydefrayBill;	   //支付单id
	
	//分页相关属性
	private Integer pageNo = 1;
	private Integer pageSize;
	private Integer total;
	
	/*
	 * 当支付途径为全部时，结果查询为中国银联对账和银联对账的并集，
	 * 这时我们规定先查的是中国银联对账，于是就可能会出现某一页中
	 * 一部分为中国银联对账，另一部分为银联对账的情况，这是我们需
	 * 要两个值来标记需要查询银联对账的范围
	 */
	private Integer startNum;
	private Integer endNum;
	
	//是否导出
	private String isExport;
	
	public Date getHandlerDate() {
		return handlerDate;
	}
	public Date getBeginReconcileDate() {
		return beginReconcileDate;
	}
	public Date getEndReconcileDate() {
		return endReconcileDate;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public void setTransDateStr(String transDateStr) {
		this.transDateStr = transDateStr;
	}
	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}
	public void setAvenue(String avenue) {
		this.avenue = avenue;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}
	public void setPaymentBill(String paymentBill) {
		this.paymentBill = paymentBill;
	}
	public void setHandlerDate(Date handlerDate) {
		this.handlerDate = handlerDate;
	}
	public void setBeginReconcileDate(Date beginReconcileDate) {
		this.beginReconcileDate = DateUtils.getMinDateOfDay(beginReconcileDate);
	}
	public void setEndReconcileDate(Date endReconcileDate) {
		this.endReconcileDate = DateUtils.getMinDateOfTheNextDay(endReconcileDate);
	}
	public String getId() {
		return id;
	}
	public Integer getOrdinal() {
		return ordinal;
	}
	public String getDistrict() {
		return district;
	}
	public Date getTransDate() {
		return transDate;
	}
	public String getTransDateStr() {
		return transDateStr;
	}
	public BigDecimal getRevenue() {
		return revenue;
	}
	public String getRevenueStr() {
		return revenueStr;
	}
	public String getAvenue() {
		return avenue;
	}
	public String getMerId() {
		return merId;
	}
	public String getOrderId() {
		return orderId;
	}
	public String getSeqId() {
		return seqId;
	}
	public String getPaymentBill() {
		return paymentBill;
	}
	public Integer getSuccess() {
		return success;
	}
	public void setRevenueStr(String revenueStr) {
		this.revenueStr = revenueStr;
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
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getPaydefrayBill() {
		return paydefrayBill;
	}
	public void setPaydefrayBill(String paydefrayBill) {
		this.paydefrayBill = paydefrayBill;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	public Integer getPageSize() {
		if(pageSize == null || pageSize == 0){
			pageSize = Integer.valueOf(Global.getConfig("page.pageSize"));
		}
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public Integer getStartNum() {
		return startNum;
	}
	public Integer getEndNum() {
		return endNum;
	}
	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}
	public void setEndNum(Integer endNum) {
		this.endNum = endNum;
	}
	
	public String getIsExport() {
		return isExport;
	}
	public void setIsExport(String isExport) {
		this.isExport = isExport;
	}
	
}
