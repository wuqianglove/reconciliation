/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayReconcileDetail;
import org.springframework.stereotype.Repository;

/**
 * 银行在线对账明细DAO接口
 * @author Guody
 * @version 2017-09-12
 */

@Repository
public interface PayReconcileDetailDao extends CrudDao<PayReconcileDetail> {
	/**
	 * @param payReconcileDetail
	 */
	void updateSyncDetail(PayReconcileDetail payReconcileDetail);
	
}