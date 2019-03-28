/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayDefrayPayment;
import org.springframework.stereotype.Repository;

/**
 * 关联支付单与缴费单DAO接口
 * @author guody
 * @version 2017-10-18
 */
@Repository
public interface PayDefrayPaymentDao extends CrudDao<PayDefrayPayment> {
	//获得缴费单编码集合  返回值为List
	List<String> findPaymentBillIdList(String priv1);
	
	//更新关联表状态
	void updateStatus(PayDefrayPayment payDefrayPayment);

	//获得缴费单编码 支撑外部支付系统
	String findPaymentBillIdForExternal(String priv1);
	/**
	 *@author guody
	 *2017.11.30
	 * 通过支付单编码集合查询对应的缴费单编码集合
	 * @param payDefrayBillList
	 * @return
	 */
	List<String> findPaymentListByPayDefrayBillList(@Param("payDefrayBillList") List<String> payDefrayBillList);
	
}