package com.qiuchen.tianyicrack.Bean;

/**
 * Created by qiuchen on 2018/2/14.
 */

public class FlowExpressOnlineBean {
    /**
     * 返回的状态码
     * -1 = 无法识别URL信息
     * 0 = 微信分享
     * 1 = 掌厅分享
     */
    public int retCode;
    /**
     * 流量分享者幸运儿昵称
     */
    public String nickName;
    /**
     * 幸运儿手机号码
     */
    public String mainNum;
    /**
     * 流量包大小
     */
    public String flowSize;
    /**
     * 关键信息1
     */
    public String rowID;
    /**
     * 关键信息2
     */
    public String ztInterSource;

    public int lastBag;
}
