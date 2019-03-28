/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.common.utils;

/*import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;*/
import com.mainsoft.mlp.common.utils.Encodes;
import com.mainsoft.mlp.common.security.Digests;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @author ThinkGem
 * @version 2013-05-22
 */
public class StringUtils extends com.mainsoft.mlp.common.utils.StringUtils {

	/**
	 * 获得i18n字符串
	 *//*
	public static String getMessage(String code, Object[] args) {
		LocaleResolver localLocaleResolver = (LocaleResolver) SpringContextHolder.getBean(LocaleResolver.class);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		Locale localLocale = localLocaleResolver.resolveLocale(request);
		return SpringContextHolder.getApplicationContext().getMessage(code, args, localLocale);
	}*/

    /**
     * 获取哈希后的10位随机密码
     * @param randomPassword 随机密码
     * @return 返回哈希后的10位随机密码
     * @author chenzw
     */
    public static String getHashRandomPassword(String randomPassword) {
		byte[] salt = Digests.generateSalt(8);
		byte[] hashPassword = Digests.sha1(randomPassword.getBytes(), salt, 1024);
		String hashRandomPassword = Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
		return hashRandomPassword;
	}
    
    
    /**
     * 在字符前面自动补字符
     * @param target 目标字符串
     * @param size  长度
     * @param character 字符
     * @return 
     * @author gaosong
     */
    public static String frontComplementaryCharacter(String target,int size,String character){
    	int diff = size - target.length();
    	if(diff > 0){
    		String front = "";
    		for(int i = 0 ; i < diff ; i ++ ){
    			front = front + character;
    		}
    		target = front + target;
    	}
		return target;
    }
}
