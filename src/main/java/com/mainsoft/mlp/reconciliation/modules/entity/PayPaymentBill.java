/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.mainsoft.mlp.common.utils.DateUtils;
import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 缴费单Entity
 * @author ZhangSC
 * @version 2017-03-28
 */
public class PayPaymentBill extends DataEntity<PayPaymentBill> {
	
	private static final long serialVersionUID = 1L;
	private String district;		// 所属辖区
	private String ownerCompanyId;		// 所属企业用户id
	private BigDecimal revenue;		// 缴费金额
	private String status;		// 缴费单状态（01，尚未支付；02，已经支付；03，已经作废；04，无需支付；）
	private String defrayBill;		// 有效支付单
	private String verify;		// 对账状态（01，尚未对账；02，对账成功；03，对账失败）
	private String origin;		// 来源缴费单
	private String businessType;  //业务类型（01，港口建设费）
	
	private List<PayPaymentBillDetail> paymentBillDetailList;   //缴费单明细
	private PayPaymentBillFollow payPaymentBillFollow;  //缴费单凭据
	private PayDefrayBill payDefrayBill;  //支付单
	
	private String payer; //支付人
	private Integer freightCount;  //提运单数量
	private Date beginCreateDate;//开始创建时间
	private Date endCreateDate; //结束创建时间
	private Date beginPayDate;//开始支付时间
	private Date endPayDate; //结束支付时间
	private Date finishDate;//支付完成时间
	private Date delayTime; //延时时间
	
	private Boolean isManifest; //来自舱单 用于页面返回按钮显示
	
	private List<String> paymentBillList;  //缴费单idList
	private String [] paymentBillIdList; //缴费单号数组
	private List<String> paymentBillIdsList; //缴费单号list集合
	private String paymentBillIds;  //缴费单号字符串（多个）
	private BigDecimal totalRevenue;  //缴费合计金额
	private Date finishTime;//支付完成时间  列表显示
	
	//VO
	private String voyage; //航次号码
	private String wharf; //码头锚地
	private String intent;//业务类型
	private String vesselName; //船舶名称
	private String nameCn;//中文船名
	private String nameEn;//英文船名
	private String codeIMO; //imo号
	private String identifier;//船舶识别码
	private Integer course; //特殊航线
	private Date manifestCreateDate; //舱单创建时间

	public PayPaymentBill() {
		super();
	}

	public PayPaymentBill(String id){
		super(id);
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
	
	public String getOwnerCompanyId() {
		return ownerCompanyId;
	}

	public void setOwnerCompanyId(String ownerCompanyId) {
		this.ownerCompanyId = ownerCompanyId;
	}
	
	public BigDecimal getRevenue() {
		return revenue;
	}

	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getDefrayBill() {
		return defrayBill;
	}

	public void setDefrayBill(String defrayBill) {
		this.defrayBill = defrayBill;
	}
	
	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}
	
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = DateUtils.getMinDateOfDay(beginCreateDate);
	}

	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = DateUtils.getMinDateOfTheNextDay(endCreateDate);
	}

	public Date getBeginPayDate() {
		return beginPayDate;
	}

	public void setBeginPayDate(Date beginPayDate) {
		this.beginPayDate = DateUtils.getMinDateOfDay(beginPayDate);
	}

	public Date getEndPayDate() {
		return endPayDate;
	}

	public void setEndPayDate(Date endPayDate) {
		this.endPayDate = DateUtils.getMinDateOfTheNextDay(endPayDate);
	}

	public List<PayPaymentBillDetail> getPaymentBillDetailList() {
		return paymentBillDetailList;
	}

	public void setPaymentBillDetailList(
			List<PayPaymentBillDetail> paymentBillDetailList) {
		this.paymentBillDetailList = paymentBillDetailList;
	}

	public PayPaymentBillFollow getPayPaymentBillFollow() {
		return payPaymentBillFollow;
	}

	public void setPayPaymentBillFollow(PayPaymentBillFollow payPaymentBillFollow) {
		this.payPaymentBillFollow = payPaymentBillFollow;
	}

	public Integer getFreightCount() {
		return freightCount;
	}

	public void setFreightCount(Integer freightCount) {
		this.freightCount = freightCount;
	}

	public PayDefrayBill getPayDefrayBill() {
		return payDefrayBill;
	}

	public void setPayDefrayBill(PayDefrayBill payDefrayBill) {
		this.payDefrayBill = payDefrayBill;
	}
	
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public List<String> getPaymentBillList() {
		return paymentBillList;
	}

	public void setPaymentBillList(List<String> paymentBillList) {
		this.paymentBillList = paymentBillList;
	}

	public Boolean getIsManifest() {
		return isManifest;
	}

	public void setIsManifest(Boolean isManifest) {
		this.isManifest = isManifest;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getPaymentBillIds() {
		return paymentBillIds;
	}

	public void setPaymentBillIds(String paymentBillIds) {
		this.paymentBillIds = paymentBillIds;
	}

	public String[] getPaymentBillIdList() {
		return paymentBillIdList;
	}

	public void setPaymentBillIdList(String[] paymentBillIdList) {
		this.paymentBillIdList = paymentBillIdList;
	}

	public List<String> getPaymentBillIdsList() {
		return paymentBillIdsList;
	}

	public void setPaymentBillIdsList(List<String> paymentBillIdsList) {
		this.paymentBillIdsList = paymentBillIdsList;
	}

	public BigDecimal getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(BigDecimal totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Date getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Date delayTime) {
		this.delayTime = delayTime;
	}

	public String getVoyage() {
		return voyage;
	}

	public void setVoyage(String voyage) {
		this.voyage = voyage;
	}

	public String getWharf() {
		return wharf;
	}

	public void setWharf(String wharf) {
		this.wharf = wharf;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getCodeIMO() {
		return codeIMO;
	}

	public void setCodeIMO(String codeIMO) {
		this.codeIMO = codeIMO;
	}

	public Integer getCourse() {
		return course;
	}

	public void setCourse(Integer course) {
		this.course = course;
	}

	public Date getManifestCreateDate() {
		return manifestCreateDate;
	}

	public void setManifestCreateDate(Date manifestCreateDate) {
		this.manifestCreateDate = manifestCreateDate;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
}