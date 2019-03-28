
package com.mainsoft.mlp.reconciliation.common.webservice.osb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *       &lt;sequence&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "serviceEntranceResult"
})
@XmlRootElement(name = "ServiceEntranceResponse")
public class ServiceEntranceResponse {

    @XmlElement(name = "ServiceEntranceResult")
    protected byte[] serviceEntranceResult;

    /**
     * 获取serviceEntranceResult属性的值。
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getServiceEntranceResult() {
        return serviceEntranceResult;
    }

    /**
     * 设置serviceEntranceResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setServiceEntranceResult(byte[] value) {
        this.serviceEntranceResult = value;
    }

}
