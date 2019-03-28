package com.mainsoft.mlp.reconciliation.modules.enums;

public enum WechatBillColumnEnum {
    /*
     * 列与它对应的index
     * 0:交易时间；1:公众账号ID；2.商户号；3.子商户号；4.设备号；5.微信订单号；6.商户订单号；
     * 7.用户标识；8.交易类型；9.交易状态；10.付款银行；11.货币种类；12.总金额；13.代金券或立减优惠金额；
     * 14.微信退款单号；15.商户退款单号；16.退款金额；17.代金券或立减优惠退款金额；18.退款类型；
     * 19.退款状态；20.商品名称；21.商户数据包；22.手续费；23.费率
     */
    /** 交易时间 */
    transTime(0),
    /** 商户号 */
    MerchantNumber(2),
    /** 收款流水号，这里用我们自己的订单编码  */
    Serial(6),
    /** 缴费单号，这里是订单编码，我们需要根据订单编码来查询缴费单编码 */
    PaymentCode(6),
    /** 交易类型 */
    transType(8),
    /** 交易状态 */
    transStatus(9),
    /** 总金额 */
    Revenue(12);

    private int code;

    WechatBillColumnEnum(Integer code) {
        this.code=code;
    }

    public Integer getCode(){
        return code;
    }
}
