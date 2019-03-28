/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mainsoft.mlp.common.utils.ListsUtils;
import com.mainsoft.mlp.reconciliation.common.persistence.Page;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;
import com.mainsoft.mlp.reconciliation.common.utils.StringUtils;
import com.mainsoft.mlp.reconciliation.modules.dao.PayPaymentBillDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayPaymentBill;
import com.mainsoft.mlp.reconciliation.modules.enums.PayPaymentBillBusinessTypeEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PaymentBillVerifyStatusEnum;
import com.mainsoft.mlp.reconciliation.modules.entity.User;


/**
 * 缴费单Service
 * @author ZhangSC
 * @version 2017-03-28
 */
@Service
@Transactional(readOnly = true)
public class PayPaymentBillService extends CrudService<PayPaymentBillDao, PayPaymentBill> {

	@Autowired
	private PayPaymentBillDetailService payPaymentBillDetailService;

	@Autowired
	private PayDefrayPaymentService payDefrayPaymentService;

	public PayPaymentBill get(String id) {
		return super.get(id);
	}

	/**
	 * guody 2017.10.17
	 * 根据缴费单编码集合 查询出所有的缴费单信息
	 * @param paymentIds
	 * @return
	 */
	public List<PayPaymentBill> findPaymentBillListByIds (List<String> paymentIds){
		return dao.findPaymentBillListByIds(paymentIds);
	}
	
	/**
	 * guody 2017.10.18
	 * 根据缴费单编码集合，查询出是否含有已经缴费的缴费单
	 * 返回值为布尔值
	 * isPayPaymentList
	 */
	public boolean isPayPaymentList(List<String> paymentIds){
		
		//检验缴费单，是否存在已经缴费或者已经作废的状态
		Integer count = dao.checkPaymentBillListForPayStatus(paymentIds);
			if (count == 0) {
			return true;
			}
			return false;		
	}
	
	public List<PayPaymentBill> findList(PayPaymentBill payPaymentBill) {
		return super.findList(payPaymentBill);
	}
	
	/**
	 * 查找港口建设费类型的缴费单
	 * @param page
	 * @param payPaymentBill
	 * @return
	 */
	public Page<PayPaymentBill> findPageByPortConstructionFee(Page<PayPaymentBill> page, PayPaymentBill payPaymentBill) {
		payPaymentBill.setBusinessType(PayPaymentBillBusinessTypeEnum.PORTCONSTRUCTIONFEE.getCode());  //设置查询条件为港口建设费
		
		//设置分页的查询条件
		payPaymentBill.setPage(page);
		page.setList(dao.findListByPortConstructionFee(payPaymentBill));
		return page;
		
	}
	
	public Page<PayPaymentBill> findPage(Page<PayPaymentBill> page, PayPaymentBill payPaymentBill) {
		return super.findPage(page, payPaymentBill);
	}

	
	@Transactional(readOnly = false)
	public void delete(PayPaymentBill payPaymentBill) {
		super.delete(payPaymentBill);
	}

	//TODO 2018.03.21 guodongyu  这里的 -1 要改一下 建议捋一下逻辑
	public User getCreateUserOrUpdateUser(User original){
		//如果创建人\更新人为空或者他的id和姓名为空，那么返回id和name都为-1的user
		if((original == null) || (StringUtils.isBlank(original.getId()) || StringUtils.isBlank(original.getName()))){
			original = new User("-1");
			original.setName("-1");
		}
		return original;
	}
	
	/**
	 * 缴费单对账成功
	 * @param PayDefrayBillList
	 */
	@Transactional(readOnly = false)
	public void paymentBillVerifySuccess(List<String> PayDefrayBillList){
		if(ListsUtils.isNotEmpty(PayDefrayBillList)){
			//通过支付单编码集合获得缴费单编码集合
			List<String> paymentBillList = payDefrayPaymentService.findPaymentListByPayDefrayBillList(PayDefrayBillList);
			PayPaymentBill verifrySuccessPaymentBill = new PayPaymentBill();
			verifrySuccessPaymentBill.setVerify(PaymentBillVerifyStatusEnum.VERIFIED_SUCCESS.getCode());
			verifrySuccessPaymentBill.setPaymentBillList(paymentBillList);
			updVerifyStatus(verifrySuccessPaymentBill);
		}
	}
	
	/**
	 * 缴费单对账失败
	 * @param PayDefrayBillList
	 */
	@Transactional(readOnly = false)
	public void paymentBillVerifyFail(List<String> PayDefrayBillList){
		if(ListsUtils.isNotEmpty(PayDefrayBillList)){
			//更改对账失败的缴费的对账状态
			List<String> paymentBillList = payDefrayPaymentService.findPaymentListByPayDefrayBillList(PayDefrayBillList);
			PayPaymentBill verifryFailePaymentBill = new PayPaymentBill();
			verifryFailePaymentBill.setVerify(PaymentBillVerifyStatusEnum.VERIFIED_FAIL.getCode());
			verifryFailePaymentBill.setPaymentBillList(paymentBillList);
			updVerifyStatus(verifryFailePaymentBill);
		}
	}
	
	/**
	 * 更新对账状态
	 * @param payPaymentBill
	 */
	@Transactional(readOnly = false)
	public void updVerifyStatus(PayPaymentBill payPaymentBill){
		dao.updVerifyStatus(payPaymentBill);
	}



}