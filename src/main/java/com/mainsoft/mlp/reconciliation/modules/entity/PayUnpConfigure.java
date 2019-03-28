/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 银联在线配置Entity
 * @author ZhangSC
 * @version 2017-04-10
 */
public class PayUnpConfigure extends DataEntity<PayUnpConfigure> {
	
	private static final long serialVersionUID = 1L;
	private String district;		// 辖区编码
	private String accountType;		// 账户类型（01，企业；02，个人；03，手机用户）
	private String merchantCode;		// 商户编码
	private String merchantName;		// 商户名称
	private String merchantSimpleName;  //商户简称
	private String keyPwd;          //证书密码
	private Integer splitType;		// 是否分账
	private String splitCode;		// 分账代码
	private String publicKey;		// 公钥
	private String privateKey;		// 私钥
	private String businessType;    //业务类型 （支撑外部支付系统）
	
	public PayUnpConfigure() {
		super();
	}

	public PayUnpConfigure(String id){
		super(id);
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
	
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	
	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	public Integer getSplitType() {
		return splitType;
	}

	public void setSplitType(Integer splitType) {
		this.splitType = splitType;
	}
	
	public String getSplitCode() {
		return splitCode;
	}

	public void setSplitCode(String splitCode) {
		this.splitCode = splitCode;
	}
	
	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getMerchantSimpleName() {
		return merchantSimpleName;
	}

	public void setMerchantSimpleName(String merchantSimpleName) {
		this.merchantSimpleName = merchantSimpleName;
	}

	public String getKeyPwd() {
		return keyPwd;
	}

	public void setKeyPwd(String keyPwd) {
		this.keyPwd = keyPwd;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	
}