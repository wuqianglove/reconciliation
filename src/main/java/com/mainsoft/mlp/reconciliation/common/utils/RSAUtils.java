package com.mainsoft.mlp.reconciliation.common.utils;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
/**
* RSA 加解密工具方法
*/
@SuppressWarnings("restriction")
public class RSAUtils{
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	public static String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp8E9P+1dr9x9AhjZYKamhcA26JLLSE6TkVKbqeIHMmF+ZFcrF49F5UPnr1vsk8cHvy8Y80ygcV3pLltzdv1SFzE/C+iqhTtmLrP9j6PtafvOTdmCBjdn7aAcR/iEbKpdM0mEXnj15pRSGvd75YJ6Vol39mOiUv1lPY3wruXubRJpPOo+N0oSp0xuy9jFlRD98CR1GbYh+xWFcCaRdwJD8Ybm6NO8OOHDk5YqUOijQ78k/Z0e1AaOl6ZD3O0shkrGlcOYolfv2YchKYm2NHzR8lNo5h5pEjne7v3cFNwiBf1LeRI+YQDuD5hBwZPwRMIy4qvoT7JQxC4naXBHub4wAQIDAQAB";
	public static String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCnwT0/7V2v3H0CGNlgpqaFwDbokstITpORUpup4gcyYX5kVysXj0XlQ+evW+yTxwe/LxjzTKBxXekuW3N2/VIXMT8L6KqFO2Yus/2Po+1p+85N2YIGN2ftoBxH+IRsql0zSYReePXmlFIa93vlgnpWiXf2Y6JS/WU9jfCu5e5tEmk86j43ShKnTG7L2MWVEP3wJHUZtiH7FYVwJpF3AkPxhubo07w44cOTlipQ6KNDvyT9nR7UBo6XpkPc7SyGSsaVw5iiV+/ZhyEpibY0fNHyU2jmHmkSOd7u/dwU3CIF/Ut5Ej5hAO4PmEHBk/BEwjLiq+hPslDELidpcEe5vjABAgMBAAECggEADxgzM7JfELjtQqgLv0efij9ucWTsB7iSTKw7N4EQFrdxEQRK61bH+kYu7bd+xRlOmCRbwWCuuySxG6u86wMGRPH4c1DfLxfWLPOjKC6e6T9wI88jhP2tS1Nx2AQDnmeB9WBzGbEWp0XHUYfdTRx9d5XtBm2WqJMGbhB8jLBB0OyG0WOAX+RLWygVWsUtcTuHVCsHcuAkQ5jEDswS/CNy+KWXBCl04gRVqoufwzAXHnRIlCJ5CdC1QPy2yID5eAcfA4rOIrKAwED3pVd2WY3z6JrxS6EJggAm85vynROmn5JufMpb47itzH6ewmm+hIwoelv+zMLO3gxKHPye6wkjdQKBgQDb+Sf6aAdlVgqOiShRtBoGLYKo+zig3fJwqr7qJfuVN4PCzDODPbIg8sBazRfGJFhcc1c2Z1kK3Miee9LwVsh/HQR8vb4yPxwxlPhAnYyWIliBMoHgW6zLHhi2cR93M+kcAn7cKOxqphR+SjX3b4cyiCchlhntOJeFiAcb0h2sswKBgQDDOrceSN8fbX/YlTxRC2w+DqyUgzlBOQyyEkgaQxJxQE3pNm94FOBg/qK+reNuRLfgGDpPNh661TfdrNLHwTeQw/y5dNShkdWab6nTqZenO4+ulxNNlZ2/6UDo/fqV7giioBWL5BjJ3LYyeBVNsMdOu6dpfJ5aN8vvZvUXmlnyewKBgDK0zW1ge4v1eHuAzGWBEZqo9PyE5HnYj6E/CsEovkSYRmwp7Bntp9kGfCayLi/DgwbUo57r1c+2MxytSGOUbaU3iChFCMnGWHZpc4xZhOW6xxIJFX0lRMCRkHrFus4cjjihrqKLYGv5GrI94xDFibGxRuvsEM/pAqyKPbFBDwwXAoGBAJU8H9iNmcmbWXwE8Bt/oNl7PB82hCDAbFz+rpEsrj9WCXh5PRJM2wwp+dvKbqzO6MProT8Fvs3QySixwFoj78bAmkJl9Vggc2jfbLE41Y1ncTjjkUTgVrrpoc24v8G2OHGf73iE/xHnnnrL4nUF6gTcW2e+dAnA1yqEyFuniJRbAoGAcwi+NueBr23qBwO+pYodcZfe24Go/jdP9DcageXaVEo5CGSnjp4Jcryzrw/gIAuzj6P2/WwACzChvm5IeTJxQy383kIteAbHKuceusP+YWYCQ7Ru4xwQwbktjF34Rxv5/cxao2OU+V+KBqm4BVfCbtQbedraJHrLQq4dt4inlD0=";
	public static String publicKeyStrForPenalty ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDd7N2VLWO7YKNeijcnd0riIOHxDX4tEL+6NjB6G2G67GQaY9X/QdrRchA2C3oOhgzBnaSlayHr7cjD3y3JWieMZnyuR2JrX8PEUtGToOn8rxQ0sO6h6mEmuzyUXPs0qPYdJGyFuYXPqidSNxAvRaJeNzPcYOTIi2kIJJyfP/htzwIDAQAB";
	public static String privateKeyStrForPenalty = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN3s3ZUtY7tgo16KNyd3SuIg4fENfi0Qv7o2MHobYbrsZBpj1f9B2tFyEDYLeg6GDMGdpKVrIevtyMPfLclaJ4xmfK5HYmtfw8RS0ZOg6fyvFDSw7qHqYSa7PJRc+zSo9h0kbIW5hc+qJ1I3EC9Fol43M9xg5MiLaQgknJ8/+G3PAgMBAAECgYAZ3vsV4Oy3c1SYONSlLSr1oY+1YjVLW6B+PML5+Scze0tQKENSNoMj14A8TKo19zqVd6MyBYM21cmJzAKybmgdpks2qZrVVDAgrMG8MPtr5ol2wMSAeqkPJWwJ7wU4j50+JNVDUfPygNWea5rgDfNCEpE6wfKKJVTu/sRULy3fQQJBAPxIgyADTEKmxyqNn/QqX4n/EOPqW3fiCFE1EwShOEfArINlKSBToiD7U+7q2ldujXGz6Ltz2csf0HMzCJHRKK0CQQDhMdulT5wQVNngnKYdFG0Ykmp+oh9WkQS7JUglLgme79GZJAoPtE6dimBaEAg8okGy5JCf5wLcMgTnt8OY6FPrAkAfoJePs9ojoPTnCMo8ufMwQaHEcOT2qoeQZq7zraJ4nFcEQfJd8x4oNFnZYeftfhaNYpiSp3tuU5a2WneAkFAhAkEA4BhebD4xnqrhejxIrc2C1x307fllWB7N53ItPhp3JnueR1fuzzvts6ZdRaKIn9YTrPL43LblSLxZ10EuWv5vawJBAK+UCLFdbpyBe/qE2oQJ6iCDkkRigqxjpuFyVBDUDuZ0MXJqA66a3PhRS4zmgz8UKZLUoALdb113oX3ELKh6fQc=";	
	public static String publicKeyStrForSurveyor = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPfMUFBQOfQyxtrCUljUT0J2jPIGd4YX65zzdZhMI+YiH6KdJ4/g1WJ/gnVEr8j/W39VbboJM+1NsxhCh2U6Muz4XjbuKwSzmEq7syBkGlYxy6npZRQNu8sqvPtv5jKkXCmyKbLokof660IPm2Mv8hwZ7+7OnWfOu4McnbRNBVEwIDAQAB";
	public static String privateKeyStrForSurveyor ="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAM98xQUFA59DLG2sJSWNRPQnaM8gZ3hhfrnPN1mEwj5iIfop0nj+DVYn+CdUSvyP9bf1Vtugkz7U2zGEKHZToy7PheNu4rBLOYSruzIGQaVjHLqellFA27yyq8+2/mMqRcKbIpsuiSh/rrQg+bYy/yHBnv7s6dZ867gxydtE0FUTAgMBAAECgYBp9iEz4AyqgAand7t8wvmM6rdm1/ifjMLIJ5/vspH0g+8wL72SFsQ+1KixhSXKd8aYDM4v2YB5HQFX91Mmoq35pGKTWC7bUsAf65l0BTsUtJlt1jyC6DF+LIYLWhXHI00O/pHED6gGgBS1UG6QbCQ5hopuE3i+bSvFKLQ7Dz/5KQJBAP69ZJaEFlNdJz47fawwgU6nXOSGRIUg71XuliM0EEZqDUuW1uq41l1GLxbAiy2xaHhRmJqdMx8i1xKYAjMOGw8CQQDQg4kM9izaOcSPPigW2waSscIc9byNb4jV/rkfB4NWYCdfnjnorzkVY2g/nvnBsLfN41++hFaGxgIvLYkob/W9AkAhnksNFieAHvYT33QJamlWQP4gZpvaWetcEt2bjb5sHIjepex9E6Gus5l5TjXP9O7Nmi8ikIZzoHxzxr3bkQv/AkBgzqDjlquuSaG7i3UbX2cG1ma0AVcgJ0dVayTRhjBw+sVs/E4QfMG472UoHIwCZCSB/22lS1XMzsOW2cJTNt/VAkA1a2oJ1weRQ0SV7nuKHiVZpv6lnGehWPuNNt70l/1oDMfVlvPhmW5GWnhBiYAoz7GlhlDJHRhUYarWBVr5co76";
	
	/**
     * 得到公钥
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
    */
	public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
    /**
     * 得到私钥
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
    */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 签名和验证
     * @param data
     * @param privateKey 私有密钥字符串（经过base64编码）
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] data,String privateKey) throws Exception{
		PrivateKey priK = getPrivateKey(privateKey);
		Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);		
		sig.initSign(priK);
		sig.update(data);
		return sig.sign();
  	}
  	
    /**
     * 验证
     * @param data 被签名数据
     * @param sign 
     * @param publicKey 公有密钥字符串（经过base64编码）
     * @return
     * @throws Exception
     */
  	public static boolean verify(byte[] data,byte[] sign,String publicKey) throws Exception{
  		PublicKey pubK = getPublicKey(publicKey);
  		Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
  		sig.initVerify(pubK);
  		sig.update(data);
  		return sig.verify(sign);
  	}
  	
    /**
     * 验证
     * @param data 被签名数据
     * @param sign 数据
     * @param publicKey 公有密钥字符串（经过base64编码）
     * @return
     * @throws Exception
     */
 	public static boolean verify(String data,String sign,String publicKey,String privateKey) throws Exception{
 		byte[] signature = sign(data.getBytes(),privateKey);
 		return verify(data.getBytes(),signature,publicKey);
 	}
  	
 
  	/**
  	 * 加密
  	 * @param btPlaintext
  	 * @param publicKeyStr 公有密钥字符串（经过base64编码）
  	 * @return
  	 * @throws Exception
  	 */
  	public static byte[] encrypt(byte[] btPlaintext,String publicKeyStr)throws Exception{
  		PublicKey publicKey = getPublicKey(publicKeyStr);
  		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
  		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bt_encrypted = cipher.doFinal(btPlaintext);
        return bt_encrypted;
  	}
  	
  	/**
  	 * 加密
  	 * @param btPlaintext 被加密字段
  	 * @param publicKeyStr 公有密钥字符串（经过base64编码）
  	 * @return
  	 * @throws Exception
  	 */
  	public static String encrypt(String btPlaintext,String publicKeyStr) throws Exception{
  		return Base64.encodeBase64String(encrypt(btPlaintext.getBytes(), publicKeyStr));
  	}
  	
  	/**
  	 * 解密
  	 * @param btPlaintext 被加密字段
  	 * @param publicKeyStr 私有密钥字符串（经过base64编码）
  	 * @return
  	 * @throws Exception
  	 */
  	public static String decrypt(String btEncrypted,String privateKey) throws Exception{
  		return new String(decrypt(Base64.decodeBase64(btEncrypted) ,privateKey));
  	}
  	
  	
  	
  	/**
  	 * 解密
  	 * @param btEncrypted 要解密的代码
  	 * @param privateKey 私有密钥字符串（经过base64编码）
  	 * @return
  	 * @throws Exception
  	 */
  	public static byte[] decrypt(byte[] btEncrypted,String privateKey) throws Exception{
        PrivateKey pKey = getPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, pKey);
        byte[] btOriginal = cipher.doFinal(btEncrypted);
        return btOriginal;
  	}
  	
  	//********************main函数：加密解密和签名验证*********************
    public static void main(String[] args) throws Exception {
        String strPlaintext = "这是一段用来测试密钥转换的明文";
        System.err.println("明文：" + strPlaintext);
        byte[] btCipher = encrypt(strPlaintext.getBytes(),publicKeyStr);
        System.out.println("加密后："+Base64.encodeBase64String(btCipher));
        byte[] btOriginal = decrypt(btCipher,privateKeyStr);
        String strOriginal = new String(btOriginal);
        System.out.println("解密结果:" + strOriginal);
        String str="被签名的内容";
		System.err.println("\n原文:"+ str);
		byte[] signature = sign(str.getBytes(),privateKeyStr);
		System.out.println("产生签名：" + Base64.encodeBase64String(signature));
		boolean status = verify(str.getBytes(), signature,publicKeyStr);
		System.out.println("验证情况：" + status);
		System.out.println("测试2: 加密后 " + encrypt("我是个神奇的测试代码我叫,,,...&*!@#$$%$*()_",publicKeyStr));
		System.out.println("测试2: 解密后 " + decrypt(encrypt("我是个神奇的测试代码我叫,,,...&*!@#$$%$*()_",publicKeyStr),privateKeyStr));
		status = verify("我是个神奇的测试代码我叫,,,...&*!@#$$%$*()_",encrypt("我是个神奇的测试代码我叫,,,...&*!@#$$%$*()_", publicKeyStr),publicKeyStr,privateKeyStr);
		System.out.println(status);
    }
 
}