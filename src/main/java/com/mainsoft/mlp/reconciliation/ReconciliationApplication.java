package com.mainsoft.mlp.reconciliation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@MapperScan("com.mainsoft.mlp.reconciliation.modules.dao")
@EnableScheduling
@EnableConfigurationProperties
public class ReconciliationApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(ReconciliationApplication.class).web(false).run(args);
    }

}

