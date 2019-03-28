/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import com.mainsoft.mlp.common.utils.ListsUtils;
import com.mainsoft.mlp.reconciliation.common.persistence.Page;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;
import com.mainsoft.mlp.reconciliation.common.utils.StringUtils;
import com.mainsoft.mlp.reconciliation.modules.dao.PayUnpReconcileDetailDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayReconcileDetailMapping;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpReconcileDetail;
import com.mainsoft.mlp.reconciliation.modules.enums.PayDefrayBillAvenueEnum;
import com.mainsoft.mlp.reconciliation.modules.utils.PayUtils;

/**
 * 银联在线对账明细Service
 * @author ZhangSC
 * @version 2017-04-10
 */
@Service
@Transactional(readOnly = true)
public class PayUnpReconcileDetailService extends CrudService<PayUnpReconcileDetailDao, PayUnpReconcileDetail> {

	public PayUnpReconcileDetail get(String id) {
		return super.get(id);
	}
	
	public List<PayUnpReconcileDetail> findList(PayUnpReconcileDetail payUnpReconcileDetail) {
		return super.findList(payUnpReconcileDetail);
	}
	
	public Page<PayUnpReconcileDetail> findPage(Page<PayUnpReconcileDetail> page, PayUnpReconcileDetail payUnpReconcileDetail) {
		return super.findPage(page, payUnpReconcileDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(PayUnpReconcileDetail payUnpReconcileDetail) {
		super.save(payUnpReconcileDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayUnpReconcileDetail payUnpReconcileDetail) {
		super.delete(payUnpReconcileDetail);
	}
	
	/**
	 * 更改unionpay的对账状态
	 * @param payUnpReconcileDetail
	 * @author ZhangSC
	 * @version 2017-05-09
	 */
	@Transactional(readOnly = false)
	public void updUnpDetailReconcileStatus(PayUnpReconcileDetail payUnpReconcileDetail){
		dao.updUnpDetailReconcileStatus(payUnpReconcileDetail);
	}
	
	/**
	 * 查找unionPay对账记录的list，用于获取总数
	 * @return
	 */
	public List<PayReconcileDetailMapping> findUnpReconcileListForWebService(PayReconcileDetailMapping payReconcileDetailMapping){
		return dealPayReconcileDetail(dao.findUnpReconcileListForWebService(payReconcileDetailMapping));
	}
	
	/**
	 * 查找unionPay对账记录的分页list，用于webService查看
	 * @return
	 */
	public List<PayReconcileDetailMapping> findUnpReconcilePageListForWebService(PayReconcileDetailMapping payReconcileDetailMapping){
		return dealPayReconcileDetail(dao.findUnpReconcilePageListForWebService(payReconcileDetailMapping));
	}
	
	/**
	 * 处理对账详细
	 * @author LiTeng
	 * @param payReconcileDetailList
	 * @return
	 */
	public List<PayReconcileDetailMapping> dealPayReconcileDetail(List<PayReconcileDetailMapping> payReconcileDetailList){
		if(ListsUtils.isNotEmpty(payReconcileDetailList)){
			for (PayReconcileDetailMapping payReconcileDetail : payReconcileDetailList) {
				payReconcileDetail.setAvenue(PayDefrayBillAvenueEnum.UNION_PAY.getCode());   //设置缴费途径为
				//解析缴费金额，转为bigdecimal
				if(StringUtils.isNotBlank(payReconcileDetail.getRevenueStr())){
					payReconcileDetail.setRevenue(PayUtils.parseFenRevenueStr(payReconcileDetail.getRevenueStr()));    
				}
				//解析交易日期
				//2018.04.10 交易时间从关联表中获得 guodongyu
				/*if(StringUtils.isNotBlank(payReconcileDetail.getTransDateStr())){
					try {
						payReconcileDetail.setTransDate(DateUtils.parseDate(PayUtils.getYearStr()+payReconcileDetail.getTransDateStr(), "yyyyMMddHHmmss"));
					} catch (ParseException e) {
						e.printStackTrace();
					};
				}*/
			}
		}else{
			payReconcileDetailList = Lists.newArrayList();
		}
		return payReconcileDetailList;
	}

	//通过对账文件id查询出对应的对账详情集合
	public List<PayUnpReconcileDetail> findListByImportFileId(PayUnpReconcileDetail payUnpReconcileDetail) {
		return dao.findListByImportFileId(payUnpReconcileDetail);
	}
	
}