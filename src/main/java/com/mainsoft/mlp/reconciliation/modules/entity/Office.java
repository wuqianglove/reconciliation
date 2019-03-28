/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import java.util.List;
import com.mainsoft.mlp.reconciliation.common.persistence.TreeEntity;

/**
 * 机构Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Office extends TreeEntity<Office> {

	private static final long serialVersionUID = 1L;
//	private Office parent;	// 父级编号
//	private String parentIds; // 所有父级编号
	private String onlineParentId;
	private String code; 	// 对应4A系统机构maritimeOrgCode
//	private String name; 	// 机构名称
//	private Integer sort;		// 排序
	private String type; 	// 机构类型（1：公司；2：部门；3：小组）
	private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	private String address; // 联系地址
	private String zipCode; // 邮政编码
	private String master; 	// 负责人
	private String phone; 	// 电话
	private String fax; 	// 传真
	private String email; 	// 邮箱
	private String useable;//是否可用
	private List<String> childDeptList;//快速添加子部门
	
	private String faOfficeCode;//4A系统部门编号
	
	private String gjfCode;//代收港建费系统的机构id
	//用于导出
	private String areaId;	// 归属区域ID
	protected String fsCode;    //对应非税机构
	private String isAcceptingInstitutionNo; //对应非税机构
	
	public Office(){
		super();
//		this.sort = 30;
		this.type = "1";
	}

	public Office(String id){
		super(id);
	}
	
	public List<String> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<String> childDeptList) {
		this.childDeptList = childDeptList;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}


	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getFaOfficeCode() {
		return faOfficeCode;
	}

	public void setFaOfficeCode(String faOfficeCode) {
		this.faOfficeCode = faOfficeCode;
	}
	
	public String getGjfCode() {
		return gjfCode;
	}

	public void setGjfCode(String gjfCode) {
		this.gjfCode = gjfCode;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getFsCode() {
		return fsCode;
	}

	public void setFsCode(String fsCode) {
		this.fsCode = fsCode;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getIsAcceptingInstitutionNo() {
		return isAcceptingInstitutionNo;
	}

	public void setIsAcceptingInstitutionNo(String isAcceptingInstitutionNo) {
		this.isAcceptingInstitutionNo = isAcceptingInstitutionNo;
	}

	public String getOnlineParentId() {
		return onlineParentId;
	}

	public void setOnlineParentId(String onlineParentId) {
		this.onlineParentId = onlineParentId;
	}

	
}