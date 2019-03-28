/*
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.Date;
import java.util.List;

import com.mainsoft.mlp.common.mapper.JsonMapper;
import com.mainsoft.mlp.common.utils.DateUtils;
import com.mainsoft.mlp.reconciliation.common.config.Global;
import com.mainsoft.mlp.reconciliation.common.enums.ClientCallerEnum;
import com.mainsoft.mlp.reconciliation.common.jms.MessageInfo;
import com.mainsoft.mlp.reconciliation.common.jms.TextJmsProducer;

import com.mainsoft.mlp.reconciliation.common.utils.SpringContextHolder;
import com.mainsoft.mlp.reconciliation.common.utils.StringUtils;
import com.mainsoft.mlp.reconciliation.common.webservice.DPS_Passport;
import com.mainsoft.mlp.reconciliation.common.webservice.ServiceParam;
import com.mainsoft.mlp.reconciliation.common.webservice.ServiceResult;
import com.mainsoft.mlp.reconciliation.common.webservice.osb.RouterClientCaller;
import com.mainsoft.mlp.reconciliation.modules.dps.client.DpsClientCaller;
import com.mainsoft.mlp.reconciliation.modules.payment.entity.PayReconcile;
import com.mainsoft.mlp.reconciliation.modules.payment.entity.PayReconcileDetail;
import com.mainsoft.mlp.reconciliation.modules.sys.entity.Area;
import com.mainsoft.mlp.reconciliation.modules.sys.service.AreaService;

*/
/**
 * 测试并行同步银行对账信息
 * @author guody
 * @date 2017年9月14日
 *//*


public class SyncRunnable implements Runnable  {
			 
	private static DpsClientCaller dpsClientCaller =  (DpsClientCaller)SpringContextHolder.getBean(DpsClientCaller.class);			 
	private static PayReconcileService payReconcileService =  (PayReconcileService)SpringContextHolder.getBean(PayReconcileService.class);		
	private static PayReconcileDetailService payReconcileDetailService =  (PayReconcileDetailService)SpringContextHolder.getBean(PayReconcileDetailService.class);
	private static RouterClientCaller routerClientCaller = (RouterClientCaller)SpringContextHolder.getBean(RouterClientCaller.class);			 
	private static TextJmsProducer textJmsProducer = (TextJmsProducer)SpringContextHolder.getBean(TextJmsProducer.class);
	private static AreaService areaService = SpringContextHolder.getBean(AreaService.class);
	
	private String merchant;
	private String district;
	public SyncRunnable(String merchant, String district) {  
		this.merchant = merchant;
	    this.district = district;
	}  
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public void run() {
		Integer no = Integer.parseInt(Global.NO);		//no的int类型
		PayReconcile payReconcile = new PayReconcile();
		//要同步银行对账信息的日期（交易日期）
		Date transDate = DateUtils.addDays(new Date(), -2);	
		payReconcile.setTransDate(transDate);
		payReconcile.setBeginTransDate(transDate);
		payReconcile.setEndTransDate(transDate);
		//测试阶段固定商务号		
		payReconcile.setMerchant(merchant);
		//设置银行对账信息为未同步状态
		payReconcile.setSync(no);
		//根据条件查询银行在线对账表中的数据
		PayReconcile selectPayReconcileDetailForSync = new PayReconcile();
		try {			
			selectPayReconcileDetailForSync = payReconcileService.selectPayReconcileDetailForSync(payReconcile);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//根据获得的uuid查询银行对账详情
		PayReconcileDetail payReconcileDetail = new PayReconcileDetail();
		
		//如果selectPayReconcileDetailForSync对象返回结果为空表示无需要对账的文件
		if (selectPayReconcileDetailForSync == null) {
			throw new RuntimeException("所查商户号"+merchant+"在"+transDate+"无对账文件");
		}
		payReconcileDetail.setId(selectPayReconcileDetailForSync.getId());
		List<PayReconcileDetail> payReconcileDetailList = payReconcileDetailService.findList(payReconcileDetail);		
		selectPayReconcileDetailForSync.setPayReconcileDetailList(payReconcileDetailList);
		//调用内网保存银行对账信息的webservice
		//获取部署辖区编码，用于一级总线判断走哪个辖区的内网。
		Area area = areaService.get(district);
		String deployAreaId = null;
		if(area != null && StringUtils.isNotBlank(area.getRoutingAreaId())){
			deployAreaId = area.getRoutingAreaId();
		}
		//根据district的值，调用不同的分发webservice
		ServiceResult callWebserviceToSavePayReconcile = callWebserviceToSavePayReconcile(selectPayReconcileDetailForSync, deployAreaId);
		//System.out.println("我来试试"+merchant);
		payReconcile.setChannel(selectPayReconcileDetailForSync.getChannel());
		if (callWebserviceToSavePayReconcile.getStatus().equals("0")) {
			//如果返回值为成功，调用内网的对账方法，传入的值为（payDate交易日期，渠道来源）
			//根据district的值，调用不同的对账webservice（指向对应辖区系统）
			//调用webService
//			if(Global.getConfig("webService.client.mode").equals(Global.WEBSERVICE_CLIENT_OSB)) {
//				if(Global.getConfig("mlp.online.jms.enable").equals(Global.TRUE)){
//					MessageInfo messageInfo = new MessageInfo();
//					messageInfo.setBusinessName("DPS");
//					messageInfo.setServiceName("Payment.toVerifyEntrance"); 
//					messageInfo.setJsonParam(JsonMapper.toJsonString(getDpsServiceParam(JsonMapper.toJsonString(payReconcile))));
//					messageInfo.setDeployAreaId(deployAreaId);
//					textJmsProducer.sendMessage(messageInfo);
//				} else {
//					routerClientCaller.callRouterServer(deployAreaId, "Payment.toVerifyEntrance", "DPS", JsonMapper.toJsonString(getDpsServiceParam(JsonMapper.toJsonString(payReconcile))));
//				}
//			}
//			if(Global.getConfig("webService.client.mode").equals(Global.WEBSERVICE_CLIENT_DC)) {
//				dpsClientCaller.callDpsServer("Payment.toVerifyEntrance", JsonMapper.toJsonString(getDpsServiceParam(JsonMapper.toJsonString(payReconcile))));
//			}
			
			MessageServiceUtils messageServiceUtils = new MessageServiceUtils();
			messageServiceUtils.callOnlineByWebServiceClientMode("DPS" ,"Payment.toVerifyEntrance" ,JsonMapper.toJsonString(getDpsServiceParam(JsonMapper.toJsonString(payReconcile))),ClientCallerEnum.DPS_CLIENT_CALLER.getCode(),deployAreaId);

		}else {
			throw new RuntimeException(callWebserviceToSavePayReconcile.getMessage());		
		}
	}
		
	*/
/**
	 * 调用内网webservice保存银行对账信息（银联与中国银联共用）
	 * TODO 东方支付调用webservice保存对账相关信息待深究
	 * @param payReconcile
	 * @return
	 *//*

	private ServiceResult callWebserviceToSavePayReconcile(PayReconcile payReconcile, String deployAreaId) {
		//调用内网的服务
		ServiceResult callDpsServer = null;
		//调用webService
		if(Global.getConfig("webService.client.mode").equals(Global.WEBSERVICE_CLIENT_OSB)) {
			callDpsServer = routerClientCaller.callRouterServer(deployAreaId, "Payment.toSavePayReconcile", "DPS", JsonMapper.toJsonString(getDpsServiceParam(JsonMapper.toJsonString(payReconcile))));
		}
		if(Global.getConfig("webService.client.mode").equals(Global.WEBSERVICE_CLIENT_DC)) {
			callDpsServer = dpsClientCaller.callDpsServer("Payment.toSavePayReconcile", JsonMapper.toJsonString(getDpsServiceParam(JsonMapper.toJsonString(payReconcile))));
		}
		//callDpsServer.getStatus();
		return callDpsServer;
				
	}
		
	*/
/**
	 * 得到调用外网部分的serviceParam
	 * @param acpPermissionApply
	 * @return
	 *//*

	public ServiceParam getDpsServiceParam(String paramJson){
		DPS_Passport passport = new DPS_Passport("", "", new Date());
		return new ServiceParam(JsonMapper.toJsonString(passport), paramJson);
	}
}
*/
