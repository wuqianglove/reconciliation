/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mainsoft.mlp.reconciliation.common.persistence.Page;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;
import com.mainsoft.mlp.reconciliation.modules.entity.PayReconcile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayReconcileDetail;
import com.mainsoft.mlp.reconciliation.modules.dao.PayReconcileDetailDao;

/**
 * 银行在线对账明细Service
 * @author Guody
 * @version 2017-09-12
 */
@Service
@Transactional(readOnly = true)
public class PayReconcileDetailService extends CrudService<PayReconcileDetailDao, PayReconcileDetail> {

	public PayReconcileDetail get(String id) {
		return super.get(id);
	}
	
	public List<PayReconcileDetail> findList(PayReconcileDetail payReconcileDetail) {
		return super.findList(payReconcileDetail);
	}
	
	public Page<PayReconcileDetail> findPage(Page<PayReconcileDetail> page, PayReconcileDetail payReconcileDetail) {
		return super.findPage(page, payReconcileDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(PayReconcileDetail payReconcileDetail) {
		super.save(payReconcileDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayReconcileDetail payReconcileDetail) {
		super.delete(payReconcileDetail);
	}
	
	@Transactional(readOnly = false)
	public void updateSyncDetail(PayReconcile payReconcile) {
		//payReconcile 调用更新银行对账信息的方法，
		Map<String, String> syncMap = payReconcile.getRequestMap();		
		PayReconcileDetail payReconcileDetail = new PayReconcileDetail();
		for (Map.Entry<String, String> entry : syncMap.entrySet()) {									
			payReconcileDetail.setOrderCode(entry.getKey());
			payReconcileDetail.setRemarks(entry.getValue());
			dao.updateSyncDetail(payReconcileDetail);
		}
		
	}
}