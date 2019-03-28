/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;


import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 缴费单凭据Entity
 * @author ZhangSC
 * @version 2017-03-28
 */
public class PayPaymentBillFollow extends DataEntity<PayPaymentBillFollow> {
	
	private static final long serialVersionUID = 1L;
	private String avenue;		// 领取方式（01，现场领取；02，收据邮寄）
	private String title;		// 收据抬头
	private String address;		// 邮寄地址
	private String postcode;		// 邮政编码
	private String receiver;		// 收件人名
	private String telephone;		// 联系电话
	private Integer invoice;		// 是否开票(1,是；0，否)
	private String envelope;		// 信封编码
	
	//2018.04.26 guodongyu 添加领取票据机构点字段
	private String invoiceOffice;
	//2018.05.02 guodongyu 添加领票机构名称显示
	private String offName;
	
	public PayPaymentBillFollow() {
		super();
	}
	
	public PayPaymentBillFollow(String title, String address, String postcode, String receiver, String telephone, String envelope){
		super();
	}
	
	public PayPaymentBillFollow(String id){
		super(id);
	}

	public String getAvenue() {
		return avenue;
	}

	public void setAvenue(String avenue) {
		this.avenue = avenue;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public Integer getInvoice() {
		return invoice;
	}

	public void setInvoice(Integer invoice) {
		this.invoice = invoice;
	}
	
	public String getEnvelope() {
		return envelope;
	}

	public void setEnvelope(String envelope) {
		this.envelope = envelope;
	}

	public String getInvoiceOffice() {
		return invoiceOffice;
	}

	public void setInvoiceOffice(String invoiceOffice) {
		this.invoiceOffice = invoiceOffice;
	}

	public String getOffName() {
		return offName;
	}

	public void setOffName(String offName) {
		this.offName = offName;
	}

}