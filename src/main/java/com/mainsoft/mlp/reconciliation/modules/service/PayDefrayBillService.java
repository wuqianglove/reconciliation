
package com.mainsoft.mlp.reconciliation.modules.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mainsoft.mlp.common.mapper.JsonMapper;
import com.mainsoft.mlp.reconciliation.common.utils.StringUtils;
import com.mainsoft.mlp.reconciliation.common.webservice.DPS_Passport;
import com.mainsoft.mlp.reconciliation.common.webservice.ServiceParam;
import com.mainsoft.mlp.reconciliation.common.webservice.osb.RouterClientCaller;
import com.mainsoft.mlp.reconciliation.modules.entity.PayPaymentBill;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpDealing;
import com.mainsoft.mlp.reconciliation.modules.enums.BusinessTypeEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PayDefrayBillAvenueEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PayPaymentBillBusinessTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mainsoft.mlp.common.utils.EnumsUtils;
import com.mainsoft.mlp.reconciliation.common.persistence.Page;
import com.mainsoft.mlp.reconciliation.common.service.CrudService;
import com.mainsoft.mlp.reconciliation.modules.dao.PayDefrayBillDao;
import com.mainsoft.mlp.reconciliation.modules.entity.PayDefrayBill;
import com.mainsoft.mlp.reconciliation.modules.enums.PaymentDefrayBillStatusEnum;



 /*
  * 支付单Service
  * @author ZhangSC
  * @version 2017-04-01
 * */

@Service
@Transactional(readOnly = true)
public class PayDefrayBillService extends CrudService<PayDefrayBillDao, PayDefrayBill> {
	

	@Autowired
	private PayPaymentBillService payPaymentBillService;
	@Autowired
	private PayPaymentBillDetailService payPaymentBillDetailService;
	@Autowired
	private PayUnpDealingService payUnpDealingService;
	@Autowired
	private PayDefrayPaymentService payDefrayPaymentService;

    @Autowired
    private RouterClientCaller routerClientCaller;

	public PayDefrayBill get(String id) {
		return super.get(id);
	}
	
	public List<PayDefrayBill> findList(PayDefrayBill payDefrayBill) {
		return super.findList(payDefrayBill);
	}
	
	public Page<PayDefrayBill> findPage(Page<PayDefrayBill> page, PayDefrayBill payDefrayBill) {
		return super.findPage(page, payDefrayBill);
	}
	
	public Page<PayDefrayBill> findConfiscatePage(Page<PayDefrayBill> page, PayDefrayBill payDefrayBill) {
		payDefrayBill.setPage(page);
		page.setList(dao.findConfiscateList(payDefrayBill));
		return page;
	}
	
	public Page<PayDefrayBill> findExamPage(Page<PayDefrayBill> page, PayDefrayBill payDefrayBill) {
		payDefrayBill.setPage(page);
		page.setList(dao.findExamList(payDefrayBill));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(PayDefrayBill payDefrayBill) {
		super.save(payDefrayBill);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayDefrayBill payDefrayBill) {
		super.delete(payDefrayBill);
	}


	public void update(PayDefrayBill payDefrayBill) {
		// TODO Auto-generated method stub
		payDefrayBill.setUpdateDate(new Date());
		dao.update(payDefrayBill);
		
	}

    /**
     * 判断支付单是否已经缴费（参数为缴费单号）
     * @param paymentBill
     * @return
     */

	public boolean isPayDefrayPaid(String paymentBill){
		PayDefrayBill payDefrayBill = getPayDefrayBillByPaymentBill(paymentBill);
		if(payDefrayBill != null && EnumsUtils.stringEquals(payDefrayBill.getStatus(), PaymentDefrayBillStatusEnum.ALREADY_PAID)){
			return true;
		}
		return false;
	}

    /**
     * 判断支付单是否已经缴费（参数为支付单号）
     * @param payDefrayBillId
     * @return
     */

	public boolean isPayDefrayPaidById(String payDefrayBillId){
		PayDefrayBill payDefrayBill = get(payDefrayBillId);
		if(payDefrayBill != null && EnumsUtils.stringEquals(payDefrayBill.getStatus(), PaymentDefrayBillStatusEnum.ALREADY_PAID)){
			return true;
		}
		return false;
	}
	


    /**
     * 根据缴费单号获取支付单
     * @param paymentBillNo
     * @return
     */

	public PayDefrayBill getPayDefrayBillByPaymentBill(String paymentBillNo){
		return dao.getPayDefrayBillByPaymentBill(paymentBillNo);
	}


	//根据PayDefrayBillList查询对应的支付单信息
	public List<PayDefrayBill> findListByPayDefrayBillList(PayDefrayBill payDefrayBill) {
		
		return dao.findListByPayDefrayBillList(payDefrayBill);
	}



    /**
     * 银联在线支付成功 支撑银联查询，更改对应的支付单相关
     * @param payDefrayBill
     * @param successTime
     * @param payUnpDealing
     * @param merId
     */
   /* @Transactional(readOnly = false)
    public void unionPaySuccessForQuery(PayDefrayBill payDefrayBill, Date successTime, PayUnpDealing payUnpDealing, String merId, String orderCode) throws Exception{
        paidSuccessForQuery(payDefrayBill, successTime, merId, PayDefrayBillAvenueEnum.UNION_PAY.getCode(), orderCode);
        payUnpDealing.setDefrayBill(payDefrayBill.getId());
        payUnpDealingService.save(payUnpDealing);
    }*/

    /**
     * 2019.03.27 guodongyu 暂时不在定时程序中提供更改支付单状态功能
     * 支付成功
     * 支付成功后同步内网缴费单信息
     * 支撑银联查询
     * @param payDefrayBill
     * @param successTime
     * @param merId
     * @param code
     * @param orderCode
     */
   /* @Transactional(readOnly = false)
    public String paidSuccessForQuery(PayDefrayBill payDefrayBill, Date successTime,String merId, String code, String orderCode) throws Exception {

        String businessType = BusinessTypeEnum.PORT_BUILD_FEE.getCode(); //默认港口建设费
        // for 循环根据缴费单编码获得缴费单信息 并设置缴费单状态为支付成功，设置支付时间
        //2017.10.19 guody 通过支付单编码 调用payDefrayPaymentService
        List<String> paymentIds = payDefrayPaymentService.findPaymentBillIdList(payDefrayBill.getId());
        //ArrayList<String> paymentIds=new ArrayList<String>(Arrays.asList(paymentBillNo.split(",")));
        List<PayPaymentBill> PaymentBillList = payPaymentBillService.findPaymentBillListByIds(paymentIds);
        for (PayPaymentBill payPaymentBill2 : PaymentBillList) {
            payPaymentBill2.setStatus(PaymentBillStatusEnum.ALREADY_PAID.getCode());  //缴费单状态设置为支付成功
            payPaymentBill2.setFinishDate(successTime);
            if(EnumsUtils.stringEquals(payPaymentBill2.getBusinessType(), PayPaymentBillBusinessTypeEnum.PORTCONSTRUCTIONFEE)){
                businessType = BusinessTypeEnum.PORT_BUILD_FEE.getCode();
            }
        }
        //PayDefrayBill payDefrayBill = get(defrayBillNo);
        payDefrayBill.setFinishTime(successTime);
        payDefrayBill.setStatus(PaymentDefrayBillStatusEnum.ALREADY_PAID.getCode());
        //TODO 设置枚举类
        payDefrayBill.setRefundFlag("01");
        //更新支付单信息
        savePayDefrayBill(payDefrayBill);
        //调用webservice去内网将提运单状态改为已收费
        Map<String, String> paramMap = Maps.newHashMap();
        //这里传入的缴费单号应该是更改逻辑后的  多个缴费单号字符串
        String paymentBillNo = StringUtils.join(paymentIds.toArray(), ",");
        paramMap.put("paymentBillNo", paymentBillNo);     //缴费单号
        paramMap.put("orderNo", payDefrayBill.getId());   //支付单号 ，用于内网确认缴费进行记录
        //User user = UserUtils.getUser();
        paramMap.put("createBy", payDefrayBill.getCreateBy().getId());
        paramMap.put("createName", payDefrayBill.getCreateBy().getName());
        paramMap.put("district", payDefrayBill.getDistrict());
        paramMap.put("businessType", businessType);
        paramMap.put("payDefrayBill", JsonMapper.toJsonString(payDefrayBill));
        paramMap.put("paymentBillList", JsonMapper.toJsonString(PaymentBillList));
        Map<String, Area> areaMapByCache = areaService.getAreaMapByCache();
        Area area = areaMapByCache.get(payDefrayBill.getDistrict());
        //TODO获取部署辖区编码，用于一级总线判断走哪个辖区的内网。
        String deployAreaId = null;
        if(area != null && StringUtils.isNotBlank(area.getRoutingAreaId())){
            deployAreaId = area.getRoutingAreaId();
        }
        //2017.10.19 guody 循环更新缴费单信息（使用for可能会出现问题  暂时先跑通业务）
        for (PayPaymentBill payPaymentBill : PaymentBillList) {
            payPaymentBillService.save(payPaymentBill);
        }
        routerClientCaller.callRouterServer(deployAreaId,"DPS", "Payment.updatePayStatusForPort", JsonMapper.toJsonString(getDpsServiceParam(JsonMapper.toJsonString(paramMap))));
        return payDefrayBill.getId();
    }*/

    /**
     * 得到调用外网部分的serviceParam
     * @param paramJson
     * @return
     */
    public ServiceParam getDpsServiceParam(String paramJson){
        DPS_Passport passport = new DPS_Passport("", "", new Date());
        return new ServiceParam(JsonMapper.toJsonString(passport), paramJson);
    }

	public List<PayDefrayBill> findListByOrderCodeList(PayDefrayBill payDefrayBill) {
    	return dao.findListByOrderCodeList(payDefrayBill);
	}
}
