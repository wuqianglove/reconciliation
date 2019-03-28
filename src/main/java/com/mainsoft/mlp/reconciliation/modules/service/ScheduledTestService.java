package com.mainsoft.mlp.reconciliation.modules.service;



import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component

public class ScheduledTestService {
    //输出时间格式
    private static final SimpleDateFormat format = new SimpleDateFormat("HH(hh):mm:ss S");

    public void test1(){
    log.info("===============定时启动");
        System.out.println("定时任务执行，现在时间是 : "+format.format(new Date()));


    }

}
