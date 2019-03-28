package com.mainsoft.mlp.reconciliation.common.persistence.interceptor;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Maps;
import com.mainsoft.mlp.reconciliation.common.persistence.annotation.Uppercase;
import com.mainsoft.mlp.common.utils.Reflections;

@Intercepts({ 
    @Signature(type=Executor.class,method="update",args={MappedStatement.class,Object.class}),
    @Signature(type=Executor.class,method="query",args={MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class})  
    })
public class UppercaseConverterInterceptor implements Interceptor, Serializable {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -43783654266156835L;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
        Object parameter = invocation.getArgs()[1];  
        if(parameter != null){  
	    	// 拿到该类
			Class<?> clz = parameter.getClass();
			Uppercase uppercase = clz.getAnnotation(Uppercase.class);

			if(uppercase != null){
				String[] uppercaseFields = uppercase.uppercaseField();
				Map<String, Boolean> uppercaseFieldsMap = Maps.newHashMap();
				if(uppercaseFields != null && uppercaseFields.length > 0) {
					for(String fieldName : uppercaseFields) {
						uppercaseFieldsMap.put(fieldName, true);
					}
				}
	    		// Get annotation fields
				Field[] fields = clz.getDeclaredFields();
				for (Field field : fields) {
					// 跳过静态属性
		            String mod = Modifier.toString(field.getModifiers());
		            if (mod.indexOf("static") != -1) continue;
		            
					String className = field.getType().getSimpleName();
					if(className.equalsIgnoreCase("String")) {
		     			if(uppercaseFieldsMap.get(field.getName()) != null) {
		     				logger.debug(field.getName()+":"+(String)Reflections.getFieldValue(parameter, field.getName()));
		     				String value = StringUtils.upperCase((String)Reflections.getFieldValue(parameter, field.getName()), Locale.ENGLISH);
		     				logger.debug(field.getName()+":"+value);
		     				Reflections.setFieldValue(parameter, field.getName(), value);
		     			} 
					}
	     		}
			}
        }
        return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
