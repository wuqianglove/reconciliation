package com.mainsoft.mlp.reconciliation.common.webservice;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

public class ServiceSupport {

	/**
	 * 验证.net加密后的签名
	 * 
	 * @param paramJson
	 *            参数JSON
	 * @param checkValue
	 *            签名字符串
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyData(String paramJson, String checkValue) {
		return RSA.verifyData(getPublicKey(), paramJson, checkValue);
	}

	/**
	 * 验证java加密后的签名
	 * 
	 * @param paramJson
	 *            参数JSON
	 * @param checkValue
	 *            签名字符串
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyData2(String paramJson, String checkValue) {
		return RSA.verifyData(getPublicKey2(), paramJson, checkValue);
	}

	/**
	 * 将数据签名
	 * 
	 * @param paramJson
	 *            参数JSON
	 * @return
	 * @throws Exception
	 */
	public static String signData(String paramJson) {
		return RSA.signData(getPrivateKey(), paramJson);
	}

	/**
	 * 验证时间
	 * 
	 * @param passport
	 *            通信身份对象
	 * @return
	 */
	public static boolean verifyIssueTime(DPS_Passport passport) {
		boolean timeout = false;
		Date nowDate = new Date();
		// 10分钟超时
		long timespan = 10 * 60 * 1000;
		if (passport.getIssueTime() != null && nowDate.getTime() - passport
				.getIssueTime().getTime() >= timespan) {
			timeout = true;
		}
		return !timeout;
	}

	/**
	 * 获取系统公钥
	 * 
	 * @return
	 */
	public static RSAPublicKey getPublicKey() {

		String key = "<RSAKeyValue><Modulus>5Um4lzJoO6MvGGo16hC+OaGGWb9y0qbFjbIEwuCR6q6NK3txxvKO0ODoofEeIh2kabgz3DgWuDLYPCfct75/+Q==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
		try {
			return RSA.createFromDotNetPublicKey(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取系统公钥-验证java加密的串
	 * 
	 * @return
	 */
	public static RSAPublicKey getPublicKey2() {

		String key = "<RSAKeyValue><Modulus>vWjlxgBiXNIN9WlwICjTL04dzeJaRUe+dP1R+daKnYt44Z2sWgZZ+Q5OBJD3GidYjUJau8tNH4S5dRsc6daodw==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
		try {
			return RSA.createFromDotNetPublicKey(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取系统私钥
	 * 
	 * @return
	 */
	public static RSAPrivateKey getPrivateKey() {
		return RSA
				.getPrivateKey(
						new BigInteger(
								"9920193864727170037609676795445416876278597994883736526118797063169858417213066294229564941538926080951001720963433915143207439792348767312114603716421751"),
						new BigInteger(
								"225386707731186294551181908668663895643359207995207038579594562263452998813341457367497214973896846690423272800666056853425746536752771512771756648883137"));

	}

	public static String getAppId() {
		return "023";
	}

	/**
	 * 创建成功结果对象
	 * 
	 * @param data
	 *            返回的结果
	 * @return
	 */
	public static ServiceResult createSuccess(Object data) {
		return new ServiceResult("0", "", data);
	}

	/**
	 * 创建业务失败结果对象
	 * 
	 * @param message
	 *            错误信息
	 * @return
	 */
	public static ServiceResult createFailure(String message) {
		return new ServiceResult("2", message, null);
	}

	/**
	 * 创建系统失败结果对象
	 * 
	 * @param ex
	 * @return
	 */
	public static ServiceResult createFailure(Exception ex) {
		return new ServiceResult("1", ex.toString(), null);
	}

	/**
	 * 创建签名失败结果对象
	 * 
	 * @return
	 */
	public static ServiceResult createCheckFailure() {
		return new ServiceResult("3", "验证签名失败", null);
	}

	/**
	 * 创建请求超时结果对象
	 * 
	 * @return
	 */
	public static ServiceResult createTimeOutFailure() {
		return new ServiceResult("4", "请求超时", null);
	}

	/**
	 * 创建方法名不存在的失败结果对象
	 * 
	 * @param method
	 * @return
	 */
	public static ServiceResult createNoSuchMethodFailure(String method) {
		return new ServiceResult("5", "不存在方法名【" + method + "】", null);
	}
	
	/**
	 * 创建加密明文失败的结果对象
	 * @return
	 */
	public static ServiceResult createEncryptFailure() {
		return new ServiceResult("6", "加密明文失败！", null);
	}
	
	/**
	 * 创建密文解密失败的结果对象
	 * @return
	 */
	public static ServiceResult createDecryptFailure() {
		return new ServiceResult("7", "密文解密失败！", null);
	}

}
