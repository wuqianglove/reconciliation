package com.mainsoft.mlp.reconciliation.common.webservice;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.AbstractMap;
import java.util.Map.Entry;

import javax.crypto.Cipher;

//import com.mainsoft.mlp.corpus.common.utils.Encodes;

/**
 * RSA加密解密
 * 
 * @author czz
 * @version 2016-11-12
 */
public class RSA {
	/**
	 * 生成公钥和私钥
	 * 
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static Entry<RSAPublicKey, RSAPrivateKey> getKeys() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("生成RSA钥对失败。");
		}
		keyPairGen.initialize(512);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return new AbstractMap.SimpleEntry<RSAPublicKey, RSAPrivateKey>(
				publicKey, privateKey);
	}

	/**
	 * 使用模和指数生成RSA公钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public static RSAPublicKey getPublicKey(BigInteger modulus, BigInteger exponent) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 使用模和指数生成RSA私钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA/
	 * None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public static RSAPrivateKey getPrivateKey(BigInteger modulus, BigInteger exponent) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] enBytes = cipher.doFinal(data.getBytes("UTF-8"));
		 return Convert.toBase64String(enBytes);
//		return Encodes.encodeBase64(enBytes);
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		 byte[] enBytes = cipher.doFinal(Convert.fromBase64String(data));
//		byte[] enBytes = cipher.doFinal(Encodes.decodeBase64(data));
		return new String(enBytes);
	}

	/**
	 * 验签。
	 * 
	 * @param key
	 *            公钥。
	 * @param data
	 *            待验证源数据。
	 * @param signature
	 *            签名。
	 * @return 是否通过验证。
	 */
	public static boolean verifyData(RSAPublicKey key, String data, String signature) {
		try {
			Signature sign = Signature.getInstance("SHA1withRSA");
			sign.initVerify(key);
			sign.update(data.getBytes("UTF-8"));
			 return sign.verify(Convert.fromBase64String(signature));
//			return sign.verify(Encodes.decodeBase64(data));
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 使用私钥数据签名。
	 * 
	 * @param key
	 *            私钥。
	 * @param data
	 *            待签名数据。
	 * @return 签名结果。
	 */
	public static String signData(RSAPrivateKey key, String data) {
		try {
			Signature sign = Signature.getInstance("SHA1withRSA");
			sign.initSign(key);
			sign.update(data.getBytes("UTF-8"));
			 return Convert.toBase64String(sign.sign());
//			return Encodes.encodeBase64(sign.sign());
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 生成DotNet程序使用的公钥。
	 * 
	 * @param key
	 *            RSAPublicKey。
	 * @return 公钥。
	 */
	public static String createDotNetPublicKey(RSAPublicKey key) {
		String[] strs = new String[2];
		byte[] ary_m = key.getModulus().toByteArray();
		if (ary_m[0] == 0 && ary_m.length == 65)// 判断数组首元素是否为0，若是，则将其删除，保证模的位数是128
		{
			byte[] temp = new byte[ary_m.length - 1];
			for (int i = 1; i < ary_m.length; i++) {
				temp[i - 1] = ary_m[i];
			}
			ary_m = temp;
		}
		 strs[0] = Convert.toBase64String(ary_m);
		 strs[1] =
		 Convert.toBase64String(key.getPublicExponent().toByteArray());
//		strs[0] = Encodes.encodeBase64(ary_m);
//		strs[1] = Encodes.encodeBase64(key.getPublicExponent().toByteArray());
		StringBuilder builder = new StringBuilder();
		builder.append("<RSAKeyValue><Modulus>");
		builder.append(strs[0]);
		builder.append("</Modulus><Exponent>");
		builder.append(strs[1]);
		builder.append("</Exponent></RSAKeyValue>");
		return builder.toString();
	}

	/**
	 * 根据.netpublickey生成公钥。
	 * 
	 * @param dotnetKey
	 *            由.net程序生成的公钥。
	 * @return RSAPublicKey。
	 * @throws Exception
	 */
	public static RSAPublicKey createFromDotNetPublicKey(String dotnetKey) throws Exception {
		String strModulus = "<Modulus>";
		String strEModulus = "</Modulus>";
		String strExponent = "<Exponent>";
		String strEExponent = "</Exponent>";
		int iMStart = dotnetKey.indexOf(strModulus) + strModulus.length();
		int iMEnd = dotnetKey.indexOf(strEModulus);
		int iEStart = dotnetKey.indexOf(strExponent) + strExponent.length();
		int iEEnd = dotnetKey.indexOf(strEExponent);
		try {
			 byte[] modulus =
			 Convert.fromBase64String(dotnetKey.substring(iMStart, iMEnd));
			 byte[] exponent =
			 Convert.fromBase64String(dotnetKey.substring(iEStart, iEEnd));
//			byte[] modulus = Encodes.decodeBase64(dotnetKey.substring(iMStart,
//					iMEnd));
//			byte[] exponent = Encodes.decodeBase64(dotnetKey.substring(iEStart,
//					iEEnd));
			return getPublicKey(new BigInteger(1, modulus), new BigInteger(1,
					exponent));
		} catch (Exception ex) {
			throw new Exception("生成公共密钥失败！");
		}
	}
}
