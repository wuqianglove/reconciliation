/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.mainsoft.mlp.reconciliation.common.persistence.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;
/**
 * 多数据源一期外网查询数据库
 * 标识MyBatis的DAO,方便{@link org.mybatis.spring.mapper.MapperScannerConfigurer}的扫描。
 * @author cuiyl
 * @version 2016-11-4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface MlpOnline1MyBatisDao {
      
      /**
       * The value may indicate a suggestion for a logical component name,
       * to be turned into a Spring bean in case of an autodetected component.
       * @return the suggested component name, if any
       */
      String value() default "";
}