/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mainsoft.mlp.common.utils.ListsUtils;
import com.mainsoft.mlp.reconciliation.common.persistence.Page;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;
import com.mainsoft.mlp.reconciliation.modules.dao.PayUnpConfigureDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpConfigure;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpReconcile;

/**
 * 银联在线配置Service
 * @author ZhangSC
 * @version 2017-04-10
 */
@Service
@Transactional(readOnly = true)
public class PayUnpConfigureService extends CrudService<PayUnpConfigureDao, PayUnpConfigure> {

	public PayUnpConfigure get(String id) {
		return super.get(id);
	}

	public List<PayUnpConfigure> findAllList(PayUnpConfigure payUnpConfigure){
		return dao.findAllList(payUnpConfigure);
	}
	
	public List<PayUnpConfigure> findList(PayUnpConfigure payUnpConfigure) {
		//根据辖区过滤
		return super.findList(payUnpConfigure);
	}
	
	public Page<PayUnpConfigure> findPage(Page<PayUnpConfigure> page, PayUnpConfigure payUnpConfigure) {
		//根据辖区过滤
		return super.findPage(page, payUnpConfigure);
	}
	
	@Transactional(readOnly = false)
	public void save(PayUnpConfigure payUnpConfigure) {
		super.save(payUnpConfigure);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayUnpConfigure payUnpConfigure) {
		super.delete(payUnpConfigure);
	}
	
	/**
	 * 获取当前辖区的unionPay配置
	 * @author ZhangSC
	 */
	public PayUnpConfigure getPayunpConfigure(PayUnpConfigure payUnpConfigure){
		List<PayUnpConfigure> payUnpConfigureList = findList(payUnpConfigure);
		if(ListsUtils.isNotEmpty(payUnpConfigureList)){
			return payUnpConfigureList.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 20.18.02.28
	 * 获取当前辖区的unionPay配置  支撑银联查询
	 * @author guodongyu
	 */
	public PayUnpConfigure getPayunpConfigureForQuery(PayUnpConfigure payUnpConfigure){		
			return dao.getPayunpConfigureForQuery(payUnpConfigure);					
	}
	
	/**
	 * 通过外部系统传入的辖区信息查询对应的unionPay配置
	 * @author Guody
	 */
	public PayUnpConfigure getPayunpConfigureForExternal(PayUnpConfigure payUnpConfigure){
		return dao.findPayunpConfigureForExternal(payUnpConfigure);
	}
	
	public List<PayUnpConfigure> getByAreaIdAndType(String areaId, String type){
		return dao.getByAreaIdAndType(areaId, type);
	}

	
	/**
	 * guody  2018.04.28
	 * 查询出非港建费商户号的银联配置信息
	 * @param payUnpConfigure
	 * @return
	 */
	public List<PayUnpConfigure> findAllListForExternal(PayUnpConfigure payUnpConfigure) {
		// TODO Auto-generated method stub
		return dao.findAllListForExternal(payUnpConfigure);
	}

	/**
	 * guody  2018.04.28
	 * 查询出港建费商户号的银联配置信息
	 * @param payUnpConfigure
	 * @return
	 */
	public List<PayUnpConfigure> findAllListForPort(PayUnpConfigure payUnpConfigure) {
		// TODO Auto-generated method stub
		return dao.findAllListForPort(payUnpConfigure);
	}

	/**
	 * 2018.06.29 guodongyu
	 * 通过辖区与账单类型（港建费，规费）查询出对应的银联商户号集合
	 * @param payUnpReconcile
	 */
	public List<String> findMerchantListForHandwork(PayUnpReconcile payUnpReconcile) {
		// TODO Auto-generated method stub
		return dao.findMerchantListForHandwork(payUnpReconcile);	
	}

	/**
	 * 根据商户号查询出对应的银联配置 支撑手工对账
	 * @param merchant
	 * @return
	 */
	public PayUnpConfigure getUnpConfigureByMerchant(String merchant) {
		// TODO Auto-generated method stub
		return dao.getUnpConfigureByMerchant(merchant);
	}
}