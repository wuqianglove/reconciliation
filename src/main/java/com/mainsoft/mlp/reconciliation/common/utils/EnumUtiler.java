package com.mainsoft.mlp.reconciliation.common.utils;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Lists;
import com.mainsoft.mlp.common.enums.IEnum;
import com.mainsoft.mlp.common.utils.EnumsUtils;
import com.mainsoft.mlp.reconciliation.modules.entity.Dict;

/**
 * 枚举工具扩展类
 * 各系统需要自己拓展的方法加在此类中
 * @author chenzw
 * @version 2017年11月30日
 */
public class EnumUtiler extends EnumsUtils {
	/**
	 * 日志对象
	 */
	protected static Logger logger = LoggerFactory.getLogger(EnumsUtils.class);
	
	/**
     * 通过枚举类名和枚举模块名获取枚举的Dict的List
     * 用于jsp页面显示下拉列表
     * @param moduleName 模块名（包名倒数第二级）
     * @param enumClassName 枚举类名
     * @return List<Dict>
     * @author chenzw
     * @version 2017年09月03日
     */
    public static List<Dict> getDictListByEnumClassName(String moduleName, String enumClassName) {
    	try {
	    	if (StringUtils.isNotBlank(enumClassName)){
				Class<? extends IEnum> enumClass = getEnumClass(moduleName, enumClassName);
				IEnum[] enums = enumClass.getEnumConstants();
				List<Dict> dictList = Lists.newArrayList();
				for(IEnum iEnum :enums) {
					Dict dict = new Dict();
					dict.setLabel(iEnum.getDesc());
					dict.setValue(iEnum.getCode());
					dictList.add(dict);
				}
				return dictList;
			}
    	} catch(Exception e) {
    		logger.equals(e.getMessage());
    	}
    	return null;
    }
}
