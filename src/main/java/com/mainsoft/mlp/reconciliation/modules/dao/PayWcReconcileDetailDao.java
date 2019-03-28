/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayWcReconcileDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 微信对账明细DAO接口
 * @author guodongyu
 * @version 2019-03-28
 */
@Repository
public interface PayWcReconcileDetailDao extends CrudDao<PayWcReconcileDetail> {

    List<PayWcReconcileDetail> findListByImportFileId(PayWcReconcileDetail payWcReconcileDetail);
}