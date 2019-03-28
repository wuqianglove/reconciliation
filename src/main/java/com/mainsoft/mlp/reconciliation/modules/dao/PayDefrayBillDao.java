/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayDefrayBill;
import org.springframework.stereotype.Repository;

/**
 * 支付单DAO接口
 * @author ZhangSC
 * @version 2017-04-01
 */
@Repository
public interface PayDefrayBillDao extends CrudDao<PayDefrayBill> {
	/**
	 * 根据缴费单号获取支付单
	 * @param paymentBillNo
	 * @return
	 */
	public PayDefrayBill getPayDefrayBillByPaymentBill(@Param("paymentBillNo") String paymentBillNo);

	/**
	 * 更改需要退费的支付单  退费处理标记
	 * @param payDefrayBill
	 */
	public void managePay(PayDefrayBill payDefrayBill);

	public void saveForExternal(PayDefrayBill payDefrayBill);

	//作废相应缴费单 支撑内网取消确认收费操作
	public void cancellationPayDefrayBill(@Param("payDefrayBillList") List<String> payDefrayBillList, @Param("remarks") String remarks);

	//根据PayDefrayBillList查询对应的支付单信息
	public List<PayDefrayBill> findListByPayDefrayBillList(PayDefrayBill payDefrayBill);
	
	public List<PayDefrayBill> findConfiscateList(PayDefrayBill payDefrayBill);
	
	public List<PayDefrayBill> findExamList(PayDefrayBill payDefrayBill);

	List<PayDefrayBill> findListByOrderCodeList(PayDefrayBill payDefrayBill);
}