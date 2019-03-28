/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import java.util.List;
import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayReconcileDetailMapping;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpReconcileDetail;
import org.springframework.stereotype.Repository;

/**
 * 银联在线对账明细DAO接口
 * @author ZhangSC
 * @version 2017-04-10
 */

@Repository
public interface PayUnpReconcileDetailDao extends CrudDao<PayUnpReconcileDetail> {
	/**
	 * 更改unionpay的对账状态
	 * @param payUnpReconcileDetail
	 * @author ZhangSC
	 * @version 2017-05-09
	 */
	public void updUnpDetailReconcileStatus(PayUnpReconcileDetail payUnpReconcileDetail);
	

	/**
	 * 查找unp对账记录的list用于webService查看
	 * @return
	 */
	public List<PayReconcileDetailMapping> findUnpReconcileListForWebService(PayReconcileDetailMapping payReconcileDetailMapping);
	
	/**
	 * 查找unp对账记录的分页list用于webService查看
	 * @return
	 */
	public List<PayReconcileDetailMapping> findUnpReconcilePageListForWebService(PayReconcileDetailMapping payReconcileDetailMapping);


	//通过对账文件id查询出对应的对账详情集合
	public List<PayUnpReconcileDetail> findListByImportFileId(PayUnpReconcileDetail payUnpReconcileDetail);
}