/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import com.mainsoft.mlp.reconciliation.common.persistence.Page;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;
import com.mainsoft.mlp.reconciliation.modules.dao.PayPaymentBillDetailDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayPaymentBillDetail;

/**
 * 缴费单明细Service
 * @author ZhangSC
 * @version 2017-03-29
 */
@Service
@Transactional(readOnly = true)
public class PayPaymentBillDetailService extends CrudService<PayPaymentBillDetailDao, PayPaymentBillDetail> {

	public PayPaymentBillDetail get(String id) {
		return super.get(id);
	}
	
	public List<PayPaymentBillDetail> findList(PayPaymentBillDetail payPaymentBillDetail) {
		return super.findList(payPaymentBillDetail);
	}
	/**
	 * 根据缴费单编码集合，查出所有的缴费单详情
	 */
	public List<PayPaymentBillDetail> findListByIds(List<String> ids) {
		return dao.findListByIds(ids);
	}
	
	public Page<PayPaymentBillDetail> findPage(Page<PayPaymentBillDetail> page, PayPaymentBillDetail payPaymentBillDetail) {
		return super.findPage(page, payPaymentBillDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(PayPaymentBillDetail payPaymentBillDetail) {
		super.save(payPaymentBillDetail);
	}
	
	/**
	 * 物理删除
	 * @param payPaymentBillDetail
	 * @version 2017-03-30
	 * @author ZhangSC
	 */
	@Transactional(readOnly = false)
	public void physicDelById(PayPaymentBillDetail payPaymentBillDetail) {
		dao.physicDelById(payPaymentBillDetail);
	}
	
	/**
	 * 添加方法，因为缴费单明细的主键也是外键，所以将添加和修改方法分开
	 * @param payPaymentBillDetail
	 */
	@Transactional(readOnly = false)
	public void update(PayPaymentBillDetail payPaymentBillDetail) {
		dao.update(payPaymentBillDetail);
	}

	
	@Transactional(readOnly = false)
	public void delete(PayPaymentBillDetail payPaymentBillDetail) {
		super.delete(payPaymentBillDetail);
	}
	
	/**
	 * 获取提运单和舱单的list 
	 * @param paymentBillDetailList
	 * @return
	 * @author ZhangSC
	 */
	public List<String> getSheetAndManifestList(List<PayPaymentBillDetail> paymentBillDetailList) {
		List<String> sheetAndManifestList = Lists.newArrayList();
		for (PayPaymentBillDetail payPaymentBillDetail : paymentBillDetailList) {
			sheetAndManifestList.add(payPaymentBillDetail.getManifest()+"."+payPaymentBillDetail.getSheet());
		}
		return sheetAndManifestList;
	}



	
}