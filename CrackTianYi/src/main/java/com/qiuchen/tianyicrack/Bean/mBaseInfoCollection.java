package com.qiuchen.tianyicrack.Bean;

/**
 * 基本信息聚合,进一步处理服务器返回的信息
 */
public class mBaseInfoCollection {


    /**
     * 鉴权token
     */
    public String token,
    /**
     * 手机号码
     */
    phoneNum,
    /**
     * 城市区域代码
     */
    areaCode,
    /**
     * 卡内剩余话费
     */
    totalMoney,
    /**
     * 本月已使用话费
     */
    dccBillFee,
    /**
     * 持卡人姓名
     */
    customerName,
    /**
     * 持卡人身份证号码
     */
    indentNbr,
    /**
     * 子区域代码,目前用不上
     */
    childAreaCode,
    /**
     * 当前账号鉴权密码
     */
    passwd;
    /**
     * 流量信息
     */
    public FlowInfo flowInfo = new FlowInfo();

    public FlowExpress flowListBean = new FlowExpress();

    public class FlowInfo {
        /**
         * 剩余流量
         */
        public String leftFlow,
        /**
         * 剩余计划流量(不确定是不是这个意思)
         */
        provinceLeftFlow,
        /**
         * 总计划流量
         */
        totalFlow,
        /**
         * 已使用流量
         */
        usedFlow;
    }
}
