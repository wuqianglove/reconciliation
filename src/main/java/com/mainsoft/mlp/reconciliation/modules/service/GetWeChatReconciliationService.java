package com.mainsoft.mlp.reconciliation.modules.service;

import com.mainsoft.mlp.common.utils.DateUtils;
import com.mainsoft.mlp.reconciliation.common.config.Global;
import com.mainsoft.mlp.reconciliation.common.utils.StringUtils;
import com.mainsoft.mlp.reconciliation.modules.entity.PayImportFile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayUnpReconcile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayWcReconcile;
import com.mainsoft.mlp.reconciliation.modules.entity.PayWcReconcileDetail;
import com.mainsoft.mlp.reconciliation.modules.enums.PayImportFileStatusEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PayImportFileTypeEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.PaymentTypeForImportFileEnum;
import com.mainsoft.mlp.reconciliation.modules.enums.WechatBillColumnEnum;
import com.mainsoft.mlp.reconciliation.modules.utils.WechatUtil;
import com.mainsoft.mlp.reconciliation.modules.utils.XmlOperation;
import com.mainsoft.mlp.reconciliation.modules.wechatSDK.SDKConstants;
import com.mainsoft.mlp.reconciliation.modules.wechatSDK.SDKUtil;
import com.unionpay.mpi.LogUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.*;

public class GetWeChatReconciliationService {
    @Value("${wechat.key}") private String weChatKey;
    @Value("${wechat.appid}") private String weChatAppId;
    @Value("${wechat.mchid}") private String weChatMacId;


    @Value("${wechat.district.code}") private String districtCode;
    @Value("${wechat.district.name}") private String districtName;

    @Value("${ wechat.file.url}") private String fileUrl;

    private static final String wechatUrl = "https://api.mch.weixin.qq.com/pay/downloadbill"; // 微信对账单地址
    private static final String DATEFORMAT_PATTERN = "yyyyMMdd"; // 日期格式

    @Autowired
    private PayImportFileService payImportFileService;

    /**
     * 请求微信对账接口
     */
    public void requestWeChat() {
        //2019.03.27 guodongyu 采用springboot读取配置文件的方式获得微信所属配置
        Date yesterday = new Date(new Date().getTime() - 86400000L); // 昨天的时间
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put(SDKConstants.param_appid, weChatAppId); // 公众账号id
        parameters.put(SDKConstants.param_bill_date, DateUtils.formatDate(yesterday, DATEFORMAT_PATTERN)); // 下载对账单的日期，格式：20140603
        parameters.put(SDKConstants.param_bill_type, "ALL"); // 账单类型
        // ALL，返回当日所有订单信息，默认值
        // 、
        // SUCCESS，返回当日成功支付的订单、REFUND，返回当日退款订单、RECHARGE_REFUND，返回当日充值退款订单（相比其他对账单多一栏“返还手续费”）
        // data.put("tar_type", "GZIP"); //压缩账单，如果传GZIP，返回压缩后的对账单
        WechatUtil wechatUtil = new WechatUtil();
        // 循环分支局的商户号，获取对账单
        parameters.put(SDKConstants.param_mch_id, weChatMacId); // 商户号
        parameters.put(SDKConstants.param_nonce_str,
                SDKUtil.create_nonce_str()); // 随机字符串
        String sign = new SDKUtil().createSign(parameters); // 生成签名

        try {
            String result = wechatUtil.post(wechatUrl, wechatUtil.getRequestXml(parameters, sign)); // 调用微信接口，获取返回值
            // 如果调用接口返回值为空
            if (StringUtils.isEmpty(result)) {
                getBillFail(weChatMacId,districtName, "获取失败，返回结果为空");
            } else {
                /** result有两种可能性，一种为xml格式，一种为文本格式，如果为xml格式，代表获取对账单失败 */
                try {
                    // 解析返回的数据
                    Document document = XmlOperation.stringToXmldocument(result);
                    Element element = document.getRootElement(); // 获取跟节点
                    String returnCode = element.element("return_code").getTextTrim(); // 返回状态码
                    if (returnCode != null && (!StringUtils.isEmpty(returnCode)) && returnCode.equals("FAIL")) {
                        getBillFail(weChatMacId,districtName, element.element("return_msg").getTextTrim());
                    }
                } catch (DocumentException e) {
                    // 将对账单信息保存至本地
                    String fileName = saveReconciliationData(weChatMacId, result, parameters.get(SDKConstants.param_bill_date));
                    //将对账信息保存到数据库中，2019.03.28 guodongyu   这里保存对账信息可能不合理
                    PayImportFile payImportFile = new PayImportFile();
                    //设置对账文件类型
                    payImportFile.setType(PayImportFileTypeEnum.WECHAT_PAY.getCode());
                    payImportFile.setName(fileName);
                    payImportFile.setContent(result);
                    payImportFile.setStatus(PayImportFileStatusEnum.NOT_HANDLE.getCode());  //默认未处理状态
                    Date today = new Date();
                    payImportFile.setCreateTime(today);  //设置生成时间
                    payImportFile.setImportTime(today);  //设置入库时间
                    parameters.get(SDKConstants.param_bill_date);
                    payImportFile.setSettleDate(DateUtils.parseDate(parameters.get(SDKConstants.param_bill_date)));//设置账单的清算日期

                    /**
                     * 设置业务类型为港建费（暂时微信只考虑港建费情况）
                     */
                    payImportFile.setPaymentType(PaymentTypeForImportFileEnum.PORTPAYMENT.getCode());  //设置缴费类型（01：港建费，02：规费）
                    /**
                     * TODO 在银行对账表（内网）中添加一个清算时间字段
                     */
                    //设置unionPay对账信息的参数
                    PayWcReconcile payWcReconcile = new PayWcReconcile();
                    payWcReconcile.setMerchant(weChatMacId);
                    payWcReconcile.setBillType(parameters.get(SDKConstants.param_bill_type));
                    payWcReconcile.setSuccess(Integer.parseInt(Global.NO));
                    //银联在线对账实体中交易日期就是清算日期，所以在新建（银行在线对账）表中不必要添加清算日期字段
                    payWcReconcile.setBillDate(parameters.get(SDKConstants.param_bill_date));
                    //解析微信对账文件字符串，装载统计数据payWcReconcile  以及对账文件详情PayWcReconcileDetail
                    parseWeChatReconcileFile(result,payWcReconcile);
                    //保存微信在线对账文件
                    payImportFileService.saveWcPayImportFile(payImportFile, payWcReconcile);
                }
            }
        } catch (Exception e) {
            LogUtil.writeErrorLog("商户号为" + weChatMacId + "的分支局，微信支付账单信息请求发生异常：", e);
        }

    }

    private void parseWeChatReconcileFile(String result, PayWcReconcile payWcReconcile) {
        List<PayWcReconcileDetail> payWcReconcileDetailList = new ArrayList<PayWcReconcileDetail>();
        String[] recordArray = result.split("\n");// 行数组
        int record_count = recordArray.length;// 总行数
        /**
         * 第一行为表头
         * 从第二行起，为数据记录，各参数以逗号分隔，参数前增加`符号，为标准键盘1左边键的字符，字段顺序与表头一致。
         * 倒数第二行为订单统计标题，最后一行为统计数据
         */
        //拼装对账文件详情信息
        /*
         * 列与它对应的index
         * 0:交易时间；1:公众账号ID；2.商户号；3.子商户号；4.设备号；5.微信订单号；6.商户订单号；
         * 7.用户标识；8.交易类型；9.交易状态；10.付款银行；11.货币种类；12.总金额；13.代金券或立减优惠金额；
         * 14.微信退款单号；15.商户退款单号；16.退款金额；17.代金券或立减优惠退款金额；18.退款类型；
         * 19.退款状态；20.商品名称；21.商户数据包；22.手续费；23.费率
         */
        for (int i = 1; i < record_count - 2; i++) {
            PayWcReconcileDetail payWcReconcileDetail = new PayWcReconcileDetail();
            String[] fieldArray = recordArray[i].split(","); // 参数分组
            payWcReconcileDetail.setPayTime(fieldArray[0].substring(1));
            payWcReconcileDetail.setMerchant(fieldArray[2].substring(1));
            payWcReconcileDetail.setOrderCode(fieldArray[6].substring(1));
            payWcReconcileDetail.setPaymentBill(fieldArray[21].substring(1));
            payWcReconcileDetail.setTransType(fieldArray[8].substring(1));
            payWcReconcileDetail.setTransStatus(fieldArray[9].substring(1));
            payWcReconcileDetail.setTransAmount(fieldArray[12].substring(1));
            payWcReconcileDetailList.add(payWcReconcileDetail);
        }
        payWcReconcile.setPayWcReconcileDetailList(payWcReconcileDetailList);
        String[] fileTile = recordArray[1].split(",");
        payWcReconcile.setPayTotalCount(Integer.parseInt(fileTile[0].substring(1)));
        payWcReconcile.setPayTotalAmount(new BigDecimal(fileTile[1].substring(1)));
        payWcReconcile.setRefundAmount(new BigDecimal(fileTile[2].substring(1)));
        payWcReconcile.setDiscountsrefundAmount(new BigDecimal(fileTile[3].substring(1)));
        payWcReconcile.setServiceAmount(new BigDecimal(fileTile[4].substring(1)));

    }

    /**
     * 获取对账单失败
     *
     *
     * @param errorMessage
     *            失败的返回信息
     */
    public void getBillFail(String weChatMacId,String districtName, String errorMessage) {
        // TODO记录失败的商户号和错误信息
        LogUtil.writeErrorLog("商户号:" + weChatMacId + "，分支局："
                + districtName + "，微信支付账单信息获取失败，错误原因："
                + errorMessage);
    }

    /**
     * 将请求结果保存至本地文件
     *
     * @param
     * @text 请求返回值
     * @requestDate 对账单的时间 格式yyMMdd
     */
    public String saveReconciliationData(String merChantCode, String text,
                                       String billDate) {
        try {
            WechatUtil.saveFile(text, fileUrl + billDate, merChantCode + ".txt");
        } catch (Exception e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        }
        return merChantCode+"@"+billDate+".txt";
    }
}
