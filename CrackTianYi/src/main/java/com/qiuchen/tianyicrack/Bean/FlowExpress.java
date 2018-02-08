package com.qiuchen.tianyicrack.Bean;

import java.util.ArrayList;

/**
 * Created by qiuchen on 2018/2/8.
 */

public class FlowExpress {

    /**
     * TSR_RESULT : 0
     * TSR_CODE : 0
     * TSR_MSG : 成功
     * getFlowList : [{"sendNbr":"17327298221","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2018/02/03","myGet":"G"},{"sendNbr":"18036613302","isGet":"1","offerId":"105781","flowName":"500","endTime":"2018/01/22","myGet":"G"},{"sendNbr":"18014103093","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/12/12","myGet":"G"},{"sendNbr":"18961120766","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/11/24","myGet":"G"},{"sendNbr":"18914370746","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/11/17","myGet":"G"},{"sendNbr":"17314050415","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/11/08","myGet":"G"},{"sendNbr":"15380790955","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/10/30","myGet":"G"},{"sendNbr":"18905249284","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/10/23","myGet":"G"},{"sendNbr":"18951027161","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/10/16","myGet":"G"},{"sendNbr":"18015114563","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/10/08","myGet":"G"},{"sendNbr":"17798730245","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/09/29","myGet":"G"},{"sendNbr":"13357885606","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/09/22","myGet":"G"},{"sendNbr":"18914011506","isGet":"1","offerId":"105781","flowName":"500","endTime":"2017/09/12","myGet":"G"},{"sendNbr":"18101411801","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/09/03","myGet":"G"},{"sendNbr":"17712815153","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/08/26","myGet":"G"},{"sendNbr":"18914011506","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/08/18","myGet":"G"},{"sendNbr":"18051160490","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/08/11","myGet":"G"},{"sendNbr":"18101411801","isGet":"1","offerId":"106024","flowName":"1024","endTime":"2017/08/03","myGet":"G"}]
     */

    /**
     * 请求成功为0
     */
    public String TSR_RESULT;
    /**
     * 请求成功为0
     */
    public String TSR_CODE;
    /**
     * 请求结果,默认为"成功"
     */
    public String TSR_MSG;
    /**
     * 返回的Flow列表
     */
    public ArrayList<GetFlowListBean> getFlowList;

    public static class GetFlowListBean {
        /**
         * 赠送号码(17327298221)
         */
        public String sendNbr;
        /**
         * 是否已经领取此流量信息(1)
         */
        public String isGet;
        /**
         * 本次流量ExpressOfferID(106024)
         */
        public String offerId;
        /**
         * 流量大小(1024)
         */
        public String flowName;
        /**
         * 失效时间(2018/02/03)
         */
        public String endTime;
        /**
         * 未知参数,已领取时显示"G"
         */
        public String myGet;
    }
}
