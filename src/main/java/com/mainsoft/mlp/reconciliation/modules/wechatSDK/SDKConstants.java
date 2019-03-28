package com.mainsoft.mlp.reconciliation.modules.wechatSDK;

public class SDKConstants {
    //	字段名 	变量名 	必填 	类型 	示例值 	描述
//	公众账号ID 	appid 	是 	String(32) 	wx8888888888888888 	微信分配的公众账号ID（企业号corpid即为此appId）
//	商户号 	mch_id 	是 	String(32) 	1900000109 	微信支付分配的商户号
//	设备号 	device_info 	否 	String(32) 	013467007045764 	微信支付分配的终端设备号
//	随机字符串 	nonce_str 	是 	String(32) 	5K8264ILTKCH16CQ2502SI8ZNMTM67VS 	随机字符串，不长于32位。推荐随机数生成算法
//	签名 	sign 	是 	String(32) 	C380BEC2BFD727A4B6845133519F3AD6 	签名，详见签名生成算法
//	签名类型 	sign_type 	否 	String(32) 	HMAC-SHA256 	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
//	对账单日期 	bill_date 	是 	String(8) 	20140603 	下载对账单的日期，格式：20140603
//	账单类型 	bill_type 	是 	String(8) 	ALL
//
//	ALL，返回当日所有订单信息，默认值
//
//	SUCCESS，返回当日成功支付的订单
//
//	REFUND，返回当日退款订单
//
//	RECHARGE_REFUND，返回当日充值退款订单（相比其他对账单多一栏“返还手续费”）
//	压缩账单 	tar_type 	否 	String(8) 	GZIP 	非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
    /** 公众账号ID */
    public static final String param_appid = "appid";
    /** 商户号 */
    public static final String param_mch_id = "mch_id";
    /** 设备号 */
    public static final String param_device_info = "device_info";
    /** 随机字符串 */
    public static final String param_nonce_str = "nonce_str";
    /** 签名 */
    public static final String param_sign = "sign";
    /** 签名类型 */
    public static final String param_sign_type = "sign_type";
    /** 对账单日期 */
    public static final String param_bill_date = "bill_date";
    /** 账单类型 */
    public static final String param_bill_type = "bill_type";
    /** 压缩账单 */
    public static final String param_tar_type = "tar_type";
}
