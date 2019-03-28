/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.entity;

import com.mainsoft.mlp.reconciliation.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保存微信对账记录Entity
 * @author guodongyu
 * @version 2019-03-28
 */
public class PayWcReconcile extends DataEntity<PayWcReconcile> {
	
	private static final long serialVersionUID = 1L;
	private String merchant;		// 商户编号
	private Integer payTotalCount;		// 总交易单数
	private BigDecimal payTotalAmount;		// 总交易额
	private BigDecimal refundAmount;		// 总退款金额
	private BigDecimal discountsrefundAmount;		// 总代金券或立减优惠退款金额
	private BigDecimal serviceAmount;		// 手续费总金额
	private String billType;		// 账单类型
	private String billDate;		// 对账单日期
	private Integer success;		// 是否对账完成
	private String message;		// 处理消息
	private List<PayWcReconcileDetail> payWcReconcileDetailList;
	
	public PayWcReconcile() {
		super();
	}

	public PayWcReconcile(String id){
		super(id);
	}

	@Length(min=1, max=20, message="商户编号长度必须介于 1 和 20 之间")
	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	
	public Integer getPayTotalCount() {
		return payTotalCount;
	}

	public void setPayTotalCount(Integer payTotalCount) {
		this.payTotalCount = payTotalCount;
	}
	

	

	
	@Length(min=0, max=4, message="账单类型长度必须介于 0 和 4 之间")
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@Length(min=0, max=20, message="对账单日期长度必须介于 0 和 20 之间")
	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	
	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}
	
	@Length(min=0, max=250, message="处理消息长度必须介于 0 和 250 之间")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    public List<PayWcReconcileDetail> getPayWcReconcileDetailList() {
        return payWcReconcileDetailList;
    }

    public void setPayWcReconcileDetailList(List<PayWcReconcileDetail> payWcReconcileDetailList) {
        this.payWcReconcileDetailList = payWcReconcileDetailList;
    }

    public BigDecimal getPayTotalAmount() {
        return payTotalAmount;
    }

    public void setPayTotalAmount(BigDecimal payTotalAmount) {
        this.payTotalAmount = payTotalAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getDiscountsrefundAmount() {
        return discountsrefundAmount;
    }

    public void setDiscountsrefundAmount(BigDecimal discountsrefundAmount) {
        this.discountsrefundAmount = discountsrefundAmount;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }
}