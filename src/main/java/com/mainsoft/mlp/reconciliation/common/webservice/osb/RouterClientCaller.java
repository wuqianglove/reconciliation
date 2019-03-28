package com.mainsoft.mlp.reconciliation.common.webservice.osb;

import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mainsoft.mlp.common.mapper.JsonMapper;
import com.mainsoft.mlp.reconciliation.common.config.Global;
import com.mainsoft.mlp.reconciliation.common.service.ServiceException;
import com.mainsoft.mlp.reconciliation.common.webservice.GZip;
import com.mainsoft.mlp.reconciliation.common.webservice.RSA;
import com.mainsoft.mlp.reconciliation.common.webservice.ServiceResult;
import com.mainsoft.mlp.reconciliation.common.webservice.ServiceSupport;

/**
 * OSB一级总线服务客户端
 * @author liushr
 * @version 2017-04-11
 */
@Component
public class RouterClientCaller {
	/**
	 * 日志对象
	 */
	protected static Logger logger = LoggerFactory.getLogger(RouterClientCaller.class);
	protected static final RSAPrivateKey privateKey = ServiceSupport.getPrivateKey();
	
	/**
	 *  @param deployAreaId 部署辖区编码
	 *  @param method 要处理的方法名称
	 *  @param json 业务数据
	 */
	public ServiceResult callRouterServer(String deployAreaId, String method, String businessName, String json) {
		String checkValue = RSA.signData(privateKey, json);
		try {
			if(StringUtils.isBlank(deployAreaId)){
				throw new ServiceException("没有获取到部署辖区编码");
			}
			URL wsdlLocation = new URL(Global.getConfig("webService.osb.corpus.url"));
			Service factory = new Service(wsdlLocation);
			ServiceSoap serviceSoap = factory.getServiceSoap();
			byte[] args = GZip.compress(json.getBytes("utf-8"));
			//UserUtils.getArea().getParent().getId()
			byte[] result = serviceSoap.serviceEntrance(deployAreaId, businessName, method+".GZip", args, checkValue);
			return (ServiceResult) JsonMapper.fromJsonString(new String(GZip.uncompress(result), "utf-8"), ServiceResult.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServiceSupport.createFailure("调用OSB一级总线服务客户端发生异常:"+e.getMessage());
		}
	}
}
