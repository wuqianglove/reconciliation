package com.mainsoft.mlp.reconciliation.modules.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mainsoft.mlp.reconciliation.common.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mainsoft.mlp.common.mapper.JsonMapper;
import com.mainsoft.mlp.common.utils.DateUtils;
import com.mainsoft.mlp.reconciliation.common.config.Global;
import com.mainsoft.mlp.reconciliation.common.utils.SendMailUtil;
import com.mainsoft.mlp.reconciliation.common.utils.StringUtils;
import com.mainsoft.mlp.reconciliation.modules.entity.PayImportFile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpConfigure;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpReconcile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpReconcileDetail;
import com.mainsoft.mlp.reconciliation.modules.enums.PayImportFileStatusEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PayImportFileTypeEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PaymentTypeForImportFileEnum;
import com.mainsoft.mlp.reconciliation.modules.unionpaySDK.SDKConstants;
import com.mainsoft.mlp.reconciliation.modules.utils.AcpUtils;
import com.mainsoft.mlp.reconciliation.modules.utils.PayUtils;
import com.mainsoft.mlp.reconciliation.modules.utils.UnionPayUtils;

/**
 * 银联支付service
 * @author ZhangSC
 * @version 2017-05-15
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@PropertySource("classpath:cron.props")
public class UnionPayService {

	@Autowired
	private PayImportFileService payImportFileService;

	@Autowired
	private PayUnpConfigureService payUnpConfigureService;

    UnionPayUtils unionPayUtils = new UnionPayUtils();
	
	//protected Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${payment.workMode}") private String paymentWorkMode;
    @Value("${payment.fileUrl}") private String paymentFileUrl;
    @Value("${payment.downloadUrl}") private String paymentDownloadUrl;
    @Value("${mlp.pay.signMethod}") private String signMethod;
    @Value("${mlp.pay.param_txnType}") private String paramTxnType;
    @Value("${mlp.pay.param_txnSubType}") private String paramTxnSubType;
    @Value("${mlp.pay.param_bizType}") private String paramBizType;
    @Value("${mlp.pay.param_accessType}") private String paramAccessType;
    @Value("${mlp.pay.param_fileType}") private String paramFileType;
    @Value("${mlp.pay.zip_url}") private String fileZipUrl;

	
	/**
	 * 获取银联支付通用的参数
	 * @param payUnpConfigure
	 * @param reqReserved
	 * @param revenue
	 * @return
	 */
	public Map<String, String> getUnionPayCommonPayParam(PayUnpConfigure payUnpConfigure, String reqReserved, BigDecimal revenue, String orderId){
		Map<String, String> requestData = Maps.newHashMap();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		requestData.put("version", UnionPayUtils.version);   			      //版本号，全渠道默认值
		requestData.put("encoding", UnionPayUtils.encoding_UTF8); 	      //字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", "01");            			  //签名方法，只支持 01：RSA方式证书加密
		requestData.put("txnType", "01");               			  //交易类型 ，01：消费
		requestData.put("txnSubType", "01");            			  //交易子类型， 01：自助消费
		requestData.put("channelType", "07");           			  //渠道类型 固定07
		
		/***商户接入参数***/
		requestData.put("merId", payUnpConfigure.getMerchantCode());	  //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		
		String txnAmt = PayUtils.formatRevenueToFen(revenue);
		requestData.put("accessType", "0");             			  //接入类型，0：直连商户 
		requestData.put("orderId", orderId);             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则		
		requestData.put("txnTime", UnionPayUtils.getCurrentTime());    //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		requestData.put("currencyCode", "156");         			  //交易币种（境内商户一般是156 人民币）		
		requestData.put("txnAmt", txnAmt);  //交易金额，单位分，不要带小数点
		requestData.put("reqReserved", reqReserved);     //请求方保留域  此处使用缴费单号
		
		//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
		//2018.03.08 guodongyu 替换前后天顺序frontUrl backUrl
		requestData.put("frontUrl", Global.getConfig("mlp.unionpay.pay.front.rspUrl"));
		
		//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
		//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		requestData.put("backUrl", Global.getConfig("mlp.unionpay.pay.back.rspUrl"));
		
		//实现网银前置的方法：
		//上送issInsCode字段，该字段的值参考《平台接入接口规范-第5部分-附录》（全渠道平台银行名称-简码对照表）2）联系银联业务运营部门开通商户号的网银前置权限
		//requestData.put("issInsCode", "ABC");                 //发卡机构代码
		
		//如果开启分账，并且分账代码不为空
		if(payUnpConfigure.getSplitType().toString().equals(Global.YES) && StringUtils.isBlank(payUnpConfigure.getSplitCode())){
			requestData.put("accSplitData", getSplitData(payUnpConfigure, txnAmt));
		}
		
		return requestData;
	}

	/**
	 * 获取分账数据
	 * @param payUnpConfigure 银联配置
	 * @param splitAmt 分账金额
	 * @return
	 */
	public String getSplitData(PayUnpConfigure payUnpConfigure, String splitAmt){
		String splitData = "";
		//如果开启分账，并且分账代码不为空
		if(payUnpConfigure.getSplitType().toString().equals(Global.YES) && StringUtils.isBlank(payUnpConfigure.getSplitCode())){
			ObjectNode rootNode = JsonMapper.getInstance().createObjectNode();
			rootNode.put("accSplitType", "1");  //分账类型  1 表示按照商户列表分账，联机带入分账入账金额     2 表示按照分账规则 ID 分账，联机带分账决定要素
			
			List<Map<String, String>> accSplitMchts = Lists.newArrayList();
			Map<String, String> splitMcht = Maps.newHashMap();
			splitMcht.put("accSplitMerId", payUnpConfigure.getSplitCode());  //分账二级商户代码
			splitMcht.put("accSplitAmt", splitAmt);							//分账金额
			accSplitMchts.add(splitMcht);
			
			rootNode.put("accSplitMchts", JsonMapper.toJsonString(accSplitMchts));//分账对象组
			
			splitData = rootNode.toString();
		}

		return splitData;
	}
	
	/**
	 * 银联对账入口  使用定时任务运行
	 * @author ZhangSC
	 * @version 2017-05-15
	 */
	@Transactional(readOnly = false)
    @Scheduled(cron="${jobs.schedule.download}")
	public void unpReconcileEntrance(){
	    log.info("============下载对账文件====================");
		//guody  2018.04.28     这里的查询银行配置信息list的方法需要修改  只限制为业务类型为01（港建费）的信息	
		List<PayUnpConfigure> payUnpConfigureList = payUnpConfigureService.findAllListForPort(new PayUnpConfigure());  //获取所有的unionPay商户号
		Map<String, PayUnpConfigure> payUnpConfigureMap = Maps.newHashMap();  //将unpConfigure放入map中 防止同一个商户号下载对账文件两次
		for (PayUnpConfigure payUnpConfigure : payUnpConfigureList) {
			payUnpConfigureMap.put(payUnpConfigure.getMerchantCode(), payUnpConfigure);
		}		
		Date settleDate = DateUtils.addDays(new Date(), -2);   //要下载哪一天的对账文件
		//发送请求，下载对账文件并解析入库
		for (Map.Entry<String, PayUnpConfigure> payUnpConfigure : payUnpConfigureMap.entrySet()) {
			try {
				reconciliation(payUnpConfigure.getValue(), settleDate);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("unionPay商户"+payUnpConfigure.getKey()+"获取对账文件失败："+e);
			}
		}
	}
	
	/**
	 * 银联在线对账
	 * @param payUnpConfigure 
	 * @param payDate 交易日期
	 * @throws IOException 
	 * @author ZhangSC
	 * @version 2017-05-02
	 */
	@Transactional(readOnly = false)
	public int reconciliation(PayUnpConfigure payUnpConfigure, Date payDate) throws Exception{
		//出错的总条数
		int sumManual = 0;
		String fileURL = paymentFileUrl;
        String url = paymentDownloadUrl; //对账地址
		String settleDate = null; //交易日期
		if(paymentWorkMode.equals(Global.PAYMENT_DEV)){
			payUnpConfigure.setMerchantCode("700000000000001");
			payUnpConfigure.setPrivateKey("/payment/unionpay/B2B/acp_test_sign.pfx");
			payUnpConfigure.setKeyPwd("000000");
			payUnpConfigure.setPublicKey("/payment/unionpay/B2B/acp_test_verify_sign.cer");
			payUnpConfigure.setSplitType(0);
			settleDate =  "0119";  //测试环境固定使用0119
			url = "https://101.231.204.80:9080/";  //开发模式固定使用https://101.231.204.80:9080/作为下载对账地址
            /*InputStream inputStream =this.getClass().getResourceAsStream("/certificate/payment/unionpay/B2B/acp_test_sign.pfx");
            for (String readLine : IOUtils.readLines(inputStream)) {
                System.out.println(readLine);
            }*/
        }
		if(paymentWorkMode.equals(Global.PAYMENT_PRODUCT)){
			//如果是生产模式 使用正式日期  2018.04.04 guodongyu 修改对账文件请求报文中的清算日期问题（yyyyMMdd 改为MMdd）
			settleDate = DateUtils.formatDate(payDate, "MMdd");
			url = "";
		}
		String merId = payUnpConfigure.getMerchantCode();   //商户号
		Map<String, String> data = new HashMap<String, String>();
		//获取证书，并转为对象
		KeyStore keyStore = unionPayUtils.getKeyInfo(fileURL+payUnpConfigure.getPrivateKey(), payUnpConfigure.getKeyPwd());
		//2018.4.4 zhanghz修改参数暴露在外的问题  在mlp_online.propertise 文件中加入相应的属性  ---start
		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		data.put(SDKConstants.param_version, UnionPayUtils.version); // 版本号 全渠道默认值
		data.put(SDKConstants.param_encoding, UnionPayUtils.encoding_UTF8); // 字符集编码 可以使用UTF-8,GBK两种方式
		data.put(SDKConstants.param_certId, UnionPayUtils.getSignCertId(keyStore)); //证书id
		data.put("signMethod", signMethod); // 签名方法 目前只支持01-RSA方式证书加密
		data.put(SDKConstants.param_txnType, paramTxnType); // 交易类型 76-对账文件下载
		data.put(SDKConstants.param_txnSubType, paramTxnSubType); // 交易子类型 01-对账文件下载
		data.put(SDKConstants.param_bizType, paramBizType); // 业务类型，固定
		/*** 商户接入参数 ***/
		data.put(SDKConstants.param_accessType, paramAccessType); // 接入类型，商户接入填0，不需修改
		data.put(SDKConstants.param_merId, merId); // 商户代码，请替换正式商户号测试，如使用的是自助化平台注册的777开头的商户号，该商户号没有权限测文件下载接口的，请使用测试参数里写的文件下载的商户号和日期测。如需777商户号的真实交易的对账文件，请使用自助化平台下载文件。
		data.put(SDKConstants.param_settleDate, settleDate); // 清算日期，如果使用正式商户号测试则要修改成自己想要获取对账文件的日期，
		data.put(SDKConstants.param_txnTime, DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		data.put(SDKConstants.param_fileType, paramFileType); // 文件类型，一般商户填写00即可   00代表zip文件
		//2018.4.4 zhanghz修改参数暴露在外的问题   ---end
		//获取签名后的map
		Map<String, String> reqData = UnionPayUtils.sign(PayUtils.filterBlank(data), UnionPayUtils.encoding_UTF8, keyStore, payUnpConfigure.getKeyPwd());
		AcpUtils acpUtils = new AcpUtils();
		Map<String, String> rspData = acpUtils.post(reqData, url, UnionPayUtils.encoding_UTF8);
		
		// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
		if (!rspData.isEmpty()) {
			//验证签名   
			if (unionPayUtils.validate(rspData, UnionPayUtils.encoding_UTF8, fileURL+payUnpConfigure.getPublicKey())) {
				String respCode = rspData.get("respCode");
				if ("00".equals(respCode)) {
					// 交易成功，解析返回报文中的fileContent
					//start ---2018.4.4 zhanghz修改自拍对账文件保存指定位置  位置在mlp_online.propertise中的  利用byte流的方式保存zip文件 不解压  文件名称格式为 商户号+日期（对账日期）
					byte[]  zip_Byte=acpUtils.deCodeFileContent(rspData, UnionPayUtils.encoding_UTF8);
                    String filename =merId+"_"+DateUtils.formatDate(payDate, "yyyyMMdd")+".zip";
                    //保存文件
                    if(!FileUtils.savaFile(fileZipUrl, filename, zip_Byte)){
                    	sumManual++;
                        String message =DateUtils.getDateTime()+"在获取商户号为"+merId+"的"+settleDate+"的对账文件时保存"+filename+"文件到本地失败！";
                    	//SendMailUtil.sendCommonMail(Global.getConfig("htmlmail.toAddress"),Global.getConfig("htmlmail.title"), message);
						log.info("商户号："+merId+"对账zip文件保存失败！！！");
                    }
				    //end ---2018.4.4 zhanghz
                    //从zip文件流中解析出对账文件内容
				    Map<String, String> resultMap = UnionPayUtils.parseZMZipFile(zip_Byte);
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
					/**
					 * 2018.06.11 guodongyu
					 * 设置缴费类型字段  支撑港建费与规费银联对账分支
					 */
					payImportFile.setPaymentType(PaymentTypeForImportFileEnum.PORTPAYMENT.getCode());  //设置缴费类型（01：港建费，02：规费）
					/**
					 * TODO 在银行对账表（内网）中添加一个清算时间字段
					 */
					//设置unionPay对账信息的参数
				    PayUnpReconcile payUnpReconcile = new PayUnpReconcile();
				    payUnpReconcile.setMerchant(merId);  
				    payUnpReconcile.setSplitType(payUnpConfigure.getSplitType());
				    payUnpReconcile.setSuccess(Integer.parseInt(Global.NO));
				    //银联在线对账实体中交易日期就是清算日期，所以在新建（银行在线对账）表中不必要添加清算日期字段
				    payUnpReconcile.setTransDate(payDate);
				    payUnpReconcile.setPayUnpReconcileDetailList(parseUnpReconcileFile(resultMap.get("content")));   //解析银联在线文件详情
					
				    //保存银联在线对账文件
					payImportFileService.saveUNPPayImportFile(payImportFile, payUnpReconcile);

					log.info("Unionpay:商户号："+payUnpConfigure.getMerchantCode()+"|辖区编码："+payUnpConfigure.getDistrict()+"|商户名称："+payUnpConfigure.getMerchantName()+"|清算日期："+settleDate+"|下载对账文件状态：成功");
				} else {
					 sumManual++;
					//2018.04.12 zhanghz 增加发送短信
					 String message ="Unionpay:商户号："+payUnpConfigure.getMerchantCode()+"|辖区编码："+payUnpConfigure.getDistrict()+"|商户名称："+payUnpConfigure.getMerchantName()+"|清算日期："+settleDate+"|下载对账文件状态：失败|应答码："+respCode;
                 	 //SendMailUtil.sendCommonMail(Global.getConfig("htmlmail.toAddress"),Global.getConfig("htmlmail.title"), message);
					// 其他应答码为失败请排查原因
					log.info("Unionpay:商户号："+payUnpConfigure.getMerchantCode()+"|辖区编码："+payUnpConfigure.getDistrict()+"|商户名称："+payUnpConfigure.getMerchantName()+"|清算日期："+settleDate+"|下载对账文件状态：失败|应答码："+respCode);
				}
			} else {
				 sumManual++;
				 //2018.04.12 zhanghz 增加发送短信
				 String message ="Unionpay:商户号"+payUnpConfigure.getMerchantCode()+"下载对账单验证签名失败";
            	 //SendMailUtil.sendCommonMail(Global.getConfig("htmlmail.toAddress"),Global.getConfig("htmlmail.title"), message);
				log.info("Unionpay:商户号"+payUnpConfigure.getMerchantCode()+"下载对账单验证签名失败");
			}
		} else {
			 sumManual++;
			 //2018.04.12 zhanghz 增加发送短信
			 String message ="Unionpay:商户号"+payUnpConfigure.getMerchantCode()+"未获取到返回对账单或返回http状态码非200";
       	     //SendMailUtil.sendCommonMail(Global.getConfig("htmlmail.toAddress"),Global.getConfig("htmlmail.title"), message);
			 // 未返回正确的http状态
			log.info("Unionpay:商户号"+payUnpConfigure.getMerchantCode()+"未获取到返回对账单或返回http状态码非200");
		}
		return sumManual;
	}
	
	/**
	 * 解析银联在线对账文件(5.0.0版本)
	 * @param content
	 * @return
	 */
	public List<PayUnpReconcileDetail> parseUnpReconcileFile(String content){
	    List<Map<Integer, String>> reconciliationMapList = UnionPayUtils.parseReconciliationFile(content);
	    List<PayUnpReconcileDetail> payUnpReconcileDetails = Lists.newArrayList();
	    
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
			PayUnpReconcileDetail payUnpReconcileDetail = new PayUnpReconcileDetail();
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
			payUnpReconcileDetail.setPriv1(reconciliationMap.get(34).trim());   //支付单  并添加去空格的操作
			payUnpReconcileDetail.setSuccess(Integer.parseInt(Global.NO));  //默认未对账
			payUnpReconcileDetails.add(payUnpReconcileDetail);         
		}
	    
		return payUnpReconcileDetails;
	}
	
	/**
	 * 外部系统银联在线对账逻辑
	 * @author guodongyu
	 * @version 2018-04-28
	 */
	@Transactional(readOnly = false)
	public void unpReconcileEntranceForExternal(){
		//获取银联配置信息list时应该限制业务类型为非01（港建费）
		List<PayUnpConfigure> payUnpConfigureList = payUnpConfigureService.findAllListForExternal(new PayUnpConfigure());  //获取所有的unionPay商户号
		Map<String, PayUnpConfigure> payUnpConfigureMap = Maps.newHashMap();  //将unpConfigure放入map中 防止同一个商户号下载对账文件两次
		for (PayUnpConfigure payUnpConfigure : payUnpConfigureList) {
			payUnpConfigureMap.put(payUnpConfigure.getMerchantCode(), payUnpConfigure);
		}		
		Date settleDate = DateUtils.addDays(new Date(), -2);   //要下载哪一天的对账文件
		//发送请求，下载对账文件并解析入库
		for (Map.Entry<String, PayUnpConfigure> payUnpConfigure : payUnpConfigureMap.entrySet()) {
			try {
				reconciliationForExternal(payUnpConfigure.getValue(), settleDate);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("unionPay商户"+payUnpConfigure.getKey()+"获取对账文件失败："+e);
			}
		}
	}

	/**
	 * 银联在线对账
	 * @param payUnpConfigure 
	 * @param payDate 交易日期
	 * @throws IOException 
	 * @author guodongyu
	 * @version 2018-04-28
	 */
	@Transactional(readOnly = false)
	public int reconciliationForExternal(PayUnpConfigure payUnpConfigure, Date payDate) throws Exception{
		//出错的总条数
		int sumManual = 0;				
		String fileURL = Global.getConfig("mlp.pay.file.url");		
        String url = null; //对账地址
		String settleDate = null; //交易日期
		if(paymentWorkMode.equals(Global.PAYMENT_DEV)){
			payUnpConfigure.setMerchantCode("700000000000001");
			payUnpConfigure.setPrivateKey("/payment/unionpay/B2B/acp_test_sign.pfx");
			payUnpConfigure.setKeyPwd("000000");
			payUnpConfigure.setPublicKey("/payment/unionpay/B2B/acp_test_verify_sign.cer");
			payUnpConfigure.setSplitType(0);                 			
			settleDate =  "0119";  //测试环境固定使用0119
			url = "https://101.231.204.80:9080/";  //开发模式固定使用https://101.231.204.80:9080/作为下载对账地址
			}
		if(paymentWorkMode.equals(Global.PAYMENT_PRODUCT)){
			//如果是生产模式 使用正式日期  2018.04.04 guodongyu 修改对账文件请求报文中的清算日期问题（yyyyMMdd 改为MMdd）
			settleDate = DateUtils.formatDate(payDate, "MMdd");			
			//2018.03.22 guodongyu  获取银联对账文件下载地址
			//url = cfgDistrictService.getCfgInfoByCacheForExternal(CfgDistrictService.UNIONPAY_RECONCILIATION_URL, payUnpConfigure.getDistrict());
			url = "";
		}
		
		String merId = payUnpConfigure.getMerchantCode();   //商户号
		Map<String, String> data = new HashMap<String, String>();		
		//获取证书，并转为对象
		KeyStore keyStore = unionPayUtils.getKeyInfo(fileURL+payUnpConfigure.getPrivateKey(), payUnpConfigure.getKeyPwd());

		//2018.4.4 zhanghz修改参数暴露在外的问题  在mlp_online.propertise 文件中加入相应的属性  ---start
		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		data.put(SDKConstants.param_version, UnionPayUtils.version); // 版本号 全渠道默认值
		data.put(SDKConstants.param_encoding, UnionPayUtils.encoding_UTF8); // 字符集编码 可以使用UTF-8,GBK两种方式
		data.put(SDKConstants.param_certId, UnionPayUtils.getSignCertId(keyStore)); //证书id
		data.put("signMethod", Global.getConfig("mlp.pay.signMethod").toString()); // 签名方法 目前只支持01-RSA方式证书加密
		data.put(SDKConstants.param_txnType, Global.getConfig("mlp.pay.param_txnType").toString()); // 交易类型 76-对账文件下载
		data.put(SDKConstants.param_txnSubType, Global.getConfig("mlp.pay.param_txnSubType").toString()); // 交易子类型 01-对账文件下载
		data.put(SDKConstants.param_bizType, Global.getConfig("mlp.pay.param_bizType").toString()); // 业务类型，固定

		/*** 商户接入参数 ***/
		data.put(SDKConstants.param_accessType, Global.getConfig("mlp.pay.param_accessType").toString()); // 接入类型，商户接入填0，不需修改
		data.put(SDKConstants.param_merId, merId); // 商户代码，请替换正式商户号测试，如使用的是自助化平台注册的777开头的商户号，该商户号没有权限测文件下载接口的，请使用测试参数里写的文件下载的商户号和日期测。如需777商户号的真实交易的对账文件，请使用自助化平台下载文件。
		data.put(SDKConstants.param_settleDate, settleDate); // 清算日期，如果使用正式商户号测试则要修改成自己想要获取对账文件的日期，
		data.put(SDKConstants.param_txnTime, DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		data.put(SDKConstants.param_fileType, Global.getConfig("mlp.pay.param_fileType").toString()); // 文件类型，一般商户填写00即可   00代表zip文件
		//2018.4.4 zhanghz修改参数暴露在外的问题   ---end
		//获取签名后的map
		Map<String, String> reqData = UnionPayUtils.sign(PayUtils.filterBlank(data), UnionPayUtils.encoding_UTF8, keyStore, payUnpConfigure.getKeyPwd());  		
		AcpUtils acpUtils = new AcpUtils();
		Map<String, String> rspData = acpUtils.post(reqData, url, UnionPayUtils.encoding_UTF8);
		
		// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
		if (!rspData.isEmpty()) {
			//验证签名   
			if (unionPayUtils.validate(rspData, UnionPayUtils.encoding_UTF8, fileURL+payUnpConfigure.getPublicKey())) {
				String respCode = rspData.get("respCode");
				if ("00".equals(respCode)) {
					// 交易成功，解析返回报文中的fileContent
					//start ---2018.4.4 zhanghz修改自拍对账文件保存指定位置  位置在mlp_online.propertise中的  利用byte流的方式保存zip文件 不解压  文件名称格式为 商户号+日期（对账日期）
					byte[]  zip_Byte=acpUtils.deCodeFileContent(rspData, UnionPayUtils.encoding_UTF8);
                    String filename =merId+"_"+DateUtils.formatDate(payDate, "yyyyMMdd")+".zip";
                    //保存文件
                    if(!FileUtils.savaFile(Global.getConfig("mlp.pay.zip_url").toString(), filename, zip_Byte)){
                    	sumManual++;
                        String message =DateUtils.getDateTime()+"在获取商户号为"+merId+"的"+settleDate+"的对账文件时保存"+filename+"文件到本地失败！";
                    	SendMailUtil.sendCommonMail(Global.getConfig("htmlmail.toAddress"),Global.getConfig("htmlmail.title"), message);
						log.info("商户号："+merId+"对账zip文件保存失败！！！");
                    }
				    //end ---2018.4.4 zhanghz
                    //从zip文件流中解析出对账文件内容
				    Map<String, String> resultMap = UnionPayUtils.parseZMZipFile(zip_Byte);				   
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
					/**
					 * 2018.06.11 guodongyu
					 * 设置缴费类型字段  支撑港建费与规费银联对账分支
					 */
					payImportFile.setPaymentType(PaymentTypeForImportFileEnum.FEESPAYMENT.getCode());  //设置缴费类型（01：港建费，02：规费）
					/**
					 * TODO 在银行对账表（内网）中添加一个清算时间字段
					 */
					//设置unionPay对账信息的参数
				    PayUnpReconcile payUnpReconcile = new PayUnpReconcile();
				    payUnpReconcile.setMerchant(merId);  
				    payUnpReconcile.setSplitType(payUnpConfigure.getSplitType());
				    payUnpReconcile.setSuccess(Integer.parseInt(Global.NO));
				    //银联在线对账实体中交易日期就是清算日期，所以在新建（银行在线对账）表中不必要添加清算日期字段
				    payUnpReconcile.setTransDate(payDate);
				    payUnpReconcile.setPayUnpReconcileDetailList(parseUnpReconcileFileForExternal(resultMap.get("content")));   //解析银联在线文件详情
					
				    //保存银联在线对账文件
					payImportFileService.saveUNPPayImportFile(payImportFile, payUnpReconcile);

					log.info("Unionpay:商户号："+payUnpConfigure.getMerchantCode()+"|辖区编码："+payUnpConfigure.getDistrict()+"|商户名称："+payUnpConfigure.getMerchantName()+"|清算日期："+settleDate+"|下载对账文件状态：成功");
				} else {
					 sumManual++;
					//2018.04.12 zhanghz 增加发送短信
					 String message ="Unionpay:商户号："+payUnpConfigure.getMerchantCode()+"|辖区编码："+payUnpConfigure.getDistrict()+"|商户名称："+payUnpConfigure.getMerchantName()+"|清算日期："+settleDate+"|下载对账文件状态：失败|应答码："+respCode;
                 	 SendMailUtil.sendCommonMail(Global.getConfig("htmlmail.toAddress"),Global.getConfig("htmlmail.title"), message);
					// 其他应答码为失败请排查原因
					log.error("Unionpay:商户号："+payUnpConfigure.getMerchantCode()+"|辖区编码："+payUnpConfigure.getDistrict()+"|商户名称："+payUnpConfigure.getMerchantName()+"|清算日期："+settleDate+"|下载对账文件状态：失败|应答码："+respCode);
				}
			} else {
				 sumManual++;
				 //2018.04.12 zhanghz 增加发送短信
				 String message ="Unionpay:商户号"+payUnpConfigure.getMerchantCode()+"下载对账单验证签名失败";
            	 SendMailUtil.sendCommonMail(Global.getConfig("htmlmail.toAddress"),Global.getConfig("htmlmail.title"), message);
				log.error("Unionpay:商户号"+payUnpConfigure.getMerchantCode()+"下载对账单验证签名失败");
			}
		} else {
			 sumManual++;
			 //2018.04.12 zhanghz 增加发送短信
			 String message ="Unionpay:商户号"+payUnpConfigure.getMerchantCode()+"未获取到返回对账单或返回http状态码非200";
       	     SendMailUtil.sendCommonMail(Global.getConfig("htmlmail.toAddress"),Global.getConfig("htmlmail.title"), message);
			 // 未返回正确的http状态
			log.error("Unionpay:商户号"+payUnpConfigure.getMerchantCode()+"未获取到返回对账单或返回http状态码非200");
		}
		return sumManual;
	}
	
	
	
	/**
	 * 解析银联在线对账文件(5.0.0版本)
	 * @author guodongyu
	 * @version 2018-04-28
	 */
	public List<PayUnpReconcileDetail> parseUnpReconcileFileForExternal(String content){
	    List<Map<Integer, String>> reconciliationMapList = UnionPayUtils.parseReconciliationFile(content);
	    List<PayUnpReconcileDetail> payUnpReconcileDetails = Lists.newArrayList();
	    
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
			PayUnpReconcileDetail payUnpReconcileDetail = new PayUnpReconcileDetail();
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
			/**
			 * guody  2018.04.28 关于外部系统私有域中的格式
			 * 
			 */
			//银行的私有域中包含支付单编码 业务类型 辖区编码   以,号分割
			List<String> result = Arrays.asList(reconciliationMap.get(34).trim().split(","));			
			//获得支付单编码
			String payDefrayBillId = result.get(0);
			payUnpReconcileDetail.setPriv1(payDefrayBillId);   //支付单  并添加去空格的操作
			payUnpReconcileDetail.setSuccess(Integer.parseInt(Global.NO));  //默认未对账
			payUnpReconcileDetails.add(payUnpReconcileDetail);         
		}
	    
		return payUnpReconcileDetails;
	}

}
