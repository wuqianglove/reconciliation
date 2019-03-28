/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import java.util.List;
import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayImportFile;
import org.springframework.stereotype.Repository;

/**
 * 导入文件DAO接口
 * @author ZhangSC
 * @version 2017-04-01
 */

@Repository
public interface PayImportFileDao extends CrudDao<PayImportFile> {
	/**
	 * 更改对账导入文件的处理状态
	 * @param payImportFile
	 */
	public void updPayImportFileStatus(PayImportFile payImportFile);

	/**
	 * 获得对账文件集合
	 * @param payImportFile
	 * @return
	 */
	public List<PayImportFile> findListByPaymentType(PayImportFile payImportFile);


	public List<PayImportFile> findListForTest();
	/**
	 * 2018.06.29 guodongyu
	 * 根据商户号，清算日期，对账文件类型（港建费，非税）查询满足的对账文件集合
	 * 连接PAY_UNP_RECONCILE表
	 * @param payImportFile
	 *
	 */
	public List<PayImportFile> findListForHandwork(PayImportFile payImportFile);
}