/*
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mainsoft.mlp.common.mapper.JsonMapper;
import com.mainsoft.mlp.common.utils.EnumsUtils;
import com.mainsoft.mlp.reconciliation.common.config.Global;
import com.mainsoft.mlp.reconciliation.common.utils.StringUtils;
import com.mainsoft.mlp.reconciliation.modules.entity.PayDefrayBill;
import com.mainsoft.mlp.reconciliation.modules.enums.PayPaymentBillBusinessTypeEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.QueryRespStauseEnum;

*/
/**
 * @author guodongyu
 * 2019.02.11
 * 定时查询未支付成功的支付单在银行系统中的缴费状态（查询三天内的数据）
 *
 *//*


@Service
public class PayDefrayBillTimingCheckService {
	
	*/
/**
	 * 日志对象
	 *//*

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PayDefrayBillCheckService payDefrayBillCheckService;
	
	@Autowired
	private PayDefrayBillForQueryService payDefrayBillForQueryService;
	
	@Autowired
	private PayDefrayBillService payDefrayBillService;
	
	//启用定时任务
	@Transactional(readOnly = false)
	public void timingCheckPayDefrayBill(){
		
		//查询未缴费的三天内的支付单  for循环调用银联查询接口
		List<PayDefrayBill> listForTimingCheck = payDefrayBillService.getPayDefrayBillListForTimingCheck();
		for (PayDefrayBill payDefrayBill : listForTimingCheck) {
			ObjectNode json = JsonMapper.getObjectNode();			
			try{			
				if(EnumsUtils.stringEquals(payDefrayBill.getAvenue(), PayPaymentBillBusinessTypeEnum.PORTCONSTRUCTIONFEE)){
					Map<String, String> rspData =	payDefrayBillCheckService.checkUnpPayDefrayBill(payDefrayBill.getMerchant(), payDefrayBill.getDistrict(), payDefrayBill.getOrderCode(), payDefrayBill.getGateCode());
					String result = rspData.get("queryRespStatus");
					json = bySelectResultUnp(result);
					//如果返回结果不为空且等于成功则支付
					if(json.get(Global.RESULT_CODE) != null && StringUtils.equals(Global.RESULT_CODE_SUCCESS, json.get(Global.RESULT_CODE).asText())){						
						payDefrayBillForQueryService.updateForUnpPayDefrayBill(payDefrayBill.getId(),payDefrayBill.getRemarks(),rspData);
					}else{
						logger.error("银联查询失败，原因："+json.get(Global.RESULT_MSG));					
					}
				}else if(!EnumsUtils.stringEquals(payDefrayBill.getAvenue(), PayPaymentBillBusinessTypeEnum.PORTCONSTRUCTIONFEE)){
					Map<String, String> rspData =	payDefrayBillCheckService.checkUnpPayDefrayBill(payDefrayBill.getMerchant(), payDefrayBill.getDistrict(), payDefrayBill.getOrderCode(), payDefrayBill.getGateCode());
					String result = rspData.get("queryRespStatus");
					json = bySelectResultUnp(result);
					//如果返回结果不为空且等于成功则支付
					if(json.get(Global.RESULT_CODE) != null && StringUtils.equals(Global.RESULT_CODE_SUCCESS, json.get(Global.RESULT_CODE).asText())){						
						payDefrayBillForQueryService.updateForUnpPayDefrayBillForExternal(payDefrayBill.getId(),payDefrayBill.getRemarks(),rspData);
					}else{
						logger.error("银联查询失败，原因："+json.get(Global.RESULT_MSG));					
					}
					
				}				
							
			}catch(Exception e){
				logger.error("银联自动查询失败：",e);				
			}
			
		}
						
	}
	
	
	public ObjectNode bySelectResultUnp(String result){
		ObjectNode json = JsonMapper.getObjectNode();

		if(StringUtils.isNoneBlank(result)){
			if(EnumsUtils.stringEquals(result, QueryRespStauseEnum.SUCCEED)){
				json.put(Global.RESULT_CODE, Global.RESULT_CODE_SUCCESS);
				json.put(Global.RESULT_MSG, "交易成功！");
			}else if(EnumsUtils.stringEquals(result, QueryRespStauseEnum.FAILED)){
				json.put(Global.RESULT_CODE, Global.RESULT_CODE_FAIL);
				json.put(Global.RESULT_MSG, QueryRespStauseEnum.FAILED.getDesc());
				
			}else if(EnumsUtils.stringEquals(result, QueryRespStauseEnum.DISPOSING)){
				json.put(Global.RESULT_CODE, Global.RESULT_CODE_FAIL);
				json.put(Global.RESULT_MSG, QueryRespStauseEnum.DISPOSING.getDesc());
				
			}else if(EnumsUtils.stringEquals(result, QueryRespStauseEnum.INEXISTENCE)){
				json.put(Global.RESULT_CODE, Global.RESULT_CODE_FAIL);
				json.put(Global.RESULT_MSG, QueryRespStauseEnum.INEXISTENCE.getDesc());
				
			}else if(EnumsUtils.stringEquals(result, QueryRespStauseEnum.NETWORKLOSE)){
				json.put(Global.RESULT_CODE, Global.RESULT_CODE_FAIL);
				json.put(Global.RESULT_MSG, QueryRespStauseEnum.NETWORKLOSE.getDesc());
				
			}else if(EnumsUtils.stringEquals(result, QueryRespStauseEnum.QUERYFAILED)){
				json.put(Global.RESULT_CODE, Global.RESULT_CODE_FAIL);
				json.put(Global.RESULT_MSG, QueryRespStauseEnum.QUERYFAILED.getDesc());
				
			}else if(EnumsUtils.stringEquals(result, QueryRespStauseEnum.SIGNFAILED)){
				json.put(Global.RESULT_CODE, Global.RESULT_CODE_FAIL);
				json.put(Global.RESULT_MSG, QueryRespStauseEnum.SIGNFAILED.getDesc());
			}else{
				json.put(Global.RESULT_CODE, Global.RESULT_CODE_FAIL);
				json.put(Global.RESULT_MSG, "没有对应的结果返回的数据为：" + result);
			}
		}else{
			json.put(Global.RESULT_CODE, Global.RESULT_CODE_FAIL);
			json.put(Global.RESULT_MSG, "查询银行接口结果为空，请稍后再次尝试！");
		}
		return json;
	}

}
*/
