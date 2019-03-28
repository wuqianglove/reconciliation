/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpReconcile;
import org.springframework.stereotype.Repository;

/**
 * 银联在线对账DAO接口
 * @author ZhangSC
 * @version 2017-04-10
 */

@Repository
public interface PayUnpReconcileDao extends CrudDao<PayUnpReconcile>  {
	/**
	 * 更改unionpay的对账状态
	 * @author ZhangSC
	 * @version 2017-05-09
	 */
	public void updUnpReconcileStatus(PayUnpReconcile payUnpReconcile);
}