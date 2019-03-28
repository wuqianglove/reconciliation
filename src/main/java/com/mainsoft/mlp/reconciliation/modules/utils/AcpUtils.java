package com.mainsoft.mlp.reconciliation.modules.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Maps;
import com.mainsoft.mlp.reconciliation.modules.unionpaySDK.HttpClient;
import com.mainsoft.mlp.reconciliation.modules.unionpaySDK.SDKConstants;
import com.mainsoft.mlp.reconciliation.modules.unionpaySDK.SecureUtil;

public class AcpUtils {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 功能：后台交易提交请求报文并接收同步应答报文<br>
	 * @param reqData 请求报文<br>
	 * @param encoding 应答报文<br>
	 * @param reqUrl  请求地址<br>
	 * @param encoding<br>
	 * @return 应答http 200返回true ,其他false<br>
	 */
	public Map<String,String> post(Map<String, String> reqData,String reqUrl,String encoding) {
		Map<String, String> rspData = Maps.newHashMap();
		//发送后台请求数据
		//2018.03.06 guodongyu  原30000 因为上海一直提示连接超时 修改为120000
		HttpClient hc = new HttpClient(reqUrl, 120000, 120000);
		try {
			int status = hc.send(getRequestParamString(reqData, encoding), encoding);
			if (200 == status) {
				String resultString = hc.getResult();
				if (null != resultString && !"".equals(resultString)) {
					// 将返回结果转换为map
					Map<String,String> tmpRspData  = PayUtils.convertResultStringToMap(resultString);
					rspData.putAll(tmpRspData);
				}
			}else{
				logger.error("返回http状态码["+status+"]，请检查请求报文或者请求地址是否正确");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return rspData;
	}
	
	 /**
	 * 将Map存储的对象，转换为key=value&key=value的字符
	 *
	 * @param requestParam
	 * @param coder
	 * @return
	 */
	private String getRequestParamString(Map<String, String> requestParam, String coder) {
		if (null == coder || "".equals(coder)) {
			coder = "UTF-8";
		}
		StringBuffer sf = new StringBuffer("");
		String reqstr = "";
		if (null != requestParam && 0 != requestParam.size()) {
			for (Entry<String, String> en : requestParam.entrySet()) {
				try {
					sf.append(en.getKey()
							+ "="
							+ (null == en.getValue() || "".equals(en.getValue()) ? "" : URLEncoder
									.encode(en.getValue(), coder)) + "&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return "";
				}
			}
			reqstr = sf.substring(0, sf.length() - 1);
		}
		return reqstr;
	}
	
	/**
	 * 功能：解析交易返回的fileContent字符串并落地 （ 解base64，解DEFLATE压缩并转为byte[]类型)
	 * 适用到的交易：对账文件下载，批量交易状态查询<br>
	 * @param resData 返回报文map<br>
	 *
	 * @param encoding 上送请求报文域encoding字段的值<br>	
	 */
	public byte[] deCodeFileContent(Map<String, String> resData ,String encoding) {
		// 解析返回文件
		String fileContent = resData.get(SDKConstants.param_fileContent);
		if (null != fileContent && !"".equals(fileContent)) {
			try {
				byte[] fileArray = SecureUtil.inflater(SecureUtil.base64Decode(fileContent.getBytes(encoding)));
				return fileArray;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
