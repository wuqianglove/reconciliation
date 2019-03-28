/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mainsoft.mlp.reconciliation.common.persistence.Page;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpDealing;
import com.mainsoft.mlp.reconciliation.modules.dao.PayUnpDealingDao;

/**
 * 银联在线交易记录Service
 * @author ZhangSC
 * @version 2017-04-10
 */
@Service
@Transactional(readOnly = true)
public class PayUnpDealingService extends CrudService<PayUnpDealingDao, PayUnpDealing> {

	public PayUnpDealing get(String id) {
		return super.get(id);
	}
	
	public List<PayUnpDealing> findList(PayUnpDealing payUnpDealing) {
		return super.findList(payUnpDealing);
	}
	
	public Page<PayUnpDealing> findPage(Page<PayUnpDealing> page, PayUnpDealing payUnpDealing) {
		return super.findPage(page, payUnpDealing);
	}
	
	@Transactional(readOnly = false)
	public void save(PayUnpDealing payUnpDealing) {
		super.save(payUnpDealing);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayUnpDealing payUnpDealing) {
		super.delete(payUnpDealing);
	}

	/*public void saveForExternal(PayUnpDealing payUnpDealing) {
		// TODO Auto-generated method stub.
		dao.insert(payUnpDealing);
		
	}*/
	public void saveForPay(PayUnpDealing payUnpDealing) {
	// TODO Auto-generated method stub.
	dao.insert(payUnpDealing);
	
	}
	
	
}