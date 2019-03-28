/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mainsoft.mlp.reconciliation.common.persistence.Page;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;
import com.mainsoft.mlp.reconciliation.modules.entity.PayDefrayPayment;
import com.mainsoft.mlp.reconciliation.modules.dao.PayDefrayPaymentDao;

/**
 * 关联支付单与缴费单Service
 * @author guody
 * @version 2017-10-18
 */
@Service
@Transactional(readOnly = true)
public class PayDefrayPaymentService extends CrudService<PayDefrayPaymentDao, PayDefrayPayment> {

	public PayDefrayPayment get(String id) {
		return super.get(id);
	}
	
	public List<PayDefrayPayment> findList(PayDefrayPayment payDefrayPayment) {
		return super.findList(payDefrayPayment);
	}
	
	public Page<PayDefrayPayment> findPage(Page<PayDefrayPayment> page, PayDefrayPayment payDefrayPayment) {
		return super.findPage(page, payDefrayPayment);
	}
	
	@Transactional(readOnly = false)
	public void save(PayDefrayPayment payDefrayPayment) {
		super.save(payDefrayPayment);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayDefrayPayment payDefrayPayment) {
		super.delete(payDefrayPayment);
	}
	

	/**
	 * 通过银行私有预中存储的支付单编码 在支付单与缴费单关联表中获得对应的缴费单String List集合
	 * @param priv1
	 */
	public List<String> findPaymentBillIdList(@Param(value="defpayId")String priv1) {
		 return dao.findPaymentBillIdList(priv1);
		
	}
	/**
	 * 通过银行私有预中存储的支付单编码 在支付单与缴费单关联表中获得对应的缴费单
	 * 支撑外部支付系统
	 * @param priv1
	 */
	public String findPaymentBillIdForExternal(@Param(value="defpayId")String priv1) {
		 return dao.findPaymentBillIdForExternal(priv1);
		
	}

	
	/**
	 * 通过支付单编码集合查询对应的缴费单编码集合
	 * guodongyu
	 * @param payDefrayBillList
	 * 2017.11.30
	 * @return
	 */
	public List<String> findPaymentListByPayDefrayBillList(List<String> payDefrayBillList) {
		 return dao.findPaymentListByPayDefrayBillList(payDefrayBillList);
		
	}
}