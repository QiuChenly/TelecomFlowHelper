package com.qiuchen.tianyicrack.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qiuchen on 2018/2/7.
 */

public class userInfoBean {

    /**
     * managerAccount : 0
     * mobileInfo : [{"prodId":"152287363266","mobileNum":"17714602936"}]
     * iTVInfo : []
     * broadbandInfo : []
     * phoneInfo : []
     * xltInfo : []
     * c+wInfo : []
     * smState : 0
     * state34G : 1
     * zqState : 1
     * customerName : 陈春
     * indentNbr : 320924197212102954
     * indentNbrType : 1
     * accNbr : 17714602936
     * areaCode : 0515
     * childAreaCode : 39
     * payMethod : 1
     * TSR_RESULT : 0
     * TSR_CODE : 0000
     * TSR_MSG :
     * token : c8ce46cdb22044aca0681db000a5a7ad
     * cacheState : 0
     * JSZT_MEMCACHED_STATUS : 1
     */

    public String managerAccount;
    public String smState;
    public String state34G;
    public String zqState;
    public String customerName;
    public String indentNbr;
    public String indentNbrType;
    public String accNbr;
    public String areaCode;
    public String childAreaCode;
    public String payMethod;
    public String TSR_RESULT;
    public String TSR_CODE;
    public String TSR_MSG;
    public String token;
    public String cacheState;
    public String JSZT_MEMCACHED_STATUS;
    public List<MobileInfoBean> mobileInfo;
}
