/*
package com.mainsoft.mlp.reconciliation.modules.service;

import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mainsoft.mlp.common.utils.DateUtils;
import com.mainsoft.mlp.reconciliation.common.config.Global;
import com.mainsoft.mlp.reconciliation.modules.entity.PayImportFile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayReconcile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayReconcileDetail;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpConfigure;
import com.mainsoft.mlp.reconciliation.modules.enums.PayDefrayBillAvenueEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PayImportFileStatusEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PayImportFileTypeEnum;
import com.mainsoft.mlp.reconciliation.modules.unionpaySDK.SDKConstants;
import com.mainsoft.mlp.reconciliation.modules.utils.AcpUtils;
import com.mainsoft.mlp.reconciliation.modules.utils.PayUtils;
import com.mainsoft.mlp.reconciliation.modules.utils.UnionPayUtils;

*/
/**
 * 银行对账文件解析
 * @author guody
 * @date 2017年9月12日
 *//*

@Service
@Transactional(readOnly = true)
public class BankReconciliationService {
	
	static class CachedThreadPool{
		private CachedThreadPool(){}
		private static final ExecutorService exec = Executors.newCachedThreadPool(); 
		public static ExecutorService getExec(){
			return exec;
		}
	}	
	@Autowired
	private PayUnpConfigureService payUnpConfigureService;
	
	@Autowired
	private CfgDistrictService cfgDistrictService;

	@Autowired
	private PayReconcileService payReconcileService;
	
	@Autowired
	private PayReconcileDetailService payReconcileDetailService;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	
	*/
/**
	 * 银联下载对账文件入口  使用定时任务运行
	 * @author guody
	 * @version 2017-09-12
	 *//*

	@Transactional(readOnly = false)
	public void unpReconcileEntrance(){
		List<PayUnpConfigure> payUnpConfigureList = payUnpConfigureService.findAllList(new PayUnpConfigure());  //获取所有的unionPay商户号
		
		Map<String, PayUnpConfigure> payUnpConfigureMap = Maps.newHashMap();  //将unpConfigure放入map中 防止同一个商户号下载对账文件两次
		for (PayUnpConfigure payUnpConfigure : payUnpConfigureList) {
			payUnpConfigureMap.put(payUnpConfigure.getMerchantCode(), payUnpConfigure);
		}
		
		Date settleDate = DateUtils.addDays(new Date(), -2);   //要下载哪一天的对账文件
		//发送请求，下载对账文件并解析入库
		for (Map.Entry<String, PayUnpConfigure> payUnpConfigure : payUnpConfigureMap.entrySet()) {
			try {
				reconciliation(payUnpConfigure.getValue(), settleDate);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("unionPay商户"+payUnpConfigure.getKey()+"获取对账文件失败："+e.getMessage());
			}
		}
	}
	
	*/
/**
	 * 银行对账逻辑入口，使用定时任务运行
	 * 先进行内外网银行对账信息的同步，再启动内网的对账逻辑
	 * 中国银联与银联的对账逻辑入口可以走这里（东方支付的对账有稍微不同）
	 * @author guody
	 * @version 2017-09-13
	 *//*

	public void syncAndReconcile(){
		
		//同步对账信息并执行对账逻辑（银联）
		unionSyncAndReconcile();
		//同步对账信息并执行对账逻辑（中国银联）
		chinaSyncAndReconcile();
		//同步对账信息并执行对账逻辑（东方支付）
		//ecsSyncAndReconcile();
		//关闭线程池，线程池默认自动关闭时间是60s
		//CachedThreadPool.getExec().shutdownNow();
		
	}
	
	*/
/**
	 * 同步银联对账信息与执行银联对账逻辑的方法
	 * @author guody
	 * @date 2017年9月15日
	 *//*

	private void unionSyncAndReconcile() {
		//获取所有的unionPay配置对象集合
		List<PayUnpConfigure> payUnpConfigureList = payUnpConfigureService.findAllList(new PayUnpConfigure());  		
		//将unpConfigure放入map中 key为商户号，velue为所属辖区
		Map<String, String> payUnpConfigureMap = Maps.newHashMap(); 
		for (PayUnpConfigure payUnpConfigure : payUnpConfigureList) {
			payUnpConfigureMap.put(payUnpConfigure.getMerchantCode(), payUnpConfigure.getDistrict());
		}		
			createThread(payUnpConfigureMap);		
	}
	
	*/
/**
	 * 同步东方支付对账信息与执行银联对账逻辑的方法
	 * @author guody
	 * @date 2017年9月15日
	 * 最后确定不考虑东方电子支付对账业务（2017.09.28）
	 *//*

	*/
/*private void ecsSyncAndReconcile() {
		// TODO 获得东方支付配置表中的信息集合,表中的收款机构编码是否与银联中的商务号意义一致
		List<PayEcsConfigure> payEcsConfigureList = payEcsConfigureService.findConfigureList(new PayEcsConfigure());
		//将unpConfigure放入map中 key为商户号，velue为所属辖区
		*//*
*/
/**
		 * 东方支付线程中查询表中对账信息的逻辑和另外两个银行的逻辑不同。
		 * 可能要修改线程池中run()方法中的业务逻辑
		 *//*
*/
/*
		Map<String, String> payEcsConfigureMap = Maps.newHashMap(); 
		for (PayEcsConfigure payEcsConfigure : payEcsConfigureList) {
			payEcsConfigureMap.put(payEcsConfigure.getPayeeCode(), payEcsConfigure.getDistrict());
		}		
		createThread(payEcsConfigureMap);
		
		
	}*//*


	
	*/
/**
	 * 创建分发线程（使用线程池）
	 * @author guody
	 * @date 2017年9月15日
	 * 使用线程池中的submit方法创建线程，可以通过Future返回值try到异常
	 * 创建分发线程的方法被调用了三次，同时线程池也被new了三次，能不能使用单例模式进行优化
	 *//*

	private void createThread(Map<String, String> payConfigureMap){
		//使用线程池加载线程
		//ExecutorService exec = Executors.newCachedThreadPool();
		//尝试使用饿汉式单例
		ExecutorService exec = CachedThreadPool.getExec();
		//创建Future集合存放执行任务结果
		List<Future<?>> resultList = new ArrayList<Future<?>>();
		//for循环map
		for (Map.Entry<String, String> payConfigure : payConfigureMap.entrySet()) {
			Future<?> future = exec.submit(new SyncRunnable(payConfigure.getKey(), payConfigure.getValue()));
			resultList.add(future);  
		}
		//exec.shutdown(); 
		for (Future<?> fs : resultList) {  
            try {  
            	fs.get();                
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            } catch (ExecutionException e) {  
            	//exec.shutdownNow();  
                e.printStackTrace();
                logger.error(e.getMessage());
                continue;  
            }  
        }  
		//关闭线程池
		//exec.shutdownNow(); 
	}
	
	*/
/**
	 * 银联在线对账
	 * @param payUnpConfigure 
	 * @param payDate 交易日期
	 * @throws IOException 
	 * @author guody
	 * @version 2017-09-12
	 *//*

	@Transactional(readOnly = false)
	public void reconciliation(PayUnpConfigure payUnpConfigure, Date payDate) throws IOException{

		
        String url = null; //对账地址
		String settleDate = null; //交易日期
		if(Global.getConfig("mlp.online.payment.workMode").equals(Global.PAYMENT_DEV)){
			//如果是开发模式 使用固定的商户号和日期
			payUnpConfigure.setMerchantCode("700000000000001");
			payUnpConfigure.setPrivateKey("/payment/unionpay/B2B/acp_test_sign.pfx");
			payUnpConfigure.setKeyPwd("000000");
			payUnpConfigure.setPublicKey("/payment/unionpay/B2B/acp_test_verify_sign.cer");
			payUnpConfigure.setSplitType(0);                 
			
			settleDate =  "0119";  //测试环境固定使用0119
			url = "https://101.231.204.80:9080/";  //开发模式固定使用https://101.231.204.80:9080/作为下载对账地址
		}
		if(Global.getConfig("mlp.online.payment.workMode").equals(Global.PAYMENT_PRODUCT)){
			//如果是生产模式 使用正式日期
			settleDate = DateUtils.formatDate(payDate, "yyyyMMdd");
			//2019.01.22 guodongyu 固定请求对账地址
			//
			//url = cfgDistrictService.getCfgInfoByCache(CfgDistrictService.UNIONPAY_RECONCILIATION_URL);//获取unionPay请求对账文件的地址
			url ="";
		}
		
		String merId = payUnpConfigure.getMerchantCode();   //商户号

		Map<String, String> data = new HashMap<String, String>();
		
		//获取证书，并转为对象
		KeyStore keyStore = UnionPayUtils.getKeyInfo(servletContext.getResource("/WEB-INF"+payUnpConfigure.getPrivateKey()).getPath(), payUnpConfigure.getKeyPwd()); 

		*/
/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***//*

		data.put(SDKConstants.param_version, UnionPayUtils.version); // 版本号 全渠道默认值
		data.put(SDKConstants.param_encoding, UnionPayUtils.encoding_UTF8); // 字符集编码 可以使用UTF-8,GBK两种方式
		data.put(SDKConstants.param_certId, UnionPayUtils.getSignCertId(keyStore)); //证书id
		data.put("signMethod", "01"); // 签名方法 目前只支持01-RSA方式证书加密
		data.put(SDKConstants.param_txnType, "76"); // 交易类型 76-对账文件下载
		data.put(SDKConstants.param_txnSubType, "01"); // 交易子类型 01-对账文件下载
		data.put(SDKConstants.param_bizType, "000000"); // 业务类型，固定

		*/
/*** 商户接入参数 ***//*

		data.put(SDKConstants.param_accessType, "0"); // 接入类型，商户接入填0，不需修改
		data.put(SDKConstants.param_merId, merId); // 商户代码，请替换正式商户号测试，如使用的是自助化平台注册的777开头的商户号，该商户号没有权限测文件下载接口的，请使用测试参数里写的文件下载的商户号和日期测。如需777商户号的真实交易的对账文件，请使用自助化平台下载文件。
		data.put(SDKConstants.param_settleDate, settleDate); // 清算日期，如果使用正式商户号测试则要修改成自己想要获取对账文件的日期，
		data.put(SDKConstants.param_txnTime, DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		data.put(SDKConstants.param_fileType, "00"); // 文件类型，一般商户填写00即可   00代表zip文件
		
		//获取签名后的map
		Map<String, String> reqData = UnionPayUtils.sign(PayUtils.filterBlank(data), UnionPayUtils.encoding_UTF8, keyStore, payUnpConfigure.getKeyPwd());  
		
		AcpUtils acpUtils = new AcpUtils();
		Map<String, String> rspData = acpUtils.post(reqData, url, UnionPayUtils.encoding_UTF8);
		
		// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
		if (!rspData.isEmpty()) {
			//验证签名
			if (UnionPayUtils.validate(rspData, UnionPayUtils.encoding_UTF8, servletContext.getResource("/WEB-INF"+payUnpConfigure.getPublicKey()).getPath())) {  
				String respCode = rspData.get("respCode");
				if ("00".equals(respCode)) {
					// 交易成功，解析返回报文中的fileContent
				    //从zip文件流中解析出对账文件内容
				    Map<String, String> resultMap = UnionPayUtils.parseZMZipFile(acpUtils.deCodeFileContent(rspData, UnionPayUtils.encoding_UTF8));
				   
				   PayImportFile payImportFile = new PayImportFile();
				    //设置对账文件类型
					payImportFile.setType(PayImportFileTypeEnum.UNION_PAY.getCode());
					payImportFile.setName(resultMap.get("fileName"));
					payImportFile.setContent(resultMap.get("content"));
					payImportFile.setStatus(PayImportFileStatusEnum.NOT_HANDLE.getCode());  //默认未处理状态
					Date today = new Date();
					payImportFile.setCreateTime(today);  //设置生成时间
					payImportFile.setImportTime(today);  //设置入库时间
					payImportFile.setSettleDate(payDate);//设置账单的清算日期
										
					//设置unionPay对账信息的参数
				    PayReconcile payReconcile = new PayReconcile();
				    //设置渠道来源 通过枚举类
				    payReconcile.setChannel(PayDefrayBillAvenueEnum.UNION_PAY.getCode());
				    payReconcile.setMerchant(merId);  
				    payReconcile.setSplitType(payUnpConfigure.getSplitType());
				    payReconcile.setSuccess(Integer.parseInt(Global.NO));
				    //银联在线对账实体中交易日期就是清算日期，所以在新建（银行在线对账）表中不必要添加清算日期字段
				    payReconcile.setTransDate(payDate);
				    payReconcile.setPayReconcileDetailList(parseUnpReconcileFile(resultMap.get("content")));   //解析银联在线文件详情
					
				    //保存银行在线对账
				    payReconcileService.savePayReconcile(payImportFile, payReconcile);				    				    				    				    
				    logger.info("Unionpay:商户号："+payUnpConfigure.getMerchantCode()+"|辖区编码："+payUnpConfigure.getDistrict()+"|商户名称："+payUnpConfigure.getMerchantName()+"|清算日期："+settleDate+"|下载对账文件状态：成功");
				} else {
					// 其他应答码为失败请排查原因
					logger.error("Unionpay:商户号："+payUnpConfigure.getMerchantCode()+"|辖区编码："+payUnpConfigure.getDistrict()+"|商户名称："+payUnpConfigure.getMerchantName()+"|清算日期："+settleDate+"|下载对账文件状态：失败|应答码："+respCode);
				}
			} else {
				logger.error("Unionpay:商户号"+payUnpConfigure.getMerchantCode()+"下载对账单验证签名失败");
			}
		} else {
			// 未返回正确的http状态
			logger.error("Unionpay:商户号"+payUnpConfigure.getMerchantCode()+"未获取到返回对账单或返回http状态码非200");
		}
	}
		
	*/
/**
	 * 解析银联在线对账文件(5.0.0版本)用于银行对账详情信息保存
	 * @param content
	 * @return
	 *//*

	public List<PayReconcileDetail> parseUnpReconcileFile(String content){
	    List<Map<Integer, String>> reconciliationMapList = UnionPayUtils.parseReconciliationFile(content);
	    List<PayReconcileDetail> payUnpReconcileDetails = Lists.newArrayList();
	    
		//0  交易代码  1  代理机构标识码  2  发送机构标识码  3  系统跟踪号 4  交易传输时间MMDDhhmmss   5  帐号
		//6  交易金额  7 商户类别  8 终端类型   9  查询流水号  10  支付方式（旧） 11  商户订单号
		//12  支付卡类型  13  原始交易的系统跟踪号  14  原始交易日期时间  15  商户手续费  16  结算金额
		//17  支付方式 18  集团商户代码   19  交易类型  20  交易子类  21  业务类型  22  帐号类型  23  账单类型 
		//24  账单号码  25  交互方式  26  原交易查询流水号 27  商户代码  28  分账入账方式  29  二级商户代码
		//30  二级商户简称  31  二级商户分账入账金额  32  清算净额  33  终端号  34  商户自定义域 
		//35  优惠金额  36  发票金额  37  分期付款附加手续费  38  分期付款期数  39  交易介质 
		//40  原始交易订单号  41  保留使用
		//循环对账单解析后的map，获取其中的值，转为reconciliation对象
		for (Map<Integer, String> reconciliationMap : reconciliationMapList) {
			PayReconcileDetail payUnpReconcileDetail = new PayReconcileDetail();
			payUnpReconcileDetail.setPayTime(reconciliationMap.get(14));    //支付时间
			payUnpReconcileDetail.setOrderCode(reconciliationMap.get(11));   //订单编号
			if(Global.getConfig("mlp.online.payment.workMode").equals(Global.PAYMENT_DEV)){
				//如果是开发模式 使用固定的商户号和日期
				payUnpReconcileDetail.setMerchant("777290058136993");     //商户号
			}else{
				//正式环境，使用正确的商户号
				payUnpReconcileDetail.setMerchant(reconciliationMap.get(27));     //商户号
			}
			payUnpReconcileDetail.setTransType(reconciliationMap.get(19));    //交易类型
			payUnpReconcileDetail.setTransAmount(reconciliationMap.get(6));   //支付金额
			payUnpReconcileDetail.setCpSeqId(reconciliationMap.get(9));    //流水号
			payUnpReconcileDetail.setPriv1(reconciliationMap.get(34));     //缴费单号
			payUnpReconcileDetail.setSuccess(Integer.parseInt(Global.NO));  //默认未对账
			payUnpReconcileDetails.add(payUnpReconcileDetail);         
		}
	    
		return payUnpReconcileDetails;
	}

}
*/
