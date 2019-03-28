package com.mainsoft.mlp.reconciliation.modules.utils;

import com.mainsoft.mlp.reconciliation.common.utils.StringUtils;
import com.mainsoft.mlp.reconciliation.modules.wechatSDK.HttpClient;
import com.mainsoft.mlp.reconciliation.modules.wechatSDK.SDKConstants;
import com.mainsoft.mlp.reconciliation.modules.wechatSDK.SDKUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;
@Slf4j
public class WechatUtil {
    public static final String DEFAULT_ENCODE = "UTF-8";  //默认utf-8编码

    /**
     * 获取对账的xml
     * @return
     */
    public String getRequestXml(Map<String, String> parameters, String sign){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            sb.append("<"+entry.getKey()+">");
            sb.append(entry.getValue().trim());
            sb.append("</"+entry.getKey()+">");
        }
        sb.append("<"+ SDKConstants.param_sign+">");
        sb.append(sign.trim());
        sb.append("</"+SDKConstants.param_sign+">");

        sb.append("</xml>");
        return sb.toString();
    }
    /**
     * 发送post请求
     * @param urlStr
     * @param data
     * @return
     */
    public String post(String urlStr, String data){
        //发送后台请求数据
        HttpClient hc = new HttpClient(urlStr, 30000, 30000);
        try {
            int status = hc.send(data, DEFAULT_ENCODE);
            if (200 == status) {
                return hc.getResult();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;

    }

    /**
     * 保存文件
     * @param fileData 文件内容
     * @param fileDirectory 保存的目录
     * @param fileName 文件名称
     * @throws Exception
     */
    public static void saveFile(String fileData, String fileDirectory, String fileName) throws Exception{
        saveFile(fileData, fileDirectory, fileName, DEFAULT_ENCODE);
    }

    /**
     * 保存文件
     * @param fileData 文件内容
     * @param fileDirectory 保存的目录
     * @param fileName 文件名称
     * @param charset 文件使用的编码
     * @throws Exception
     */
    public static void saveFile(String fileData, String fileDirectory, String fileName, String charset) throws Exception{
        if(StringUtils.isNotBlank(fileDirectory) && StringUtils.isNotBlank(fileName)){
            String filePath = fileDirectory + File.separator + fileName;
            File direct = new File(fileDirectory);
            if(!direct.exists()){
                direct.mkdirs();
            }

            File genFile = new File(filePath);
            if(genFile.exists()){
                genFile.delete();
            }

            genFile.createNewFile();
            if(fileData != null){
                byte[] data = fileData.getBytes(charset);
                FileOutputStream out = new FileOutputStream(genFile);
                out.write(data, 0, data.length);
                out.flush();
                out.close();
            }
        }else {
            throw new Exception("文件目录或文件名称不能为空");
        }
    }


    /**
     * 从classpath路径下加载文件，返回将文件转为流返回
     * 如果从使用此方法，必须在使用后将inputStream关闭
     * @version 2017-03-24
     */
    public InputStream loadFileFromSrc(String filePath) {
        InputStream in = null;
        try {
            in = SDKUtil.class.getClassLoader().getResourceAsStream(filePath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return in;
    }

    /**
     * 获取文件中的内容
     * @param filePath
     * @return byte[]文件内容
     * @throws Exception
     */
    public static byte[] loadFile(String filePath) throws Exception{
        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }
}
