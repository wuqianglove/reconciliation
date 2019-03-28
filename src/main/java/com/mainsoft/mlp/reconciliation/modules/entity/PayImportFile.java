/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import java.util.Date;
import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mainsoft.mlp.common.utils.DateUtils;


/**
 * 导入文件Entity
 * @author ZhangSC
 * @version 2017-04-01
 */
public class PayImportFile extends DataEntity<PayImportFile> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 对账文件类型（0101，中国银联教育平台；0102，中国银联非教育平台；02，东方电子；03，银联在线 ）
	private String name;		// 文件名称
	private Date createTime;		// 生成时间
	private Date importTime;		// 入库时间
	private Date handleTime;		// 解析时间
	private String content;		// 文件内容
	private String status;		// 处理状态（00，未处理；01，处理成功；02，处理失败）
	private String message;		// 处理消息
	private Date settleDate;    //对账的日期

	private Date beginSettleDate;  //开始清算日期
	private Date endSettleDate;   //结束清算日期
	private Date beginCreateDate;  //开始生成日期
	private Date endCreateDate;  //结束生成日期
	
	//2018.06.11 guodongyu 添加对账文件业务类型（01：港建费，02：规费）
	private String paymentType;   //对账文件业务类型（01：港建费，02：规费）
	private String merchant;  //商户号
	
	public PayImportFile() {
		super();
	}

	public PayImportFile(String id){
		super(id);
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}

	public Date getBeginSettleDate() {
		return beginSettleDate;
	}

	public Date getEndSettleDate() {
		return endSettleDate;
	}

	public void setBeginSettleDate(Date beginSettleDate) {
		this.beginSettleDate = DateUtils.getMinDateOfDay(beginSettleDate);
	}

	public void setEndSettleDate(Date endSettleDate) {
		this.endSettleDate = DateUtils.getMinDateOfTheNextDay(endSettleDate);
	}

	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = DateUtils.getMinDateOfDay(beginCreateDate);
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = DateUtils.getMinDateOfTheNextDay(endCreateDate);
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	
}