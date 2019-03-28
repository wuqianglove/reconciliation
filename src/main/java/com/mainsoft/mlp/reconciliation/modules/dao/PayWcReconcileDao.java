/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;


import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayWcReconcile;
import org.springframework.stereotype.Repository;

/**
 * 保存微信对账记录DAO接口
 * @author guodongyu
 * @version 2019-03-28
 */
@Repository
public interface PayWcReconcileDao extends CrudDao<PayWcReconcile> {

    void updWcReconcileStatus(PayWcReconcile payWcReconcile);
}