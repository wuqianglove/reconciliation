package com.mainsoft.mlp.reconciliation;

import com.mainsoft.mlp.reconciliation.modules.entity.PayImportFile;
import com.mainsoft.mlp.reconciliation.modules.service.PayImportFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImportFileTest {

    @Autowired
    private PayImportFileService payImportFileService;


    @Test
    public void test(){
        String pfxkeyfile = "classpath:/certificate/payment/unionpay/B2B/acp_test_sign.pfx";
        InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(pfxkeyfile);
        System.out.println(fis);




        }

    }


