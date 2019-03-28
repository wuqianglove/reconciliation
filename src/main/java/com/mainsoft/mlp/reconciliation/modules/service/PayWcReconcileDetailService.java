/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.List;

import com.mainsoft.mlp.reconciliation.common.persistence.Page;


import com.mainsoft.mlp.reconciliation.modules.dao.PayWcReconcileDetailDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayWcReconcileDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;

/**
 * 微信对账明细Service
 * @author guodongyu
 * @version 2019-03-28
 */
@Service
@Transactional(readOnly = true)
public class PayWcReconcileDetailService extends CrudService<PayWcReconcileDetailDao, PayWcReconcileDetail> {

	public PayWcReconcileDetail get(String id) {
		return super.get(id);
	}
	
	public List<PayWcReconcileDetail> findList(PayWcReconcileDetail payWcReconcileDetail) {
		return super.findList(payWcReconcileDetail);
	}
	
	public Page<PayWcReconcileDetail> findPage(Page<PayWcReconcileDetail> page, PayWcReconcileDetail payWcReconcileDetail) {
		return super.findPage(page, payWcReconcileDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(PayWcReconcileDetail payWcReconcileDetail) {
		super.save(payWcReconcileDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayWcReconcileDetail payWcReconcileDetail) {
		super.delete(payWcReconcileDetail);
	}

	public List<PayWcReconcileDetail> findListByImportFileId(PayWcReconcileDetail payWcReconcileDetail) {


        return dao.findListByImportFileId(payWcReconcileDetail);
    }

    public void updWcDetailReconcileStatus(PayWcReconcileDetail wcReconcileDetail) {

    }
}