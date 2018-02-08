package com.qiuchen.tianyicrack.Bean;

import java.util.List;

/**
 * Created by qiuchen on 2018/2/8.
 */

public class MonthZhangDan {
    /**
     * TSR_RESULT : 0
     * TSR_CODE : 0
     * dccBalOweUsed : 5.24
     * TSR_MSG : 成功！
     * kuandai : {"leavel1":[]}
     * guhua : {"leavel1":[]}
     * itv : {"leavel1":[]}
     * shouji : {"leavel1":[{"dccParentClassId":"0","accNbr":" ","productOffName":"手机","dccBillFee":"5.24","leavel2":[{"dccParentClassId":"1000","accNbr":" ","productOffName":" ","dccBillFee":"0.00","dccClassId":"1001","dccBillItemName":"用户号码： 手机：17714602936","dccShowLevel":"2"}],"dccClassId":"1000","dccBillItemName":"手机","leavel3":[{"dccParentClassId":"1001","accNbr":"手机：17714602936","productOffName":" ","dccBillFee":"2.14","dccClassId":"1002","leavel4":[{"dccParentClassId":"1002","accNbr":"手机：17714602936","leavel5":[],"productOffName":" ","dccBillFee":"2.14","dccClassId":"1007","dccBillItemName":"手机上网及无线宽带使用费","dccShowLevel":"4"}],"dccBillItemName":"上网及数据通信费","dccShowLevel":"3"},{"dccParentClassId":"1001","accNbr":"手机：17714602936","productOffName":" ","dccBillFee":"0.10","dccClassId":"1003","leavel4":[{"dccParentClassId":"1003","accNbr":"手机：17714602936","leavel5":[],"productOffName":" ","dccBillFee":"0.10","dccClassId":"1006","dccBillItemName":"国内短信费","dccShowLevel":"4"}],"dccBillItemName":"短信彩信费","dccShowLevel":"3"},{"dccParentClassId":"1001","accNbr":"手机：17714602936","productOffName":" ","dccBillFee":"3.00","dccClassId":"1004","leavel4":[{"dccParentClassId":"1004","accNbr":"手机：17714602936","leavel5":[],"productOffName":" ","dccBillFee":"3.00","dccClassId":"1005","dccBillItemName":"套餐费优惠","dccShowLevel":"4"}],"dccBillItemName":"优惠费用","dccShowLevel":"3"}],"dccShowLevel":"1"}]}
     */

    public String TSR_RESULT;
    public String TSR_CODE;
    public String dccBalOweUsed;
    public String TSR_MSG;
    public KuandaiBean kuandai;
    public GuhuaBean guhua;
    public ItvBean itv;
    public ShoujiBean shouji;

    public static class KuandaiBean {
        public List<?> leavel1;
    }

    public static class GuhuaBean {
        public List<?> leavel1;
    }

    public static class ItvBean {
        public List<?> leavel1;
    }

    public static class ShoujiBean {
        public List<Leavel1Bean> leavel1;

        public static class Leavel1Bean {
            /**
             * dccParentClassId : 0
             * accNbr :
             * productOffName : 手机
             * dccBillFee : 5.24
             * leavel2 : [{"dccParentClassId":"1000","accNbr":" ","productOffName":" ","dccBillFee":"0.00","dccClassId":"1001","dccBillItemName":"用户号码： 手机：17714602936","dccShowLevel":"2"}]
             * dccClassId : 1000
             * dccBillItemName : 手机
             * leavel3 : [{"dccParentClassId":"1001","accNbr":"手机：17714602936","productOffName":" ","dccBillFee":"2.14","dccClassId":"1002","leavel4":[{"dccParentClassId":"1002","accNbr":"手机：17714602936","leavel5":[],"productOffName":" ","dccBillFee":"2.14","dccClassId":"1007","dccBillItemName":"手机上网及无线宽带使用费","dccShowLevel":"4"}],"dccBillItemName":"上网及数据通信费","dccShowLevel":"3"},{"dccParentClassId":"1001","accNbr":"手机：17714602936","productOffName":" ","dccBillFee":"0.10","dccClassId":"1003","leavel4":[{"dccParentClassId":"1003","accNbr":"手机：17714602936","leavel5":[],"productOffName":" ","dccBillFee":"0.10","dccClassId":"1006","dccBillItemName":"国内短信费","dccShowLevel":"4"}],"dccBillItemName":"短信彩信费","dccShowLevel":"3"},{"dccParentClassId":"1001","accNbr":"手机：17714602936","productOffName":" ","dccBillFee":"3.00","dccClassId":"1004","leavel4":[{"dccParentClassId":"1004","accNbr":"手机：17714602936","leavel5":[],"productOffName":" ","dccBillFee":"3.00","dccClassId":"1005","dccBillItemName":"套餐费优惠","dccShowLevel":"4"}],"dccBillItemName":"优惠费用","dccShowLevel":"3"}]
             * dccShowLevel : 1
             */

            public String dccParentClassId;
            public String accNbr;
            public String productOffName;
            public String dccBillFee;
            public String dccClassId;
            public String dccBillItemName;
            public String dccShowLevel;
            public List<Leavel2Bean> leavel2;
            public List<Leavel3Bean> leavel3;

            public static class Leavel2Bean {
                /**
                 * dccParentClassId : 1000
                 * accNbr :
                 * productOffName :
                 * dccBillFee : 0.00
                 * dccClassId : 1001
                 * dccBillItemName : 用户号码： 手机：17714602936
                 * dccShowLevel : 2
                 */

                public String dccParentClassId;
                public String accNbr;
                public String productOffName;
                public String dccBillFee;
                public String dccClassId;
                public String dccBillItemName;
                public String dccShowLevel;
            }

            public static class Leavel3Bean {
                /**
                 * dccParentClassId : 1001
                 * accNbr : 手机：17714602936
                 * productOffName :
                 * dccBillFee : 2.14
                 * dccClassId : 1002
                 * leavel4 : [{"dccParentClassId":"1002","accNbr":"手机：17714602936","leavel5":[],"productOffName":" ","dccBillFee":"2.14","dccClassId":"1007","dccBillItemName":"手机上网及无线宽带使用费","dccShowLevel":"4"}]
                 * dccBillItemName : 上网及数据通信费
                 * dccShowLevel : 3
                 */

                public String dccParentClassId;
                public String accNbr;
                public String productOffName;
                public String dccBillFee;
                public String dccClassId;
                public String dccBillItemName;
                public String dccShowLevel;
                public List<Leavel4Bean> leavel4;

                public static class Leavel4Bean {
                    /**
                     * dccParentClassId : 1002
                     * accNbr : 手机：17714602936
                     * leavel5 : []
                     * productOffName :
                     * dccBillFee : 2.14
                     * dccClassId : 1007
                     * dccBillItemName : 手机上网及无线宽带使用费
                     * dccShowLevel : 4
                     */

                    public String dccParentClassId;
                    public String accNbr;
                    public String productOffName;
                    public String dccBillFee;
                    public String dccClassId;
                    public String dccBillItemName;
                    public String dccShowLevel;
                    public List<?> leavel5;
                }
            }
        }
    }
}
