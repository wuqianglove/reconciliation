/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayPaymentBill;
import org.springframework.stereotype.Repository;

/**
 * 缴费单DAO接口
 * @author ZhangSC
 * @version 2017-03-28
 */

@Repository
public interface PayPaymentBillDao extends CrudDao<PayPaymentBill> {
	/**
	 * 更改缴费单的支付单号
	 * @param payPaymentBill
	 */
	public void updateDefrayNo(PayPaymentBill payPaymentBill);
	
	/**
	 * 查找缴费单用于webserivce查看
	 */
	public List<PayPaymentBill> findPageForWebService(PayPaymentBill payPaymentBill);
	
	/**
	 * 查找没有邮寄单的缴费单用于webserivce查看
	 */
	public List<PayPaymentBill> findNoEnvelopPaymentBillListForWebService(PayPaymentBill payPaymentBill);
	
	/**
	 * 查询港口建设费的缴费单信息， 如果需要分页，请设置page对象
	 * @param payPaymentBill
	 * @return
	 */
	public List<PayPaymentBill> findListByPortConstructionFee(PayPaymentBill payPaymentBill);
	
	/**
	 * 更新对账状态
	 * @param payPaymentBill
	 */
	public void updVerifyStatus(PayPaymentBill payPaymentBill);
	/**
	 * 根据缴费单集合，返回缴费单信息集合
	 * @param paymentIds
	 * @return
	 */
	public List<PayPaymentBill> findPaymentBillListByIds(@Param(value = "paymentIds") List<String> paymentIds);
	
	/**
	 * 2017.10.18 guody
	 * 检验缴费单集合中，是否含有已经缴费或者已经作废的缴费单
	 * @param paymentIds
	 */
	public Integer checkPaymentBillListForPayStatus(@Param(value = "paymentIds") List<String> paymentIds);

	
	/**
	 * 根据外部来源编码查询对应的缴费单信息
	 * 2018.02.23 guodongyu
	 * @param origin
	 * @return PayPaymentBill
	 */
	public PayPaymentBill findPaymentBillByorigin(@Param(value = "origin") String origin);

	//作废相应缴费单 支撑内网取消确认收费操作
	public void cancellationPaymentBill(@Param(value = "payBillList") List<String> payBillList, @Param(value = "remarks") String remarks);
}