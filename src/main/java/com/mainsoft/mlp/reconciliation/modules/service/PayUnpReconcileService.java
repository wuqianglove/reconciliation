/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mainsoft.mlp.reconciliation.common.persistence.Page;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;
import com.mainsoft.mlp.reconciliation.modules.dao.PayUnpReconcileDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpReconcile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpReconcileDetail;

/**
 * 银联在线对账Service
 * @author ZhangSC
 * @version 2017-04-10
 */
@Service
@Transactional(readOnly = true)
public class PayUnpReconcileService extends CrudService<PayUnpReconcileDao, PayUnpReconcile> {
	@Autowired
	private PayUnpReconcileDetailService payUnpReconcileDetailService;
	
	public PayUnpReconcile get(String id) {
		return super.get(id);
	}
	
	public List<PayUnpReconcile> findList(PayUnpReconcile payUnpReconcile) {
		return super.findList(payUnpReconcile);
	}
	
	public Page<PayUnpReconcile> findPage(Page<PayUnpReconcile> page, PayUnpReconcile payUnpReconcile) {
		return super.findPage(page, payUnpReconcile);
	}
	
	@Transactional(readOnly = false)
	public void save(PayUnpReconcile payUnpReconcile) {
		super.save(payUnpReconcile);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayUnpReconcile payUnpReconcile) {
		super.delete(payUnpReconcile);
	}
	
	/**
	 * 保存银联在线对账信息
	 * @param payUnpReconcile
	 */
	@Transactional(readOnly = false)
	public void savePayUnpReconcile(PayUnpReconcile payUnpReconcile){
		payUnpReconcile.setIsNewRecord(true); //设置id自动生成，引用对账文件id
		
		save(payUnpReconcile);   //保存银联在线对账信息
		
		//获取银联在线对账信息的id，用来与银联在线对账详情关联
		String id = payUnpReconcile.getId();
		
		int i = 0;
		for (PayUnpReconcileDetail unpReconcileDetail : payUnpReconcile.getPayUnpReconcileDetailList()) {
			i++;
			unpReconcileDetail.setId(id);
			unpReconcileDetail.setIsNewRecord(true);
			unpReconcileDetail.setOrdinal(i);   //设置明细序号
			payUnpReconcileDetailService.save(unpReconcileDetail);  //保存银联在线对账明细
		}
	}
	
	/**
	 * 更改unionpay的对账状态
	 * @param payUnpReconcile
	 * @author ZhangSC
	 * @version 2017-05-09
	 */
	@Transactional(readOnly = false)
	public void updUnpReconcileStatus(PayUnpReconcile payUnpReconcile){
		dao.updUnpReconcileStatus(payUnpReconcile);
	}
	
	
	
	
	
	
	
	
	
}