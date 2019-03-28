package com.mainsoft.mlp.reconciliation.common.webservice;

import java.io.IOException;

import com.mainsoft.mlp.common.utils.Encodes;

/**
 * 一些数组转换,编码方面的方法
 * 使用本项目的公共类Encodes会出问题
 * 
 * @author will
 */
public class Convert {

	/**
	 * 将byte[]转换成Byte[]类型。
	 * 
	 * @param array
	 *            原数组。
	 * @return null || Byte[]。
	 */
	public static Byte[] toObject(byte[] array) {
		if (array != null) {
			int bLength = array.length;
			Byte[] arrayNew = new Byte[bLength];
			for (int i = 0; i < bLength; i++) {
				arrayNew[i] = array[i];
			}
			return arrayNew;
		} else {
			return null;
		}
	}

	/**
	 * 将Byte[]转换成byte[]类型。
	 * 
	 * @param array
	 *            Byte[]数组。
	 * @return null || byte[]。
	 */
	public static byte[] toPrimitive(Byte[] array) {
		if (array != null) {
			int bLength = array.length;
			byte[] arrayNew = new byte[bLength];
			for (int i = 0; i < bLength; i++) {
				arrayNew[i] = array[i].byteValue();
			}
			return arrayNew;
		} else {
			return null;
		}
	}

	/**
	 * 将char[]转换成Character[]类型。
	 * 
	 * @param charArray
	 *            原数组。
	 * @return null || Character[]。
	 */
	public static Character[] toObject(char[] charArray) {
		if (charArray != null) {
			int bLength = charArray.length;
			Character[] arrayNew = new Character[bLength];
			for (int i = 0; i < bLength; i++) {
				arrayNew[i] = charArray[i];
			}
			return arrayNew;
		} else {
			return null;
		}
	}

	/**
	 * 将Character[]转换成char[]类型。
	 * 
	 * @param charArray
	 *            原数组。
	 * @return null || char[]。
	 */
	public static char[] toPrimitive(Character[] charArray) {
		if (charArray != null) {
			int bLength = charArray.length;
			char[] arrayNew = new char[bLength];
			for (int i = 0; i < bLength; i++) {
				arrayNew[i] = charArray[i];
			}
			return arrayNew;
		} else {
			return null;
		}
	}

	/**
	 * 将byte数组转换成Base64编码的字符串。
	 * 
	 * @param array
	 *            带转换数组。
	 * @return Base64编码的字符串。
	 */
	public static String toBase64String(byte[] array) {
		String temp = Encodes.encodeBase64(array);
		//和linux互操作需要去除win的换行，因为win的换行不同于linux的
		return temp;
		//return temp.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", ""); 
	}

	/**
	 * 将Base64编码的字符串转换成byte数组。
	 * 
	 * @param base64String
	 *            字符串。
	 * @return byte数组
	 * @throws IOException
	 */
	public static byte[] fromBase64String(String base64String)
			throws IOException {
		return Encodes.decodeBase64(base64String);
	}

	/**
	 * 将字符串转换成对应枚举。（此方法需要重写toString支持。）
	 * @param classType 枚举类型。
	 * @param value 字符串。
	 * @return
	 */
	public static <T  extends Enum<?>> T stringToEnumeration(Class<T> classType, String value) {
		try {
			if(value == null){
				return null;
			}else{
				for (T aObj : classType.getEnumConstants()) {
					if (aObj.toString().toUpperCase().equals(value.toUpperCase())) {
						return aObj;
					}
					else if (aObj.name().toUpperCase().equals(value.toUpperCase()))
					{
						return aObj;
					}
				}
			}
		} catch (Exception ex) {

		}
		return null;
	}
}
