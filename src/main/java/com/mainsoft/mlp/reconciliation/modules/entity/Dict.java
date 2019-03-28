/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import org.hibernate.validator.constraints.Length;
import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;

/**
 * 字典Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Dict extends DataEntity<Dict> {

	private static final long serialVersionUID = 1L;
	private String value;	// 数据值
	private String label;	// 标签名
	private String type;	// 类型
	private String description;// 描述
	private Integer sort;	// 排序
	private String parentId;//父Id
	private Date beginDate;		//开始时间
	private Date endDate;		//结束时间
	private Integer createCode;	//创建人编码
	private Integer updateCode;	//修改者编码
	
	public Dict() {
		super();
	}

	public Dict(String id){
		super(id);
	}
	
	public Dict(String value, String label){
		this.value = value;
		this.label = label;
	}
	
	public String getId() {
		return id;
	}
	
	@XmlAttribute
	@Length(min=1, max=100)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlAttribute
	@Length(min=1, max=100)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Length(min=1, max=100)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	@Length(min=0, max=100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Length(min=1, max=100)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Override
	public String toString() {
		return label;
	}
	
	public Date getBeginDate() {
		return beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
	public Integer getCreateCode() {
		return createCode;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public Integer getUpdateCode() {
		return updateCode;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public String getDelFlag() {
		return delFlag;
	}
	
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setCreateCode(Integer createCode) {
		this.createCode = createCode;
	}

	public void setUpdateCode(Integer updateCode) {
		this.updateCode = updateCode;
	}
}