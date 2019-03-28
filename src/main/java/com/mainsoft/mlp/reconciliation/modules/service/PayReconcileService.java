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
import com.mainsoft.mlp.reconciliation.modules.entity.PayImportFile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayReconcile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayReconcileDetail;
import com.mainsoft.mlp.reconciliation.modules.dao.PayReconcileDao;

/**
 * 银行在线对账Service
 * @author Guody
 * @version 2017-09-12
 */
@Service
@Transactional(readOnly = true)
public class PayReconcileService extends CrudService<PayReconcileDao, PayReconcile> {
	
	@Autowired
	private PayReconcileDetailService payReconcileDetailService;
	
	@Autowired
	private PayImportFileService payImportFileService;

	public PayReconcile get(String id) {
		return super.get(id);
	}
	
	public List<PayReconcile> findList(PayReconcile payReconcile) {
		return super.findList(payReconcile);
	}
	
	public Page<PayReconcile> findPage(Page<PayReconcile> page, PayReconcile payReconcile) {
		return super.findPage(page, payReconcile);
	}
	
	@Transactional(readOnly = false)
	public void save(PayReconcile payReconcile) {
		super.save(payReconcile);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayReconcile payReconcile) {
		super.delete(payReconcile);
	}
	//TODO 添加保存银行对账信息的方法，带有保存对账详情，并在对账详情中设置渠道来源
	/**
	 * 保存银行在线对账信息（银联方式）
	 * @param payCnpReconcile
	 */
	@Transactional(readOnly = false)
	public void savePayReconcile(PayImportFile payImportFile, PayReconcile payReconcile){
		payImportFileService.save(payImportFile);
		payReconcile.setIsNewRecord(true); //设置id自动生成，引用对账文件id
		payReconcile.setId(payImportFile.getId());
		save(payReconcile);   //保存银联在线对账信息
		
		//获取银联在线对账信息的id，用来与银联在线对账详情关联
		String id = payReconcile.getId();
		//获取渠道来源
		String channel = payReconcile.getChannel();		
		int i = 0;
		for (PayReconcileDetail reconcileDetail : payReconcile.getPayReconcileDetailList()) {
			i++;
			reconcileDetail.setId(id);
			reconcileDetail.setIsNewRecord(true);
			reconcileDetail.setChannel(channel);
			reconcileDetail.setOrdinal(i);   //设置明细序号
			payReconcileDetailService.save(reconcileDetail);  //保存银联在线对账明细
		}
	}
			
	/**
	 * 查询银行对账信息uuid 用于同步到内网银行信息
	 */
	public PayReconcile selectPayReconcileDetailForSync(PayReconcile payReconcile){
		return dao.selectPayReconcileDetailForSync(payReconcile);
	}
}