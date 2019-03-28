package com.mainsoft.mlp.reconciliation.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import com.mainsoft.mlp.reconciliation.common.webservice.Convert;

/**
 *  MD5加密解密。
 * @author sxycw
 *
 */
public class MD5 {

	/**
	 * 随机发生器。
	 */
	private static Random random;
	/**
	 * MD5 编码器。
	 */
	private static MessageDigest md5Provider;
	static {
		random = new Random();
		try {
			md5Provider = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {

		}
	}

	/**
	 * MD5 加密（UTF8编码、MD5 加密、Base64 编码）。
	 * 
	 * @param letter
	 *            明文。
	 * @return 密文。
	 */
	public static String md5Encrypt(String letter) {
		synchronized(md5Provider){
			if (letter != null) {
				md5Provider.reset();
				byte[] salt = doubleToByte(random.nextDouble());
				byte[] buffer = byteConcat(salt, utf8Encode(letter));
				md5Provider.update(buffer);
				byte[] result = byteConcat(salt, md5Provider.digest());
				return Convert.toBase64String(result);
			} else {
				return null;
			}
		}
	}

	/**
	 * MD5 比较（UTF8编码、MD5 加密、Base64 编码）。
	 * 
	 * @param letter
	 *            明文。
	 * @param cipher
	 *            密文。
	 * @return 是否一致。
	 */
	public static boolean md5Compare(String letter, String cipher) {
		synchronized(md5Provider){
			if (letter != null && cipher != null) {
				md5Provider.reset();
				byte[] salt;
				try {
					salt = byteRegion(Convert.fromBase64String(cipher), 0, 8);
				} catch (IOException e) {
					return false;
				}
				byte[] buffer = byteConcat(salt, utf8Encode(letter));
				md5Provider.update(buffer);
				byte[] result = byteConcat(salt, md5Provider.digest());
				return cipher.equals(Convert.toBase64String(result));
			} else {
				return letter == cipher;
			}
		}
	}

	/**
	 * 字节数组连接。
	 * 
	 * @param values
	 *            各个字节数组。
	 * @return 字节数组。
	 */
	private static byte[] byteConcat(byte[]... values) {
		int iSize = 0;
		for (byte[] value : values) {
			iSize += value.length;
		}
		byte[] tempArray = new byte[iSize];
		int iCounter = 0;
		for (byte[] value : values) {
			for (byte bValue : value) {
				tempArray[iCounter] = bValue;
				iCounter++;
			}
		}
		return tempArray;
	}

	/**
	 * 字节数组区间。
	 * 
	 * @param values
	 *            字节数组。
	 * @param indexStart
	 *            开始位置。
	 * @param count
	 *            字节个数。
	 * @return 字节数组区间。
	 */
	private static byte[] byteRegion(byte[] values, int indexStart, int count) {
		byte[] byteArray = new byte[count];
		for (int i = 0; i < count; i++) {
			byteArray[i] = values[indexStart + i];
		}
		return byteArray;
	}

	/**
	 * UTF8 编码。
	 * 
	 * @param text
	 *            文本。
	 * @return 编码后的值。空文本或者编码失败会返回空。
	 */
	private static byte[] utf8Encode(String text) {
		if (text == null || text.trim() == "") {
			return null;
		} else {
			byte[] bs = text.getBytes();
			// 用新的字符编码生成字符串
			try {
				return new String(bs, "UTF-8").getBytes();
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
	}

	/**
	 * 将double转换为byte数组。
	 * 
	 * @param dValue
	 *            double值。
	 * @return 数组。
	 */
	public static byte[] doubleToByte(double dValue) {
		byte[] bytes = new byte[8];
		long l = Double.doubleToLongBits(dValue);
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = new Long(l).byteValue();
			l = l >> 8;
		}
		return bytes;
	}

}
