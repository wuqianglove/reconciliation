/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpDealing;
import org.springframework.stereotype.Repository;

/**
 * 银联在线交易记录DAO接口
 * @author ZhangSC
 * @version 2017-04-10
 */

@Repository
public interface PayUnpDealingDao extends CrudDao<PayUnpDealing> {
	
}