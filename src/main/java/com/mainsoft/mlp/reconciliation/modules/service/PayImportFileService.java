
package com.mainsoft.mlp.reconciliation.modules.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mainsoft.mlp.reconciliation.modules.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import com.mainsoft.mlp.common.utils.Collections3;
import com.mainsoft.mlp.common.utils.CurrencyUtils;
import com.mainsoft.mlp.common.utils.DateUtils;
import com.mainsoft.mlp.common.utils.EnumsUtils;
import com.mainsoft.mlp.common.utils.ListsUtils;
import com.mainsoft.mlp.reconciliation.common.config.Global;
import com.mainsoft.mlp.reconciliation.common.persistence.Page;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;
import com.mainsoft.mlp.reconciliation.common.utils.StringUtils;
import com.mainsoft.mlp.reconciliation.modules.dao.PayImportFileDao;
import com.mainsoft.mlp.reconciliation.modules.enums.PayDefrayBillAvenueEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PayImportFileStatusEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PayImportFileTypeEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PaymentTypeForImportFileEnum;
import com.mainsoft.mlp.reconciliation.modules.utils.PayUtils;

/**
 * 导入文件Service
 * @author ZhangSC
 * @version 2017-04-01
 */
@Service
@Slf4j
@PropertySource("classpath:cron.props")
@Transactional(readOnly = true)
public class PayImportFileService extends CrudService<PayImportFileDao, PayImportFile> {


	@Autowired
	private PayUnpReconcileService payUnpReconcileService;
	@Autowired
	private PayUnpReconcileDetailService payUnpReconcileDetailService;
	@Autowired
	private PayPaymentBillService payPaymentBillService;

	@Autowired
	private PayDefrayBillService payDefrayBillService;

	@Autowired
	private PayImportFileDao payImportFileDao;

    @Autowired
    private PayWcReconcileService payWcReconcileService;

    @Autowired
    private PayWcReconcileDetailService payWcReconcileDetailService;
	
	
	public PayImportFile get(String id) {
		return super.get(id);
	}
	
	public List<PayImportFile> findList(PayImportFile payImportFile) {
		return super.findList(payImportFile);
	}

	public Page<PayImportFile> findPage(Page<PayImportFile> page, PayImportFile payImportFile) {
		return super.findPage(page, payImportFile);
	}
	
	@Transactional(readOnly = false)
	public void save(PayImportFile payImportFile) {
		super.save(payImportFile);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayImportFile payImportFile) {
		super.delete(payImportFile);
	}
	

	
	/**
	 * 保存银联在线的对账文件
	 * @param payImportFile
	 * @param payUnpReconcile
	 */
	@Transactional(readOnly = false)
	public void saveUNPPayImportFile(PayImportFile payImportFile, PayUnpReconcile payUnpReconcile){
		save(payImportFile);  //保存对账文件
		payUnpReconcile.setId(payImportFile.getId());  //将中国银联对账详情与文件关联
		payUnpReconcileService.savePayUnpReconcile(payUnpReconcile); //保存中国银联的对账详情
	}

    /**
     * 保存微信对账文件
     * @param payImportFile
     * @param payWcReconcile
     */
    @Transactional(readOnly = false)
    public void saveWcPayImportFile(PayImportFile payImportFile, PayWcReconcile payWcReconcile) {

        save(payImportFile);  //保存对账文件
        payWcReconcile.setId(payImportFile.getId());  //将微信对账详情与文件关联
        payWcReconcileService.saveWcReconcile(payWcReconcile); //保存微信的对账详情
    }


	
	/**
	 * 对账入口
	 * <p>用定时任务执行</p>
	 * @author Guodongyu
	 * @version 2019-01-28
	 */

	@Transactional(readOnly = false)
    @Scheduled(cron="${jobs.schedule.reconcile}")
	public void verifyEntrance(){
		Date settleDate = DateUtils.addDays(new Date(), -2);  //清算日期，根据该日期去找当天的账单进行对账
		log.info("开始对账，对账日期："+DateUtils.formatDate(settleDate, "yyyy年MM月dd日"));
		UNPVerify(settleDate);     //unionPay对账
        WCVerify(settleDate);      //微信对账


	}
	
	/**
	 * 对账入口 对于外部系统的银联支付记录
	 * <p>用定时任务执行</p>
	 * @author guodongyu
	 * @version 2018-06-11
	 */
	@Transactional(readOnly = false)
	public void verifyEntranceForExternal(){
		Date settleDate = DateUtils.addDays(new Date(), -2);  //清算日期，根据该日期去找当天的账单进行对账
		logger.info("开始对账，对账日期："+DateUtils.formatDate(settleDate, "yyyy年MM月dd日"));
		UNPVerifyForExternal(settleDate);     //unionPay对账
	}
	
	/**
	 * &#x94f6;&#x8054;&#x5728;&#x7ebf;&#x5bf9;&#x8d26;
	 * @param settleDate
	 * @author Guodonguyu
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public void UNPVerify(Date settleDate){
		//获取未对账的unionPay对账文件
		PayImportFile payImportFile = new PayImportFile();
		payImportFile.setBeginSettleDate(settleDate);
		payImportFile.setEndSettleDate(settleDate);
		payImportFile.setStatus(PayImportFileStatusEnum.NOT_HANDLE.getCode());
		payImportFile.setType(PayImportFileTypeEnum.UNION_PAY.getCode());
		/**
		 * 2018.06.11 guodongyu
		 * 获得港建费相关的对账文件
		 */
		payImportFile.setPaymentType(PaymentTypeForImportFileEnum.PORTPAYMENT.getCode());		
		List<PayImportFile> payImportFileList = findListByPaymentType(payImportFile);
		
		//如果当天无对账文件，不需要对账
		if(ListsUtils.isEmpty(payImportFileList)){
            logger.info("对账日期："+DateUtils.formatDate(settleDate, "yyyy年MM月dd日")+"无对账文件");
			return; 
		}
		PayUnpReconcileDetail payUnpReconcileDetail = new PayUnpReconcileDetail();  //银联在线对账明细用于查询
		//PayUnpReconcile payUnpReconcile = new PayUnpReconcile();
		Integer yes = Integer.parseInt(Global.YES);   //yes的int类型
		Integer no = Integer.parseInt(Global.NO);		//no的int类型
		/**
		 * 2017.11.29 guodongyu 
		 * 更改网上缴费模块后，发送给银行的商户私有域中存储的是支付单编码
		 * 所以注释掉下面的缴费单实体类
		 */
		//创建支付单实体类
		PayDefrayBill payDefrayBill = new PayDefrayBill();
		/**
		 * 遍历得到的对账文件对象
		 * 将对账文件id放入到银联对账明细中
		 * 通过对账文件id查询到银联对账明细信息集合
		 */
		analysisPayImportFile(payImportFileList, payUnpReconcileDetail, yes, no, payDefrayBill);
	}

    /**
     * &#x94f6;&#x8054;&#x5728;&#x7ebf;&#x5bf9;&#x8d26;
     * @param settleDate
     * @author Guodonguyu
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false)
    public void WCVerify(Date settleDate){
        //获取未对账的微信对账文件
        PayImportFile payImportFile = new PayImportFile();
        payImportFile.setBeginSettleDate(settleDate);
        payImportFile.setEndSettleDate(settleDate);
        payImportFile.setStatus(PayImportFileStatusEnum.NOT_HANDLE.getCode());
        payImportFile.setType(PayImportFileTypeEnum.WECHAT_PAY.getCode());
        payImportFile.setPaymentType(PaymentTypeForImportFileEnum.PORTPAYMENT.getCode());
        List<PayImportFile> payImportFileList = findListByPaymentType(payImportFile);

        //如果当天无对账文件，不需要对账
        if(ListsUtils.isEmpty(payImportFileList)){
            log.info("对账日期："+DateUtils.formatDate(settleDate, "yyyy年MM月dd日")+"无微信对账文件");
            return;
        }
        PayWcReconcileDetail payWcReconcileDetail = new PayWcReconcileDetail();  //银联在线对账明细用于查询
        //PayUnpReconcile payUnpReconcile = new PayUnpReconcile();
        Integer yes = Integer.parseInt(Global.YES);   //yes的int类型
        Integer no = Integer.parseInt(Global.NO);		//no的int类型
        /**
         * 2017.11.29 guodongyu
         * 更改网上缴费模块后，发送给银行的商户私有域中存储的是支付单编码
         * 所以注释掉下面的缴费单实体类
         */
        //创建支付单实体类
        PayDefrayBill payDefrayBill = new PayDefrayBill();
        /**
         * 遍历得到的对账文件对象
         * 将对账文件id放入到银联对账明细中
         * 通过对账文件id查询到银联对账明细信息集合
         */
        analysisPayImportFileForWeChat(payImportFileList, payWcReconcileDetail, yes, no, payDefrayBill);
    }



	private void analysisPayImportFile(List<PayImportFile> payImportFileList, PayUnpReconcileDetail payUnpReconcileDetail, Integer yes, Integer no, PayDefrayBill payDefrayBill) {
		for (PayImportFile importFile : payImportFileList) {

			payUnpReconcileDetail.setId(importFile.getId());
			//获取该文件的unionpay对账流水详情
			//2018.04.10 guodongyu 这里的查询出错 应该根据对账文件id查询所对应的对账明细集合
			List<PayUnpReconcileDetail> payUnpReconcileDetailList = payUnpReconcileDetailService.findListByImportFileId(payUnpReconcileDetail);
			//获取对账流水详情集合中的支付单编码
			payDefrayBill.setPayDefrayBillList(Collections3.extractToList(payUnpReconcileDetailList, "priv1"));

			//查询对账文件所对应的缴费单
			//List<PayPaymentBill> payPaymentBillList = payPaymentBillService.findList(paramPaymentBill);
			//Map<String, BigDecimal> paymentBillMap = Collections3.extractToMap(payPaymentBillList, "id", "revenue");

			//查询对账文件中所对应的支付单信息
			//2018.04.10 guodongyu 这里应该根据PayDefrayBillList查询对应的支付单信息
			List<PayDefrayBill> payDefrayBillList = payDefrayBillService.findListByPayDefrayBillList(payDefrayBill);
			Map<String, BigDecimal> payDefrayBillMap = Collections3.extractToMap(payDefrayBillList, "id", "revenue");


			List<String> verifySuccessPayDefrayBillList = Lists.newArrayList();  //对账成功的支付单id list
			List<String> verifyFailPayDefrayBillList = Lists.newArrayList();  //对账失败的支付单id List

			Integer verifyFailNum = 0;   //对账失败数量
			for (PayUnpReconcileDetail unpReconcileDetai : payUnpReconcileDetailList) {
				BigDecimal payDefrayBillRevenue = payDefrayBillMap.get(unpReconcileDetai.getPriv1());

				//如果未找到对应的缴费单
				if(payDefrayBillRevenue == null){
					unpReconcileDetai.setSuccess(yes);
					unpReconcileDetai.setMessage("对账失败，未找到支付单"+unpReconcileDetai.getPriv1());
					payUnpReconcileDetailService.updUnpDetailReconcileStatus(unpReconcileDetai);  //更改银联对账记录详情的对账状态为失败
					verifyFailNum++;
					continue;
				}

				//如果对账金额相同，则对账成功，否则对账失败
				if(CurrencyUtils.compare(PayUtils.parseFenRevenueStr(unpReconcileDetai.getTransAmount()),
						payDefrayBillMap.get(unpReconcileDetai.getPriv1())) == 0){
					verifySuccessPayDefrayBillList.add(unpReconcileDetai.getPriv1());  //将对账成功的支付单记录下来

					unpReconcileDetai.setSuccess(yes);
					payUnpReconcileDetailService.updUnpDetailReconcileStatus(unpReconcileDetai);  //更改unionPay对账记录的对账状态为成功
				}else{
					verifyFailPayDefrayBillList.add(unpReconcileDetai.getPriv1());     //将对账失败的支付单记录下来

					unpReconcileDetai.setSuccess(no);
					unpReconcileDetai.setMessage("支付金额不同，支付单应缴金额为" + payDefrayBillRevenue);
					payUnpReconcileDetailService.updUnpDetailReconcileStatus(unpReconcileDetai);  //更改unionPay对账记录的对账状态为失败
					verifyFailNum++;
				}
			}

			/**
			 *
			 * 这里不能直接更新缴费单的对账状态
			 * 因为此时成功集合与失败集合里面存放的是支付单编码
			 * 所以在更新缴费单对账状态之前要进行一次查询
			 * 根据支付单编码集合查询对应的缴费单编码集合
			 */
			//更改对账成功的缴费的对账状态
			payPaymentBillService.paymentBillVerifySuccess(verifySuccessPayDefrayBillList);
			//更改对账失败的缴费的对账状态
			payPaymentBillService.paymentBillVerifyFail(verifyFailPayDefrayBillList);

			//更改unionPay对账记录的对账状态
			PayUnpReconcile payUnpReconcile = new PayUnpReconcile(importFile.getId());

			//判断对账失败的数量，更改unionPayReconcile和payImportFile的处理状态
			if(verifyFailNum > 0){
				payUnpReconcile.setSuccess(yes);
				payUnpReconcile.setMessage(verifyFailNum.toString());  //message为对账失败的数量
				importFile.setStatus(PayImportFileStatusEnum.HANDLE_FAIL.getCode());  //更改payImportFile的处理状态为失败
				importFile.setMessage(verifyFailNum.toString()); //message为对账失败的数量
			}else{
				payUnpReconcile.setSuccess(yes);                 //unionPay对账成功
				importFile.setStatus(PayImportFileStatusEnum.HANDLE_SUCCESS.getCode());  //更改payImportFile的处理状态为成功
			}
			payUnpReconcileService.updUnpReconcileStatus(payUnpReconcile);   //更改unpReconcile中国银联对账详情的对账状态
			updPayImportFileStatus(importFile);                             //更改payImportFile导入文件的处理状态
		}
	}


    /**
     * 微信对账逻辑
     * @param payImportFileList
     * @param payWcReconcileDetail
     * @param yes
     * @param no
     * @param payDefrayBill
     */
    private void analysisPayImportFileForWeChat(List<PayImportFile> payImportFileList, PayWcReconcileDetail payWcReconcileDetail, Integer yes, Integer no, PayDefrayBill payDefrayBill) {
        for (PayImportFile importFile : payImportFileList) {

            payWcReconcileDetail.setId(importFile.getId());
            //获取该文件的unionpay对账流水详情
            //2018.04.10 guodongyu 这里的查询出错 应该根据对账文件id查询所对应的对账明细集合
            List<PayWcReconcileDetail> payWcReconcileDetailList = payWcReconcileDetailService.findListByImportFileId(payWcReconcileDetail);
            //微信对账文件中以收款流水号对应支付单表中的订单编码
            payDefrayBill.setOrderCodeList(Collections3.extractToList(payWcReconcileDetailList, "orderCode"));
            //查询对账文件中所对应的支付单信息
            //2018.04.10 guodongyu 这里应该根据PayDefrayBillList查询对应的支付单信息
            List<PayDefrayBill> payDefrayBillList = payDefrayBillService.findListByOrderCodeList(payDefrayBill);

            //生成订单编码与金额的对应map
            Map<String, BigDecimal> orderCodeRevenueMap = Collections3.extractToMap(payDefrayBillList, "orderCode", "revenue");

            //生成订单编码与支付单编码的对应map
            Map<String, String> orderCodeIdMap = Collections3.extractToMap(payDefrayBillList, "orderCode", "id");


            List<String> verifySuccessPayDefrayBillList = Lists.newArrayList();  //对账成功的支付单id list
            List<String> verifyFailPayDefrayBillList = Lists.newArrayList();  //对账失败的支付单id List

            Integer verifyFailNum = 0;   //对账失败数量
            for (PayWcReconcileDetail wcReconcileDetail : payWcReconcileDetailList) {
                BigDecimal payDefrayBillRevenue = orderCodeRevenueMap.get(payWcReconcileDetail.getOrderCode());

                //如果未找到对应的缴费单
                if(payDefrayBillRevenue == null){
                    wcReconcileDetail.setSuccess(no);
                    wcReconcileDetail.setMessage("对账失败，未找到支付单"+orderCodeIdMap.get(payWcReconcileDetail.getOrderCode()));
                    payWcReconcileDetailService.updWcDetailReconcileStatus(wcReconcileDetail);  //更改微信对账记录详情的对账状态为失败
                    verifyFailNum++;
                    continue;
                }

                //如果对账金额相同，则对账成功，否则对账失败
                if(CurrencyUtils.compare(PayUtils.parseFenRevenueStr(payWcReconcileDetail.getTransAmount()),
                        orderCodeRevenueMap.get(payWcReconcileDetail.getOrderCode())) == 0){
                    verifySuccessPayDefrayBillList.add(orderCodeIdMap.get(payWcReconcileDetail.getOrderCode()));  //将对账成功的支付单记录下来

                    payWcReconcileDetail.setSuccess(yes);
                    payWcReconcileDetailService.updWcDetailReconcileStatus(payWcReconcileDetail);  //更改微信对账详情记录的对账状态为成功
                }else{
                    verifyFailPayDefrayBillList.add(orderCodeIdMap.get(payWcReconcileDetail.getOrderCode()));     //将对账失败的支付单记录下来

                    payWcReconcileDetail.setSuccess(no);
                    payWcReconcileDetail.setMessage(orderCodeIdMap.get(payWcReconcileDetail.getOrderCode()+"支付金额不同，支付单应缴金额为" + payDefrayBillRevenue));
                    payWcReconcileDetailService.updWcDetailReconcileStatus(payWcReconcileDetail);  //更改微信对账详情记录的对账状态为失败
                    verifyFailNum++;
                }
            }

            /**
             *
             * 这里不能直接更新缴费单的对账状态
             * 因为此时成功集合与失败集合里面存放的是支付单编码
             * 所以在更新缴费单对账状态之前要进行一次查询
             * 根据支付单编码集合查询对应的缴费单编码集合
             */
            //更改对账成功的缴费的对账状态
            payPaymentBillService.paymentBillVerifySuccess(verifySuccessPayDefrayBillList);
            //更改对账失败的缴费的对账状态
            payPaymentBillService.paymentBillVerifyFail(verifyFailPayDefrayBillList);

            //更改微信对账记录的对账状态
            PayWcReconcile payWcReconcile = new PayWcReconcile(importFile.getId());

            //判断对账失败的数量，更改unionPayReconcile和payImportFile的处理状态
            if(verifyFailNum > 0){
                payWcReconcile.setSuccess(yes);
                payWcReconcile.setMessage(verifyFailNum.toString());  //message为对账失败的数量
                importFile.setStatus(PayImportFileStatusEnum.HANDLE_FAIL.getCode());  //更改payImportFile的处理状态为失败
                importFile.setMessage(verifyFailNum.toString()); //message为对账失败的数量
            }else{
                payWcReconcile.setSuccess(yes);                 //unionPay对账成功
                importFile.setStatus(PayImportFileStatusEnum.HANDLE_SUCCESS.getCode());  //更改payImportFile的处理状态为成功
            }
            payWcReconcileService.updWcReconcileStatus(payWcReconcile);   //更改微信对账详情的对账状态
            updPayImportFileStatus(importFile);                             //更改payImportFile导入文件的处理状态
        }
    }


	/**
	 * 获取对账文件集合
	 * @param payImportFile
	 * @return
	 */
	public List<PayImportFile> findListByPaymentType(PayImportFile payImportFile) {
		// TODO Auto-generated method stub
		return dao.findListByPaymentType(payImportFile);
	}

	
	/**
	 * 更改对账导入文件的处理状态
	 * @param payImportFile
	 */
	@Transactional(readOnly = false)
	public void updPayImportFileStatus(PayImportFile payImportFile){
		payImportFile.setHandleTime(new Date());
		dao.updPayImportFileStatus(payImportFile);
	}
	
	/**
	 * 银联在线对账,支撑外部系统
	 * @param settleDate
	 * @author guodongyu
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public void UNPVerifyForExternal(Date settleDate){
		//获取未对账的unionPay对账文件
		PayImportFile payImportFile = new PayImportFile();
		payImportFile.setBeginSettleDate(settleDate);
		payImportFile.setEndSettleDate(settleDate);
		payImportFile.setStatus(PayImportFileStatusEnum.NOT_HANDLE.getCode());
		payImportFile.setType(PayImportFileTypeEnum.UNION_PAY.getCode());
		/**
		 * 2018.06.11 guodongyu
		 * 获得规费相关的对账文件
		 */
		payImportFile.setPaymentType(PaymentTypeForImportFileEnum.FEESPAYMENT.getCode());	
		List<PayImportFile> payImportFileList = findListByPaymentType(payImportFile);

		//如果当天无对账文件，不需要对账
		if(ListsUtils.isEmpty(payImportFileList)){
			return; 
		}
		PayUnpReconcileDetail payUnpReconcileDetail = new PayUnpReconcileDetail();  //银联在线对账明细用于查询
		//PayUnpReconcile payUnpReconcile = new PayUnpReconcile();
		Integer yes = Integer.parseInt(Global.YES);   //yes的int类型
		Integer no = Integer.parseInt(Global.NO);		//no的int类型
		
		//创建支付单实体类
		PayDefrayBill payDefrayBill = new PayDefrayBill();
		
		/**
		 * 遍历得到的对账文件对象
		 * 将对账文件id放入到银联对账明细中
		 * 通过对账文件id查询到银联对账明细信息集合
		 */
		analysisPayImportFile(payImportFileList, payUnpReconcileDetail, yes, no, payDefrayBill);
	}

	/**
	 * 2018.06.29 guodongyu
	 * 根据商户号，清算日期，对账文件类型（港建费，非税）查询满足的对账文件集合
	 * @param merchant
	 * @param settleDate
	 * @param paymentType
	 */
	public List<PayImportFile> checkFile(String merchant, Date settleDate,String paymentType) {		
		//获取未对账的unionPay对账文件
		PayImportFile payImportFile = new PayImportFile();
		payImportFile.setBeginSettleDate(settleDate);
		payImportFile.setEndSettleDate(settleDate);
		payImportFile.setStatus(PayImportFileStatusEnum.NOT_HANDLE.getCode());
		payImportFile.setType(PayImportFileTypeEnum.UNION_PAY.getCode());		
		payImportFile.setPaymentType(paymentType);
		
		List<PayImportFile> payImportFileList = dao.findListForHandwork(payImportFile);
		return payImportFileList;
		
	}

	/**
	 * 2018.06.29 guodongyu
	 * 手工对账逻辑
	 * @param fileList
	 * @param paymentType
	 * 
	 */
	public void unpVerifyForHandwork(List<PayImportFile> fileList,String paymentType) {
		// TODO Auto-generated method stub
		if(EnumsUtils.stringEquals(paymentType, PaymentTypeForImportFileEnum.PORTPAYMENT)){
			//执行港建费对账文件的对账逻辑
			unpVerifyForHandworkForPort(fileList);
			
		}else if (EnumsUtils.stringEquals(paymentType, PaymentTypeForImportFileEnum.FEESPAYMENT)) {
			unpVerifyForHandworkForFees(fileList);
		}
		
	}

	/**
	 * 2018.06.29 guodongyu
	 * 手工对账  港建费逻辑
	 * @param payImportFileList
	 */
	@SuppressWarnings("unchecked")
	private void unpVerifyForHandworkForPort(List<PayImportFile> payImportFileList) {
		PayUnpReconcileDetail payUnpReconcileDetail = new PayUnpReconcileDetail();  //银联在线对账明细用于查询
		//PayUnpReconcile payUnpReconcile = new PayUnpReconcile();
		Integer yes = Integer.parseInt(Global.YES);   //yes的int类型
		Integer no = Integer.parseInt(Global.NO);		//no的int类型
		
		/**
		 * 2017.11.29 guodongyu 
		 * 更改网上缴费模块后，发送给银行的商户私有域中存储的是支付单编码
		 * 所以注释掉下面的缴费单实体类
		 */

		//创建支付单实体类
		PayDefrayBill payDefrayBill = new PayDefrayBill();
		
		/**
		 * 遍历得到的对账文件对象
		 * 将对账文件id放入到银联对账明细中
		 * 通过对账文件id查询到银联对账明细信息集合
		 */
		analysisPayImportFile(payImportFileList, payUnpReconcileDetail, yes, no, payDefrayBill);

	}

	
	
	/**
	 * 2018.06.29 guodongyu
	 * 手工对账  规费逻辑
	 */
	@SuppressWarnings("unchecked")
	private void unpVerifyForHandworkForFees(List<PayImportFile> payImportFileList){
		
		PayUnpReconcileDetail payUnpReconcileDetail = new PayUnpReconcileDetail();  //银联在线对账明细用于查询
		//PayUnpReconcile payUnpReconcile = new PayUnpReconcile();
		Integer yes = Integer.parseInt(Global.YES);   //yes的int类型
		Integer no = Integer.parseInt(Global.NO);		//no的int类型
		
		//创建支付单实体类
		PayDefrayBill payDefrayBill = new PayDefrayBill();
		
		/**
		 * 遍历得到的对账文件对象
		 * 将对账文件id放入到银联对账明细中
		 * 通过对账文件id查询到银联对账明细信息集合
		 */
		analysisPayImportFile(payImportFileList, payUnpReconcileDetail, yes, no, payDefrayBill);
	}

	public Integer getTotal(PayReconcileDetailMapping payReconcileDetailMapping) {
		List<PayReconcileDetailMapping> payReconcileDetailList = Lists.newArrayList();
		//如果查询条件中的缴费渠道不为空
		if(StringUtils.isNotBlank(payReconcileDetailMapping.getAvenue())){
			if(EnumsUtils.stringEquals(payReconcileDetailMapping.getAvenue(), PayDefrayBillAvenueEnum.UNION_PAY)){
				payReconcileDetailList.addAll(payUnpReconcileDetailService.findUnpReconcileListForWebService(payReconcileDetailMapping));
			}

		}else{
			//如果查询条件中的缴费渠道为空，查询全部
			payReconcileDetailList.addAll(payUnpReconcileDetailService.findUnpReconcileListForWebService(payReconcileDetailMapping));
		}
		return payReconcileDetailList.size();
	}


}