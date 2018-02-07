package com.qiuchen.tianyicrack.Bean;

/**
 * Created by qiuchen on 2018/2/7.
 */

public class CurrAcuBean {

    /**
     * body : {"TSR_RESULT":"0","leftVoice":0,"TSR_MSG":"成功","leftFlow":"530","totalVoice":0,"cityLeftFlow":"0","provinceLeftFlow":"530","totalFlow":"1599","usedFlow":"1069","neverLimitFlag":"000","usedVoice":0,"countryLeftFlow":"0"}
     * TSR_RESULT : 0
     * TSR_CODE : 0
     * TSR_MSG : 成功
     */

    public BodyBean body;
    public String TSR_RESULT;
    public String TSR_CODE;
    public String TSR_MSG;

    public class BodyBean {
        /**
         * TSR_RESULT : 0
         * leftVoice : 0
         * TSR_MSG : 成功
         * leftFlow : 530
         * totalVoice : 0
         * cityLeftFlow : 0
         * provinceLeftFlow : 530
         * totalFlow : 1599
         * usedFlow : 1069
         * neverLimitFlag : 000
         * usedVoice : 0
         * countryLeftFlow : 0
         */

        public String TSR_RESULT;
        public int leftVoice;
        public String TSR_MSG;
        public String leftFlow;
        public int totalVoice;
        public String cityLeftFlow;
        public String provinceLeftFlow;
        public String totalFlow;
        public String usedFlow;
        public String neverLimitFlag;
        public int usedVoice;
        public String countryLeftFlow;
    }
}
