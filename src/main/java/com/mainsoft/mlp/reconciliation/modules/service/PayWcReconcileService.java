/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.List;
import com.mainsoft.mlp.reconciliation.common.persistence.Page;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;
import com.mainsoft.mlp.reconciliation.modules.dao.PayWcReconcileDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpReconcileDetail;
import com.mainsoft.mlp.reconciliation.modules.entity.PayWcReconcile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayWcReconcileDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 保存微信对账记录Service
 * @author guodongyu
 * @version 2019-03-28
 */
@Service
@Transactional(readOnly = true)
public class PayWcReconcileService extends CrudService<PayWcReconcileDao, PayWcReconcile> {

    @Autowired
    private PayWcReconcileDetailService payWcReconcileDetailService;


	public PayWcReconcile get(String id) {
		return super.get(id);
	}
	
	public List<PayWcReconcile> findList(PayWcReconcile payWcReconcile) {
		return super.findList(payWcReconcile);
	}
	
	public Page<PayWcReconcile> findPage(Page<PayWcReconcile> page, PayWcReconcile payWcReconcile) {
		return super.findPage(page, payWcReconcile);
	}
	
	@Transactional(readOnly = false)
	public void save(PayWcReconcile payWcReconcile) {
		super.save(payWcReconcile);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayWcReconcile payWcReconcile) {
		super.delete(payWcReconcile);
	}

    @Transactional(readOnly = false)
	public void saveWcReconcile(PayWcReconcile payWcReconcile) {
        payWcReconcile.setIsNewRecord(true); //设置id自动生成，引用对账文件id
        save(payWcReconcile);   //保存银联在线对账信息
        //获取银联在线对账信息的id，用来与银联在线对账详情关联
        String id = payWcReconcile.getId();
        int i = 0;
        for (PayWcReconcileDetail payWcReconcileDetail : payWcReconcile.getPayWcReconcileDetailList()) {
            i++;
            payWcReconcileDetail.setId(id);
            payWcReconcileDetail.setIsNewRecord(true);
            payWcReconcileDetail.setOrdinal(i);   //设置明细序号
            payWcReconcileDetailService.save(payWcReconcileDetail);  //保存微信对账明细
        }

	}

    public void updWcReconcileStatus(PayWcReconcile payWcReconcile) {
	    dao.updWcReconcileStatus(payWcReconcile);
    }
}