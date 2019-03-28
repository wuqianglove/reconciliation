/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayPaymentBillDetail;
import org.springframework.stereotype.Repository;

/**
 * 缴费单明细DAO接口
 * @author ZhangSC
 * @version 2017-03-29
 */

@Repository
public interface PayPaymentBillDetailDao extends CrudDao<PayPaymentBillDetail> {
	
	/**
	 * 物理删除
	 * @param payPaymentBillDetail
	 * @version 2017-03-30
	 */
	public void physicDelById(PayPaymentBillDetail payPaymentBillDetail);
	
	/**
	 * 通过缴费单编码集合 查询出所有的缴费单详情
	 * @param ids
	 * @return
	 */
	public List<PayPaymentBillDetail> findListByIds(@Param(value = "ids") List<String> ids);

	
	/**
	 * 根据提运单查询未作废的缴费单
	 * @param payPaymentBillDetail
	 * @version 2018-02-27
	 */
	public List<PayPaymentBillDetail> findListNotCancel(PayPaymentBillDetail payPaymentBillDetail);

	//作废相应缴费单 支撑内网取消确认收费操作
	public void cancellationPaymentBillDetail(@Param(value = "payBillList") List<String> payBillList, @Param(value = "remarks") String remarks);

}