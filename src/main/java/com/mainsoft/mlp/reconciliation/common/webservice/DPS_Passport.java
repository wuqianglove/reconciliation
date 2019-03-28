package com.mainsoft.mlp.reconciliation.common.webservice;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 通讯身份
 * 
 * @author czz
 * @version 2016-11-12
 */
public class DPS_Passport {
	private String supremeCode; // 最高机构编码
	private String supremeName; // 最高机构名称
	private String identity; 	// 身份认证信息

	/**
	 * 护照签发时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date issueTime;

	/**
	 * 初始化
	 */
	public DPS_Passport() {
	}

	/**
	 * 初始化
	 * 
	 * @param supremeCode
	 *            最高机构编码
	 * @param supremeName
	 *            最高机构名称
	 * @param issueTime
	 *            护照签发时间
	 */
	public DPS_Passport(String supremeCode, String supremeName, Date issueTime) {
		this.supremeCode = supremeCode;
		this.supremeName = supremeName;
		this.issueTime = issueTime;
	}

	/**
	 * 
	 * @param supremeCode
	 *            最高机构编码
	 * @param supremeName
	 *            最高机构名称
	 * @param identity
	 * @param issueTime
	 *            护照签发时间
	 */
	public DPS_Passport(String supremeCode, String supremeName, String identity, Date issueTime) {
		this.supremeCode = supremeCode;
		this.supremeName = supremeName;
		this.issueTime = issueTime;
		this.identity = identity;
	}

	/**
	 * 获取最高机构编码
	 * 
	 * @return 最高机构编码
	 */
	public String getSupremeCode() {
		return supremeCode;
	}

	/**
	 * 获取最高机构名称
	 * 
	 * @return 最高机构名称
	 */
	public String getSupremeName() {
		return supremeName;
	}

	/**
	 * 获取护照签发时间
	 * 
	 * @return 护照签发时间
	 */
	public Date getIssueTime() {
		return issueTime;
	}

	/**
	 * 设置 最高机构编码
	 * 
	 * @param supremeCode
	 *            最高机构编码
	 */
	public void setSupremeCode(String supremeCode) {
		this.supremeCode = supremeCode;
	}

	/**
	 * 设置 最高机构名称
	 * 
	 * @param supremeName
	 *            最高机构名称
	 */
	public void setSupremeName(String supremeName) {
		this.supremeName = supremeName;
	}

	/**
	 * 设置 护照签发时间
	 * 
	 * @param issueTime
	 *            护照签发时间
	 */
	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
}
