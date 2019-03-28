package com.mainsoft.mlp.reconciliation.modules.utils;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mainsoft.mlp.common.utils.DateUtils;
import com.mainsoft.mlp.common.utils.StringUtils;
import com.mainsoft.mlp.reconciliation.modules.unionpaySDK.SDKConstants;
import com.mainsoft.mlp.reconciliation.modules.unionpaySDK.SecureUtil;

public class UnionPayUtils {
	//默认配置的是UTF-8
	public static final String encoding_UTF8 = "UTF-8";  //编码格式
	//全渠道固定值
	public static final String version = "5.0.0";  //版本号
	//签名证书类型
	public static final String signCertType= "PKCS12";
	
	// 商户发送交易时间 格式:YYYYMMDDhhmmss
	public static String getCurrentTime() {
		return DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
	}
	
	// AN8..40 商户订单号，不能含"-"或"_"
	public static String getOrderId() {
		return DateUtils.formatDate(new Date(), "yyyyMMddHHmmssS");
	}

	static{
		/**
		 * 添加签名，验签，加密算法提供者
		 */
		if (Security.getProvider("BC") == null) {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} else {
			Security.removeProvider("BC"); //解决eclipse调试时tomcat自动重新加载时，BC存在不明原因异常的问题。
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		}
	}
	
	/**
	 * 验证签名(SHA-1摘要算法)<br>
	 *
	 * @param encoding 上送请求报文域encoding字段的值<br>
	 * @param validateCertPath  用来验签的公钥路径 
	 * @return true 通过 false 未通过<br>
	 */
	public  boolean validate(Map<String, String> rspData, String encoding, String validateCertPath) {
		if (StringUtils.isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		String stringSign = rspData.get(SDKConstants.param_signature);  //获取签名

		// 将Map信息转换成key1=value1&key2=value2的形式
		String stringData = UnionPayUtils.coverMap2String(rspData);
		
		try {
			// 验证签名需要用银联发给商户的公钥证书.
			return SecureUtil.validateSignBySoft(getValidateKey(validateCertPath), SecureUtil.base64Decode(stringSign.getBytes(encoding)), SecureUtil.sha1X16(stringData,encoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 将Map中的数据转换成按照Key的ascii码排序后的key1=value1&key2=value2的形式 不包含签名域signature
	 * 
	 * @param data
	 *            待拼接的Map数据
	 * @return 拼接好后的字符串
	 */
	public static String coverMap2String(Map<String, String> data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			if (SDKConstants.param_signature.equals(en.getKey().trim())) {
				continue;
			}
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			sf.append(en.getKey() + SDKConstants.EQUAL + en.getValue()
					+ SDKConstants.AMPERSAND);
		}
		return sf.substring(0, sf.length() - 1);
	}
	
	/**
	 * 获取证书id
	 * @return 证书id
	 */
	public static String getSignCertId(KeyStore keyStore){
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取签名证书私钥（单证书模式）
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 */
	public static PrivateKey getSignCertPrivateKey(KeyStore keyStore, String keypwd) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
		Enumeration<String> aliasenum = keyStore.aliases();
		String keyAlias = null;
		if (aliasenum.hasMoreElements()) {
			keyAlias = aliasenum.nextElement();
		}
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keypwd.toCharArray());
		return privateKey;
	}
	
	/**
	 * 获取签名证书公钥（单证书模式）
	 * @return
	 * @throws FileNotFoundException 
	 * @throws NoSuchProviderException 
	 * @throws CertificateException 
	 */
	public  PublicKey getValidateKey(String certPath) throws CertificateException, NoSuchProviderException, FileNotFoundException {
		X509Certificate cf = getValidateCert(certPath);
		return cf.getPublicKey();
	}
	
	public  X509Certificate getValidateCert(String certPath) throws CertificateException, NoSuchProviderException, FileNotFoundException{
		CertificateFactory cf = null;
		//FileInputStream in = null;
		cf = CertificateFactory.getInstance("X.509", "BC");
		//File file = new File(certPath);
        InputStream in =this.getClass().getResourceAsStream(certPath);
		//in = new FileInputStream(file.getAbsolutePath());
		
		return (X509Certificate) cf.generateCertificate(in);
	}
	
	/**
	 * 生成签名值(SHA1摘要算法)
	 * 
	 * @param data 待签名数据Map键值对形式
	 * @param encoding 编码
	 * @param keyStore 证书对象
	 * @param keypwd 证书密码
	 * @return 签名是否成功
	 */
	public static Map<String, String> sign(Map<String, String> data, String encoding, KeyStore keyStore, String keypwd) {
		if (StringUtils.isBlank(encoding)) {
			encoding = "UTF-8";
		}
		
		// 设置签名证书序列号
		data.put(SDKConstants.param_certId, getSignCertId(keyStore));
		
		// 将Map信息转换成key1=value1&key2=value2的形式
		String stringData = UnionPayUtils.coverMap2String(data);
		//使用base64进行编码
		byte[] byteSign = null;
		String stringSign = null;
		try {
			// 通过SHA1进行摘要并转16进制
			byte[] signDigest = SecureUtil.sha1X16(stringData, encoding);
			byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(getSignCertPrivateKey(keyStore, keypwd), signDigest));
			stringSign = new String(byteSign);
			// 设置签名域值
			data.put(SDKConstants.param_signature, stringSign);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取证书文件并转为证书对象
	 * 
	 * @param pfxkeyfile
	 *            证书文件名
	 * @param keypwd
	 *            证书密码
	 *
	 * @return 证书对象
	 * @throws IOException 
	 */
	public KeyStore getKeyInfo(String pfxkeyfile, String keypwd) throws IOException {
		String type = signCertType;  //证书类型
        InputStream fis =this.getClass().getResourceAsStream(pfxkeyfile);
		try {
			KeyStore ks = KeyStore.getInstance(type, "BC");
			char[] nPassword = null;
			nPassword = null == keypwd || "".equals(keypwd.trim()) ? null: keypwd.toCharArray();
			if (null != ks) {
				ks.load(fis, nPassword);
			}
			return ks;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(null!=fis)
				fis.close();
		}
	}


    /**
	 * 组装请求，返回报文字符串用于显示
	 * @param data
	 * @return
	 */
    public static String genHtmlResult(Map<String, String> data){

    	TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			String key = en.getKey(); 
			String value =  en.getValue();
			if("respCode".equals(key)){
				sf.append("<b>"+key + SDKConstants.EQUAL + value+"</br></b>");
			}else
				sf.append(key + SDKConstants.EQUAL + value+"</br>");
		}
		return sf.toString();
    }
    /**
	 * 功能：解析全渠道商户对账文件中的ZM文件并以List<Map>方式返回
	 * 适用交易：对账文件下载后对文件的查看
	 * @param filePath ZM文件全路径
	 * @return 包含每一笔交易中 序列号 和 值 的map序列
	 */
	public static List<Map<Integer, String>> parseZMFile(String filePath){
		int lengthArray[] = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,131};
		return parseFile(filePath,lengthArray);
	}
	
	/**
	 * 功能：解析全渠道商户对账文件中的ZME文件并以List<Map>方式返回
	 * 适用交易：对账文件下载后对文件的查看
	 * @param filePath ZME文件全路径
	 * @return 包含每一笔交易中 序列号 和 值 的map序列
	 */
	public static List<Map<Integer, String>> parseZMEFile(String filePath){
		int lengthArray[] = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,131};
		return parseFile(filePath,lengthArray);
	}
	
	/**
	 * 功能：解析全渠道商户 ZM,ZME对账文件
	 * @param filePath
	 * @param lengthArray 参照《全渠道平台接入接口规范 第3部分 文件接口》 全渠道商户对账文件 6.1 ZM文件和6.2 ZME 文件 格式的类型长度组成int型数组
	 * @return
	 */
	 private static List<Map<Integer, String>> parseFile(String filePath,int lengthArray[]){
	 	List<Map<Integer, String>> ZmDataList = Lists.newArrayList();
	 	try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	//解析的结果MAP，key为对账文件列序号，value为解析的值
        		 	Map<Integer,String> ZmDataMap = new LinkedHashMap<Integer,String>();
                    //左侧游标
                    int leftIndex = 0;
                    //右侧游标
                    int rightIndex = 0;
                    for(int i=0;i<lengthArray.length;i++){
                    	rightIndex = leftIndex + lengthArray[i];
                    	String filed = lineTxt.substring(leftIndex,rightIndex);
                    	leftIndex = rightIndex+1;
                    	ZmDataMap.put(i, filed);
                    }
                    ZmDataList.add(ZmDataMap);
                }
                read.close();
	        }else{
	            System.out.println("找不到指定的文件");
	        }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return ZmDataList;	
	}

	 /**
	  * 解析zip文件，不需要文件地址 只需要zip流
	  * <p>解析并不会在本地创建文件，如需创建文件，另用方法</p>
	  * @param fileArray
	  *
	  * @return
	  */
	public static Map<String, String> parseZMZipFile(byte[] fileArray){
		Map<String, String> resultMap = Maps.newHashMap();
		String content = null; 
		ZipInputStream zipIn = null;
		try {
           zipIn = new ZipInputStream(new ByteArrayInputStream(fileArray));
           ZipEntry zipEn = null;
   			while ((zipEn = zipIn.getNextEntry()) != null) {
               if (!zipEn.isDirectory()) { // 判断此zip项是否为目录
            	   if(zipEn.getName().toUpperCase().startsWith("INN")){
            		   resultMap.put("fileName", zipEn.getName());
            		   content = new String(IOUtils.toByteArray(zipIn), "utf-8");
            		   zipIn.closeEntry();// 关闭当前打开的项
            		   break;
            	   }
        	   }
               zipIn.closeEntry();// 关闭当前打开的项
   			}
        } catch (Exception e) {
           e.printStackTrace();
        }
		
		resultMap.put("content", content);
		
		return resultMap;
	}
	 
	/**
	 * 解析对账文件
	 * @param content
	 * @return
	 */
	public static List<Map<Integer, String>> parseReconciliationFile(String content){
		int lengthArray[] = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,131};
		List<Map<Integer, String>> ZmDataList = new ArrayList<Map<Integer,String>>();
		
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes()), "utf-8"));
			String lineTxt = null;
	        while((lineTxt = bufferedReader.readLine()) != null){
	        	//解析的结果MAP，key为对账文件列序号，value为解析的值
			 	Map<Integer,String> ZmDataMap = new LinkedHashMap<Integer,String>();
	            //左侧游标
	            int leftIndex = 0;
	            //右侧游标
	            int rightIndex = 0;
	            for(int i=0;i<lengthArray.length;i++){
	            	rightIndex = leftIndex + lengthArray[i];
	            	String filed = lineTxt.substring(leftIndex,rightIndex);
	            	leftIndex = rightIndex+1;
	            	ZmDataMap.put(i, filed);
	            }
	            ZmDataList.add(ZmDataMap);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	
        
        return ZmDataList;
	}
	 
}
