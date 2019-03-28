package com.mainsoft.mlp.reconciliation.modules.utils;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import java.util.Arrays;

public class XmlOperation {
    public static Document createXmlDoc() throws Exception {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("UTF-8");
        return doc;
    }


    public static String xmldocumentToString(Document document)
            throws Exception {
        String strXml = document.asXML();
        if (!StringUtils.isEmpty(strXml)) {
            strXml = strXml.replaceAll("\r", "").replaceAll("\n", "");
        }
        return strXml;
    }


    public static Document stringToXmldocument(String xmlStr) throws DocumentException {
        xmlStr = removeBOM(xmlStr);
        xmlStr = xmlStr.replaceAll("\r", "").replaceAll("\n", "");
        return DocumentHelper.parseText(xmlStr);
    }

    /**
     * ȥ��UTF-8�ļ�ͷ����
     * @param xmlStr
     * @return
     */
    private static String removeBOM(String xmlStr){
        try {
            byte[] bs = xmlStr.getBytes("UTF-8");
            if(bs!= null){
                int length = bs.length;
                if(length>3){
                    if (bs[0] == -17 && bs[1] == -69 && bs[2] == -65) {
                        byte[] nbs = Arrays.copyOfRange(bs, 3, length-1);
                        xmlStr = new String(nbs,"UTF-8");
                    }
                }
            }
            return xmlStr;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
