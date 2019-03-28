/*
package com.mainsoft.mlp.reconciliation.modules.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mainsoft.mlp.common.utils.DateUtils;
import com.mainsoft.mlp.common.utils.EnumsUtils;
import com.mainsoft.mlp.reconciliation.common.utils.IdGen;
import com.mainsoft.mlp.reconciliation.modules.entity.PayCnpDealing;
import com.mainsoft.mlp.reconciliation.modules.entity.PayDefrayBill;
import com.mainsoft.mlp.reconciliation.modules.entity.PayEcsDealing;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpDealing;
import com.mainsoft.mlp.reconciliation.modules.entity.PayWcDealing;
import com.mainsoft.mlp.reconciliation.modules.entity.PayYeeDealing;
import com.mainsoft.mlp.reconciliation.modules.enums.PaymentDealingStatusEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PaymentDealingTransTypeEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PaymentDefrayBillStatusEnum;
import com.mainsoft.mlp.reconciliation.modules.external.service.PayDefrayBilExternalService;
import com.mainsoft.mlp.reconciliation.modules.unionpaySDK.SDKConstants;
import com.mainsoft.mlp.reconciliation.modules.utils.PayUtils;
import com.mainsoft.mlp.reconciliation.modules.sys.entity.User;

*/
/**
 * 为调用银行查询交易方法（获得交易成功信号）提供更改支付单状态的方法
 * @author guody
 * @date 2018年3月1日
 *//*


@Service
@Transactional(readOnly = true)
public class PayDefrayBillForQueryService {
	
	@Autowired
	private PayDefrayPaymentService payDefrayPaymentService;
	
	@Autowired
	private PayDefrayBillService payDefrayBillService;
	
	
	@Autowired
	private PayPaymentBillService payPaymentBillService;
	
	@Autowired
	private PayDefrayBilExternalService payDefrayBilExternalService;
	

	@Transactional(readOnly = false)
	public void updateForUnpPayDefrayBill(String payDefrayBillId, String remarks, Map<String, String> rspData) throws Exception{
		//判断之前是否已将接收过响应并将缴费单状态置为已缴费
		PayDefrayBill payDefrayBill = payDefrayBillService.get(payDefrayBillId);
		payDefrayBill.setRemarks(remarks);
		String txnAmt = rspData.get(SDKConstants.param_txnAmt);       //交易金额
		//String txnAmt = PayUtils.formatRevenueToFen(payDefrayBill.getRevenue());
		if(payDefrayBill != null && EnumsUtils.stringEquals(payDefrayBill.getStatus(), PaymentDefrayBillStatusEnum.ALREADY_PAID)){
			//此时支付单已经为支付成功状态，不进行任何操作			
		}else{
			//获得支付单对应的缴费单集合
			List<String> findPaymentBillIdList = payDefrayPaymentService.findPaymentBillIdList(payDefrayBill.getId());
			//判断是否所有的缴费单为未收费状态
			if (payPaymentBillService.isPayPaymentList(findPaymentBillIdList)) {
				//添加银联交易记录，将交易状态改为已收费
				String txnTime = rspData.get("txnTime");
				Date date = DateUtils.parseDate(txnTime, "yyyyMMddHHmmss");
				//Date date = new Date();	
				//通过支付单编码获得支付单对象							
				//构造银联在线交易记录
				//支付时间应该怎么确定   要确认一件事  银联的查询接口  如果支付成功的话 有没有 支付时间返回值  如果有时间 
				//这里的时间都应该以返回时间为准
				PayUnpDealing payUnpDealing = new PayUnpDealing(payDefrayBill.getMerchant(), payDefrayBill.getOrderCode(), DateUtils.formatDate(date, "HHmmss"), txnAmt, PaymentDealingTransTypeEnum.PAYMENT.getCode(),
						PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(),rspData.get(SDKConstants.param_signature), payDefrayBill.getId(), DateUtils.formatDate(date, "yyyyMMdd"),
						rspData.get(SDKConstants.param_currencyCode), payDefrayBill.getGateCode(), DateUtils.formatDate(date, "yyyyMMdd"), txnTime);						
				//缴费单支付成功				
				payDefrayBillService.unionPaySuccessForQuery(payDefrayBill, date, payUnpDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());													
			}else {			
				//执行设置此支付单为需要退款的方法					
				String txnTime = rspData.get("txnTime");
				Date date = DateUtils.parseDate(txnTime, "yyyyMMddHHmmss");
				//Date date = new Date();
				//构造银联在线交易记录
				PayUnpDealing payUnpDealing = new PayUnpDealing(payDefrayBill.getMerchant(), payDefrayBill.getOrderCode(), DateUtils.formatDate(date, "HHmmss"), txnAmt, PaymentDealingTransTypeEnum.PAYMENT.getCode(),
						PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(),rspData.get(SDKConstants.param_signature), payDefrayBill.getId(), DateUtils.formatDate(date, "yyyyMMdd"),
						rspData.get(SDKConstants.param_currencyCode), payDefrayBill.getGateCode(), DateUtils.formatDate(date, "yyyyMMdd"), txnTime);					
				 payDefrayBillService.unionPaySuccessNeedRefundForQuery(payDefrayBill, date, payUnpDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());													
			}			
		}
	}
	
	@Transactional(readOnly = false)
	public void updateForUnpPayDefrayBillForExternal(String payDefrayBillId, String remarks, Map<String, String> rspData) throws Exception{
		//判断之前是否已将接收过响应并将缴费单状态置为已缴费
		PayDefrayBill payDefrayBill = payDefrayBillService.get(payDefrayBillId);
		payDefrayBill.setRemarks(remarks);
		String txnAmt = rspData.get(SDKConstants.param_txnAmt);       //交易金额
		//String txnAmt = PayUtils.formatRevenueToFen(payDefrayBill.getRevenue());
		if(payDefrayBill != null && EnumsUtils.stringEquals(payDefrayBill.getStatus(), PaymentDefrayBillStatusEnum.ALREADY_PAID)){
			//此时支付单已经为支付成功状态，不进行任何操作			
		}else{
			//获得支付单对应的缴费单集合
			List<String> findPaymentBillIdList = payDefrayPaymentService.findPaymentBillIdList(payDefrayBill.getId());
			//判断是否所有的缴费单为未收费状态
			if (payPaymentBillService.isPayPaymentList(findPaymentBillIdList)) {
				//添加银联交易记录，将交易状态改为已收费
				String txnTime = rspData.get("txnTime");
				Date date = DateUtils.parseDate(txnTime, "yyyyMMddHHmmss");
				//Date date = new Date();	
				//通过支付单编码获得支付单对象							
				//构造银联在线交易记录
				//支付时间应该怎么确定   要确认一件事  银联的查询接口  如果支付成功的话 有没有 支付时间返回值  如果有时间 
				//这里的时间都应该以返回时间为准
				PayUnpDealing payUnpDealing = new PayUnpDealing(payDefrayBill.getMerchant(), payDefrayBill.getOrderCode(), DateUtils.formatDate(date, "HHmmss"), txnAmt, PaymentDealingTransTypeEnum.PAYMENT.getCode(),
						PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(),rspData.get(SDKConstants.param_signature), payDefrayBill.getId(), DateUtils.formatDate(date, "yyyyMMdd"),
						rspData.get(SDKConstants.param_currencyCode), payDefrayBill.getGateCode(), DateUtils.formatDate(date, "yyyyMMdd"), txnTime);						
				//缴费单支付成功				
				payDefrayBilExternalService.unionPaySuccessNeedRefundForExternal(payDefrayBill.getId(), date, payUnpDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());													
			}else {			
				//执行设置此支付单为需要退款的方法					
				String txnTime = rspData.get("txnTime");
				Date date = DateUtils.parseDate(txnTime, "yyyyMMddHHmmss");
				//Date date = new Date();
				//构造银联在线交易记录
				PayUnpDealing payUnpDealing = new PayUnpDealing(payDefrayBill.getMerchant(), payDefrayBill.getOrderCode(), DateUtils.formatDate(date, "HHmmss"), txnAmt, PaymentDealingTransTypeEnum.PAYMENT.getCode(),
						PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(),rspData.get(SDKConstants.param_signature), payDefrayBill.getId(), DateUtils.formatDate(date, "yyyyMMdd"),
						rspData.get(SDKConstants.param_currencyCode), payDefrayBill.getGateCode(), DateUtils.formatDate(date, "yyyyMMdd"), txnTime);					
				payDefrayBilExternalService.unionPaySuccessNeedRefundForExternal(payDefrayBill.getId(), date, payUnpDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());													
			}			
		}
	}
	
	@Transactional(readOnly = false)
	public void updateForUnpPayDefrayBill(String payDefrayBillId, String remarks) throws Exception{
		//判断之前是否已将接收过响应并将缴费单状态置为已缴费
		PayDefrayBill payDefrayBill = payDefrayBillService.get(payDefrayBillId);
		payDefrayBill.setRemarks(remarks);
		//String txnAmt = rspData.get(SDKConstants.param_txnAmt);       //交易金额
		String txnAmt = PayUtils.formatRevenueToFen(payDefrayBill.getRevenue());
		if(payDefrayBill != null && EnumsUtils.stringEquals(payDefrayBill.getStatus(), PaymentDefrayBillStatusEnum.ALREADY_PAID)){
			//此时支付单已经为支付成功状态，不进行任何操作			
		}else{
			//获得支付单对应的缴费单集合
			List<String> findPaymentBillIdList = payDefrayPaymentService.findPaymentBillIdList(payDefrayBill.getId());
			//判断是否所有的缴费单为未收费状态
			if (payPaymentBillService.isPayPaymentList(findPaymentBillIdList)) {
				//添加银联交易记录，将交易状态改为已收费
				//String txnTime = rspData.get("txnTime");
				//Date date = DateUtils.parseDate(txnTime, "yyyyMMddHHmmss");
				Date date = new Date();	
				//通过支付单编码获得支付单对象							
				//构造银联在线交易记录
				//支付时间应该怎么确定   要确认一件事  银联的查询接口  如果支付成功的话 有没有 支付时间返回值  如果有时间 
				//这里的时间都应该以返回时间为准
				PayUnpDealing payUnpDealing = new PayUnpDealing(payDefrayBill.getMerchant(), payDefrayBill.getOrderCode(), DateUtils.formatDate(date, "HHmmss"), txnAmt, PaymentDealingTransTypeEnum.PAYMENT.getCode(),
						PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(),"", payDefrayBill.getId(), DateUtils.formatDate(date, "yyyyMMdd"),
						"156", payDefrayBill.getGateCode(), DateUtils.formatDate(date, "yyyyMMdd"), DateUtils.formatDate(date, "yyyyMMddHHmmss"));						
				//缴费单支付成功				
				payDefrayBillService.unionPaySuccessForQuery(payDefrayBill, date, payUnpDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());													
			}else {			
				//执行设置此支付单为需要退款的方法					
				//String txnTime = rspData.get("txnTime");
				//Date date = DateUtils.parseDate(txnTime, "yyyyMMddHHmmss");
				Date date = new Date();
				//构造银联在线交易记录
				PayUnpDealing payUnpDealing = new PayUnpDealing(payDefrayBill.getMerchant(), payDefrayBill.getOrderCode(), DateUtils.formatDate(date, "HHmmss"), txnAmt, PaymentDealingTransTypeEnum.PAYMENT.getCode(),
						PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(),"", payDefrayBill.getId(), DateUtils.formatDate(date, "yyyyMMdd"),
						"156", payDefrayBill.getGateCode(), DateUtils.formatDate(date, "yyyyMMdd"), DateUtils.formatDate(date, "yyyyMMddHHmmss"));					
				 payDefrayBillService.unionPaySuccessNeedRefundForQuery(payDefrayBill, date, payUnpDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());													
			}			
		}
	}
	
	@Transactional(readOnly = false)
	public void updateForUnpPayCorpus(String payDefrayBillId) throws Exception{
		//判断之前是否已将接收过响应并将缴费单状态置为已缴费
		PayDefrayBill payDefrayBill = payDefrayBillService.get(payDefrayBillId);

		if(payDefrayBill != null && EnumsUtils.stringEquals(payDefrayBill.getStatus(), PaymentDefrayBillStatusEnum.ALREADY_PAID)){
			//此时支付单已经为支付成功状态，不进行任何操作
			payDefrayBillService.unionPaySuccessForCorpus(payDefrayBill);														
		}
	}
	
	@Transactional(readOnly = false)
	public void updateForEcsPayDefrayBill(String payDefrayBillId, String remarks) throws Exception{
		PayDefrayBill payDefrayBill = payDefrayBillService.get(payDefrayBillId);	
		payDefrayBill.setRemarks(remarks);
		if(payDefrayBill != null && EnumsUtils.stringEquals(payDefrayBill.getStatus(), PaymentDefrayBillStatusEnum.ALREADY_PAID)){
			//addMessage(redirectAttributes, "支付单号为："+payDefrayBillId+"的支付单已经被支付");
		}else{
			List<String> findPaymentBillIdList = payDefrayPaymentService.findPaymentBillIdList(payDefrayBill.getId());
			if (payPaymentBillService.isPayPaymentList(findPaymentBillIdList)) {
				//生成东方支付交易记录
				*/
/*PayEcsDealing ecsDealing = new PayEcsDealing(payDefrayBill.getId(), payDefrayBill.getOrderCode(), payDefrayBill.getRevenue().toString(), PaymentDealingTransTypeEnum.PAYMENT.getCode(),
						PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(), "付款时间", "币种", 
						"支付流水号", "付款方组织机构代码", "收款方组织机构代码", 
						 "CCYYMMDDhhmmssfff");	*//*

				PayEcsDealing ecsDealing = new PayEcsDealing(payDefrayBill.getId(), payDefrayBill.getOrderCode(), PayUtils.formatRevenueToFen(payDefrayBill.getRevenue()), PaymentDealingTransTypeEnum.PAYMENT.getCode(),
						PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(), "付款时间待更新", "人民币", 
						"待更新", "付款方组织机构代码", "收款方组织机构代码", "时间待更新");								
				//东方支付成功保存交易记录，更改缴费单状态
				payDefrayBillService.ecsPaySuccessForQuery(payDefrayBill, new Date(), ecsDealing, ecsDealing.getPayeeCode(), payDefrayBill.getOrderCode());									
			}else {
				//生成东方支付交易记录
				PayEcsDealing ecsDealing = new PayEcsDealing(payDefrayBill.getId(), payDefrayBill.getOrderCode(), payDefrayBill.getRevenue().toString(), PaymentDealingTransTypeEnum.PAYMENT.getCode(),
						PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(), "付款时间待更新", "人民币", 
						"待更新", "付款方组织机构代码", "收款方组织机构代码", "时间待更新");										
					//东方支付成功但需要退款保存交易记录，更改缴费单状态
					payDefrayBillService.ecsPaySuccessNeedRefundForQuery(payDefrayBill, new Date(), ecsDealing, ecsDealing.getPayeeCode(), payDefrayBill.getOrderCode());
					//addMessage(redirectAttributes, "支付单号为："+payDefrayBillId+"支付成功");				
			}
		}
		
	}

	*/
/**
	 * 更新易宝支付单的收费状态
	 * 2018.04.04 guodongyu
	 * 暂时不进行处理
	 * @param id
	 * @param remarks
	 * @throws Exception 
	 *//*

	public void updateForYeePayDefrayBill(String payDefrayBillId, String remarks) throws Exception {
		// TODO Auto-generated method stub
		
		PayDefrayBill payDefrayBill = payDefrayBillService.get(payDefrayBillId);	
		payDefrayBill.setRemarks(remarks);
		if(payDefrayBill != null && EnumsUtils.stringEquals(payDefrayBill.getStatus(), PaymentDefrayBillStatusEnum.ALREADY_PAID)){
			//addMessage(redirectAttributes, "支付单号为："+payDefrayBillId+"的支付单已经被支付");
		}else{
			List<String> findPaymentBillIdList = payDefrayPaymentService.findPaymentBillIdList(payDefrayBill.getId());
			if (payPaymentBillService.isPayPaymentList(findPaymentBillIdList)) {
				//生成易宝支付交易记录
				Date date = new Date();	
				User createBy = payDefrayBill.getCreateBy();	
				//构造银联在线交易记录					
				PayYeeDealing payYeeDealing = new PayYeeDealing(payDefrayBill.getMerchant(), payDefrayBill.getOrderCode(), payDefrayBill.getRevenue().toString(), "Buy",
						"1", "验证签名待更新", payDefrayBillId, 
						"RMB", "2",  "时间待更新");
				payYeeDealing.setCreateBy(createBy);
				payYeeDealing.setUpdateBy(createBy);
				payYeeDealing.setCreateDate(date);
				payYeeDealing.setUpdateDate(date);
				payYeeDealing.setId(IdGen.uuid());
				payDefrayBilExternalService.yeePaySuccessForExternal(payDefrayBillId, date, payYeeDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());
				//payDefrayBillService.ecsPaySuccessForQuery(payDefrayBill, new Date(), payYeeDealing, payYeeDealing.getPayeeCode(), payDefrayBill.getOrderCode());									
			}else {
				//生成易宝支付交易记录
				Date date = new Date();	
				User createBy = payDefrayBill.getCreateBy();
				//构造银联在线交易记录					
				PayYeeDealing payYeeDealing = new PayYeeDealing(payDefrayBill.getMerchant(), payDefrayBill.getOrderCode(), payDefrayBill.getRevenue().toString(), "Buy",
						"1", "验证签名待更新", payDefrayBillId, 
						"RMB", "2",  "时间待更新");
				payYeeDealing.setCreateBy(createBy);
				payYeeDealing.setUpdateBy(createBy);
				payYeeDealing.setCreateDate(date);
				payYeeDealing.setUpdateDate(date);
				payYeeDealing.setId(IdGen.uuid());										
				//易宝支付成功但需要退款保存交易记录，更改缴费单状态
				payDefrayBilExternalService.yeePaySuccessNeedRefundForExternal(payDefrayBillId, date, payYeeDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());
							
			}
		}
		
	}

	*/
/**
	 * 更新中国银联支付单的收费状态
	 * 2018.05.16 guodongyu
	 *
	 * @param id
	 * @param remarks
	 * @throws Exception 
	 *//*

	public void updateForCnpPayDefrayBill(String payDefrayBillId, String remarks) throws Exception {
		// TODO Auto-generated method stub
		
		PayDefrayBill payDefrayBill = payDefrayBillService.get(payDefrayBillId);	
		payDefrayBill.setRemarks(remarks);
		if(payDefrayBill != null && EnumsUtils.stringEquals(payDefrayBill.getStatus(), PaymentDefrayBillStatusEnum.ALREADY_PAID)){
			//addMessage(redirectAttributes, "支付单号为："+payDefrayBillId+"的支付单已经被支付");
		}else{
			List<String> findPaymentBillIdList = payDefrayPaymentService.findPaymentBillIdList(payDefrayBill.getId());
			if (payPaymentBillService.isPayPaymentList(findPaymentBillIdList)) {
				//生成中国银联在线交易记录	
				Date date = new Date();	
				User createBy = payDefrayBill.getCreateBy();	
				//构造中国银联在线交易记录									
				PayCnpDealing payCnpDealing = new PayCnpDealing(payDefrayBill.getMerchant(), payDefrayBill.getOrderCode(), DateUtils.formatDate(date, "HHmmss"), payDefrayBill.getRevenue().toString(), PaymentDealingTransTypeEnum.PAYMENT.getCode(),
					PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(), "验签字段", "私有域", DateUtils.formatDate(date, "yyyyMMdd"),
					"156", payDefrayBill.getGateCode(), DateUtils.formatDate(date, "yyyyMMdd"), null, null, DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
				payCnpDealing.setCreateBy(createBy);
				payCnpDealing.setUpdateBy(createBy);
				payCnpDealing.setCreateDate(date);
				payCnpDealing.setUpdateDate(date);
				payCnpDealing.setId(IdGen.uuid());
				payDefrayBilExternalService.chainaPaySuccessForExternal(payDefrayBillId, date, payCnpDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());																
			}else {
				//生成中国银联在线交易记录	
				Date date = new Date();	
				User createBy = payDefrayBill.getCreateBy();	
				//构造中国银联在线交易记录									
				PayCnpDealing payCnpDealing = new PayCnpDealing(payDefrayBill.getMerchant(), payDefrayBill.getOrderCode(), DateUtils.formatDate(date, "HHmmss"), payDefrayBill.getRevenue().toString(), PaymentDealingTransTypeEnum.PAYMENT.getCode(),
					PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(), "验签字段", "私有域", DateUtils.formatDate(date, "yyyyMMdd"),
					"156", payDefrayBill.getGateCode(), DateUtils.formatDate(date, "yyyyMMdd"), null, null, DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
				payCnpDealing.setCreateBy(createBy);
				payCnpDealing.setUpdateBy(createBy);
				payCnpDealing.setCreateDate(date);
				payCnpDealing.setUpdateDate(date);
				payCnpDealing.setId(IdGen.uuid());							
				//易宝支付成功但需要退款保存交易记录，更改缴费单状态
				payDefrayBilExternalService.chainaPaySuccessNeedRefundForExternal(payDefrayBillId, date, payCnpDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());
							
			}
		}
		
	}
	
	@Transactional(readOnly = false)
	public void updateForWechatPayDefrayBill(String payDefrayBillId, String remarks) throws Exception{
		//判断之前是否已将接收过响应并将缴费单状态置为已缴费
		PayDefrayBill payDefrayBill = payDefrayBillService.get(payDefrayBillId);
		payDefrayBill.setRemarks(remarks);
		//String txnAmt = rspData.get(SDKConstants.param_txnAmt);       //交易金额
		String txnAmt = PayUtils.formatRevenueToFen(payDefrayBill.getRevenue());
		if(payDefrayBill != null && EnumsUtils.stringEquals(payDefrayBill.getStatus(), PaymentDefrayBillStatusEnum.ALREADY_PAID)){
			//此时支付单已经为支付成功状态，不进行任何操作			
		}else{
			//获得支付单对应的缴费单集合
			List<String> findPaymentBillIdList = payDefrayPaymentService.findPaymentBillIdList(payDefrayBill.getId());
			//判断是否所有的缴费单为未收费状态
			if (payPaymentBillService.isPayPaymentList(findPaymentBillIdList)) {
				//添加银联交易记录，将交易状态改为已收费
				//String txnTime = rspData.get("txnTime");
				//Date date = DateUtils.parseDate(txnTime, "yyyyMMddHHmmss");
				Date date = new Date();	
				//通过支付单编码获得支付单对象							
				//构造银联在线交易记录
				//支付时间应该怎么确定   要确认一件事  银联的查询接口  如果支付成功的话 有没有 支付时间返回值  如果有时间 
				//这里的时间都应该以返回时间为准
				PayWcDealing payWcDealing = new PayWcDealing();
				payWcDealing.setMerchant(payDefrayBill.getMerchant());
				payWcDealing.setDefrayBill(payDefrayBill.getId());
				payWcDealing.setAmount(txnAmt);
				
				payWcDealing.setPriv1(payDefrayBill.getId());
				payWcDealing.setMessage(remarks);
				payWcDealing.setPayTime(DateUtils.formatDate(date, "yyyyMMddHHmmss"));
				payWcDealing.setOrderCode(payDefrayBill.getOrderCode());
				User user=payDefrayBill.getCreateBy();
				payWcDealing.setCreateBy(user);
				payWcDealing.setCreateDate(date);
				payWcDealing.setUpdateBy(user);
				payWcDealing.setUpdateDate(date);
				//payWcDealing.setTransDate(payWcDealing.getPayTime().substring(0,8));
				payWcDealing.setProcessDate(DateUtils.formatDate(date, "yyyyMMdd"));
				payWcDealing.setCurrency(PaymentDealingTransTypeEnum.PAYMENT.getCode());
				//payWcDealing.setSendTime(payUnpDealing.getPayTime().substring(8,14));
				payWcDealing.setTransType("0001");
				payWcDealing.setStatus("1001");
				//缴费单支付成功				
				payDefrayBillService.wechatPaySuccessForQuery(payDefrayBill, date, payWcDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());													
			}else {			
				//执行设置此支付单为需要退款的方法					
				//String txnTime = rspData.get("txnTime");
				//Date date = DateUtils.parseDate(txnTime, "yyyyMMddHHmmss");
				Date date = new Date();
				//构造银联在线交易记录
				PayUnpDealing payUnpDealing = new PayUnpDealing(payDefrayBill.getMerchant(), payDefrayBill.getOrderCode(), DateUtils.formatDate(date, "HHmmss"), txnAmt, PaymentDealingTransTypeEnum.PAYMENT.getCode(),
						PaymentDealingStatusEnum.PAYMENT_SUCCESS.getCode(),"", payDefrayBill.getId(), DateUtils.formatDate(date, "yyyyMMdd"),
						"156", payDefrayBill.getGateCode(), DateUtils.formatDate(date, "yyyyMMdd"), DateUtils.formatDate(date, "yyyyMMddHHmmss"));					
				 payDefrayBillService.unionPaySuccessNeedRefundForQuery(payDefrayBill, date, payUnpDealing, payDefrayBill.getMerchant(), payDefrayBill.getOrderCode());													
			}			
		}
	}
	
}
*/
