/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.common.utils;

import java.io.Serializable;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * @author chenzw
 * @version 2017-01-18
 */
@Service
@Lazy(false)
public class IdGen extends com.mainsoft.mlp.common.utils.IdGen implements SessionIdGenerator {

	@Override
	public Serializable generateId(Session session) {
		return IdGen.uuid();
	}
	
	/**
	 * 生成inv_invoice表id
	 * @author cuiyl
	 * @version 2017年1月18日
	 */
	// 收据编码（类型编码+10位票号）
	public static String generateId(String species, Long beginCode){
		int length = beginCode.toString().length();
		String spellZero = "";
		for(int i=0; i < 10-length; i++){
			spellZero += "0";
		}
		return species+spellZero+beginCode;
	}
	
	
	public static void main(String[] args) {
		System.out.println(IdGen.uuid());
		System.out.println(IdGen.uuid().length());
		//System.out.println(new IdGen().getNextId());
		for (int i=0; i<1000; i++){
			System.out.println(IdGen.randomLong() + "  " + IdGen.randomBase62(5));
		}
	}

}
