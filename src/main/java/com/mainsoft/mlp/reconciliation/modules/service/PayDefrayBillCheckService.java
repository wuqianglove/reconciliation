/*
package com.mainsoft.mlp.reconciliation.modules.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyStore;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Maps;
import com.mainsoft.mlp.common.utils.EnumsUtils;
import com.mainsoft.mlp.reconciliation.common.config.Global;
import com.mainsoft.mlp.reconciliation.modules.entity.CfgDistrict;
import com.mainsoft.mlp.reconciliation.modules.web.CfgDistrictService;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpConfigure;
import com.mainsoft.mlp.reconciliation.modules.enums.PayPaymentBillBusinessTypeEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PayUnpCfgAccountTypeEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PaymentGatewayEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.QueryRespStauseEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.UnpRespCodeEnum;
import com.mainsoft.mlp.reconciliation.modules.utils.AcpUtils;
import com.mainsoft.mlp.reconciliation.modules.utils.PayUtils;
import com.mainsoft.mlp.reconciliation.modules.utils.UnionPayUtils;

*/
/**
 * 查询支付单交易状态Service
 * 在此Service中进行各个银行的查询接口的调用
 * @author guody
 * @date 2018年1月19日
 *//*

@Service
public class PayDefrayBillCheckService {
	*/
/**
	 * 日志对象
	 *//*

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public static String accessType5 = "0";//接入类型			
	@Autowired
	private CfgDistrictService cfgDistrictService;
	
	@Autowired
	private PayUnpConfigureService payUnpConfigureService;
	

	

	
	public static final String query_Cmd = "QueryOrdDetail";  
	//先完成unionpay的查询接口调用
	*/
/**
	 * 这里传入的参数 应该有商户号  辖区编码 支付网关（支付单表中没有此字段）
	 * @param merId 商户号
	 * @param district  辖区编码
	 * @param orderId  订单号
	 * @param channelType 网关
	 * @return
	 *//*

	public  Map<String, String> checkUnpPayDefrayBill(String merId, String district, String orderId, String channelType){		
		AcpUtils acpUtils = new AcpUtils();
		Map<String, String> requestData = Maps.newHashMap();		
		*/
/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***//*

		requestData.put("version", UnionPayUtils.version);   			      //版本号，全渠道默认值
		requestData.put("encoding", UnionPayUtils.encoding_UTF8); 	      //字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", "01");            			  //签名方法，只支持 01：RSA方式证书加密
		requestData.put("txnType", "00");               			  //交易类型 ，01：消费
		requestData.put("txnSubType", "00"); 
		//交易子类型， 01：自助消费
		requestData.put("bizType", channelType);    //支付网关      000202企业用户  000201 个人用户    			  //渠道类型 固定07		
		*/
/***商户接入参数***//*

		requestData.put("merId", merId);                  //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		requestData.put("accessType", accessType5);                           //接入类型，商户接入固定填0，不需修改
		
		*/
/***要调通交易以下字段必须修改***//*

		requestData.put("orderId", orderId);                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
		requestData.put("txnTime", UnionPayUtils.getCurrentTime());               //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间		
		*/
/**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**//*

		PayUnpConfigure payUnpConfigure = new PayUnpConfigure(); //用来获取银联支付的配置
		//通过辖区编码  商户号  支付网关 定义到银联支付的配置
		//根据网管定义到时个人还是企业
		//企业
		if(EnumsUtils.stringEquals(channelType, PaymentGatewayEnum.UNION_COMPANY_PAY)){
			payUnpConfigure.setAccountType(PayUnpCfgAccountTypeEnum.COMPANY.getCode());
		}
		//个人
		if(EnumsUtils.stringEquals(channelType, PaymentGatewayEnum.UNION_CARD_PAY)){
			payUnpConfigure.setAccountType(PayUnpCfgAccountTypeEnum.INDIVIDUAL.getCode());
		}
		payUnpConfigure.setDistrict(district);
		payUnpConfigure.setMerchantCode(merId);
		payUnpConfigure.setBusinessType(PayPaymentBillBusinessTypeEnum.PORTCONSTRUCTIONFEE.getCode());
		payUnpConfigure = payUnpConfigureService.getPayunpConfigureForQuery(payUnpConfigure);
		//unionPay私钥的路径
		String priKeyPath = null;
		try {
			priKeyPath = getFileAbsolutePath(payUnpConfigure.getPrivateKey());
			logger.error("私钥路径位置："+priKeyPath);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());		
		}
		String publicKeyPath = null;
		try {
			publicKeyPath = getFileAbsolutePath(payUnpConfigure.getPublicKey());
			logger.error("公钥路径位置："+publicKeyPath);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			logger.error("获取商户:"+merId+"的配置失败,找不到公钥路径："+payUnpConfigure.getPublicKey());
		}				
		//对requestData进行签名
		Map<String, String> sign = Maps.newHashMap();
		//创建一个方法  返回 验签对象
		try {
			 sign = getUnionPayQuerySign(payUnpConfigure, priKeyPath, requestData);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		//获得银联后台  查询地址url  通过  辖区配置表中的数据
		//TODO 设置银联提交地址   不应该使用缓存获得   传入辖区编码
		CfgDistrict CfgDistrict = new CfgDistrict();
		CfgDistrict.setDistrict(payUnpConfigure.getDistrict());
		CfgDistrict.setComponent(CfgDistrictService.UNIONPAY_QUERYTRANS_URL);
		String queryUrl = cfgDistrictService.findCfgsByAreaIdAndComponent(CfgDistrict);
		Map<String, String> rspData = acpUtils.post(sign,queryUrl,UnionPayUtils.encoding_UTF8);
		String queryRespStatus = "";
			*/
/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**//*

			//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			//验签处理
			if(UnionPayUtils.validate(rspData, UnionPayUtils.encoding_UTF8, publicKeyPath)){
				logger.error("验证签名成功");				
				if(EnumsUtils.stringEquals(rspData.get("respCode"), UnpRespCodeEnum.SUCCEED)){//如果查询交易成功
					//处理被查询交易的应答码逻辑
					String origRespCode = rspData.get("origRespCode");
					if(EnumsUtils.stringEquals(origRespCode, UnpRespCodeEnum.SUCCEED) || EnumsUtils.stringEquals(origRespCode, UnpRespCodeEnum.SUCCEEDOTHER)){
						//返回交易状态的枚举类型     定义为01  交易成功
						queryRespStatus=QueryRespStauseEnum.SUCCEED.getCode();
						
					}else if(EnumsUtils.stringEquals(origRespCode, UnpRespCodeEnum.DISPOSINGONE) ||
							EnumsUtils.stringEquals(origRespCode, UnpRespCodeEnum.DISPOSINGTWO) ||
							EnumsUtils.stringEquals(origRespCode, UnpRespCodeEnum.DISPOSINGTHREE)){
						//需再次发起交易状态查询交易 ，返回交易成功的枚举类型     定义为02  订单交易处理中，请稍后再试
						//TODO  暂时不考虑  或者反馈一个请过几分钟再尝试  但是如果这样的  就不能使用boolean返回值
						queryRespStatus= QueryRespStauseEnum.DISPOSING.getCode();
					}else{
						//其他应答码为失败请排查原因
						//TODO
						//返回交易状态的枚举类型     定义为03  交易失败
						queryRespStatus= QueryRespStauseEnum.FAILED.getCode();
					}
				}else if(EnumsUtils.stringEquals(rspData.get("respCode"), UnpRespCodeEnum.INEXISTENCE)){
					*/
/**
					 * 订单不存在，可认为交易状态未明（由于网络因素，可能先发交易再发查询，但是银联先收到查询导致返回34响应码）
					 * 需要稍后发起交易状态查询（一般交易请求发起后3分钟后查询还是返回订单不存在可以认为失败）
					 * 或依据对账结果为准
					 *//*

					//查询交易本身失败，或者未查到原交易，检查查询交易报文要素
					//TODO    返回交易状态的枚举类型     定义为04  返回订单不存在
					queryRespStatus= QueryRespStauseEnum.INEXISTENCE.getCode();
				}else {
					*/
/**
					 * 查询交易失败  如应答码10/11检查查询报文是否正确 需稍后发起交易状态查询
					 * 返回查询结果 通知 查询交易方法失败 请联系华正公司   05
					 *//*

					queryRespStatus= QueryRespStauseEnum.QUERYFAILED.getCode();					
				}
			}else{
				logger.error("验证签名失败");
				//TODO 检查验证签名失败的原因     06
				queryRespStatus= QueryRespStauseEnum.SIGNFAILED.getCode();
			}
		}else{
			//未返回正确的http状态     07  调用银联查询接口不通 请稍后再试
			logger.error("未获取到返回报文或返回http状态码非200");
			queryRespStatus= QueryRespStauseEnum.NETWORKLOSE.getCode();
		}
		
		rspData.put("queryRespStatus", queryRespStatus);
		return rspData;		
	}	
	
	

	
	private Map<String, String> getUnionPayQuerySign(PayUnpConfigure payUnpConfigure,String priKeyPath, Map<String, String> requestData) throws IOException {
		//获取证书，并转为对象
		KeyStore keyStore = UnionPayUtils.getKeyInfo(priKeyPath, payUnpConfigure.getKeyPwd()); 
	    //进行签名
		return UnionPayUtils.sign(PayUtils.filterBlank(requestData), UnionPayUtils.encoding_UTF8, keyStore, payUnpConfigure.getKeyPwd());
		// TODO Auto-generated method stub
		
	}


	*/
/**
	 * 获取文件的绝对路径
	 * @param request
	 * @param filePath  文件路径，相对于WEB-INF的路径
	 * @return
	 * @throws MalformedURLException
	 *//*

	public String getFileAbsolutePath(String filePath) throws MalformedURLException{
		return Global.getConfig("mlp.pay.file.url") + filePath;
	}
}
*/
