/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.modules.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.mainsoft.mlp.reconciliation.common.persistence.CrudDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpConfigure;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpReconcile;
import org.springframework.stereotype.Repository;

/**
 * 银联在线配置DAO接口
 * @author ZhangSC
 * @version 2017-04-10
 */

@Repository
public interface PayUnpConfigureDao extends CrudDao<PayUnpConfigure>{

	public List<PayUnpConfigure> getByAreaIdAndType(@Param("areaId") String areaId, @Param("type") String type);

	/**
	 * 2018.01.19
	 * 通过辖区与银行类型查询到相应的银行配置信息
	 * @param payUnpConfigure
	 * @return
	 */
	public PayUnpConfigure findPayunpConfigureForExternal(PayUnpConfigure payUnpConfigure);

	/**
	 * guodongyu
	 * 2018.02.28
	 * 根据辖区编码 商户号 账户类型  查询对应的 银行配置信息
	 * @param payUnpConfigure
	 * @return
	 */
	public PayUnpConfigure getPayunpConfigureForQuery(PayUnpConfigure payUnpConfigure);

	/**
	 * guody  2018.04.28
	 * 查询出非港建费商户号的银联配置信息
	 * @param payUnpConfigure
	 * @return
	 */
	public List<PayUnpConfigure> findAllListForExternal(PayUnpConfigure payUnpConfigure);

	/**
	 * guody  2018.04.28
	 * 查询出港建费商户号的银联配置信息
	 * @param payUnpConfigure
	 * @return
	 */
	public List<PayUnpConfigure> findAllListForPort(PayUnpConfigure payUnpConfigure);

	/**
	 * 2018.06.29 guodongyu
	 * 通过辖区与账单类型（港建费，规费）查询出对应的银联商户号集合
	 * @param payUnpReconcile
	 */
	public List<String> findMerchantListForHandwork(PayUnpReconcile payUnpReconcile);

	/**
	 * 根据商户号查询出对应的银联配置 支撑手工对账
	 * @param merchant
	 * @return
	 */
	public PayUnpConfigure getUnpConfigureByMerchant(@Param("merchant") String merchant);
}