package com.qiuchen.tianyicrack.Bean;

/**
 * Created by qiuchen on 2018/2/7.
 */

public class CallBalanceBean {

    /**
     * body : {"TSR_RESULT":"0","isqf":"0","TSR_MSG":"成功","totalMoney":"18.91"}
     * TSR_RESULT : 0
     * TSR_CODE : 0
     * TSR_MSG : 成功
     */

    public BodyBean body;
    public String TSR_RESULT;
    public String TSR_CODE;
    public String TSR_MSG;

    public static class BodyBean {
        /**
         * TSR_RESULT : 0
         * isqf : 0
         * TSR_MSG : 成功
         * totalMoney : 18.91
         */

        public String TSR_RESULT;
        public String isqf;
        public String TSR_MSG;
        public String totalMoney;
    }
}
