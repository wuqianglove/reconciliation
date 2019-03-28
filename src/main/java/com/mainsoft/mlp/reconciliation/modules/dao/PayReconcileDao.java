/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayReconcile;
import org.springframework.stereotype.Repository;

/**
 * 银行在线对账DAO接口
 * @author Guody
 * @version 2017-09-12
 */

@Repository
public interface PayReconcileDao extends CrudDao<PayReconcile> {
	/**
	 * 查询银行对账详情
	 * @param payReconcile
	 */
	PayReconcile selectPayReconcileDetailForSync(PayReconcile payReconcile);
	
}