package com.qiuchen.tianyicrack.TianYiApi

import com.google.gson.Gson
import com.qiuchen.jingyi.nativeHttp.nHttp
import com.qiuchen.tianyicrack.Bean.FlowExpressOnlineBean
import com.qiuchen.tianyicrack.Bean.FlowExpressUserInfo
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern

/**
 * Created by qiuchen on 2018/2/6.
 */
class HttpApi {
    companion object {
        fun getData(string: String): String {
            return "para=" + DesEncrypt(true).encrypt(string)
        }

        fun getAndroidID(num: String): String {
//            return getMD5(System.currentTimeMillis().toString())
            return getMD5(num)
        }

        fun getMD5(string: String): String {
            val md5 = MessageDigest.getInstance("MD5")
            val arr = md5.digest(string.toByteArray())
            val sb = StringBuilder()

            for (a in arr) {
                val i: Int = a.toInt()
                if (i and 0xff < 0x10) {
                    sb.append("0")
                }
                sb.append(Integer.toHexString(i and 0xff))
            }
            return sb.toString()
        }


        /**
         * 执行Http请求
         * @param X_Method 回调的XMethod子类实例
         * @return 返回nHttp原生返回数据类实例
         */
        fun exec(X_Method: XMethod): nHttp {
            return nHttp.Builder(X_Method.url)
                    .setPostData(getData(X_Method.data))
                    .setRequestHeader("Accept-Charset: UTF-8\n" +
                            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" +
                            "User-Agent: Dalvik/9.5.0 (Linux; U; Android 9.0.1; iPhone 9S Build/LMY99Z)")
                    .Request()
        }

        /**
         * 初始化流量快递分享网址信息
         * @param string 网址
         * @return 返回信息聚合
         */
        fun InitFlowExpressSharedInfo(string: String): FlowExpressOnlineBean {
            //302
            // http://wx.go189.cn/tysh/pages/yaoyiyao/receiveBigFLow3.html?ztInterSource=200777&platform=wap&rowId=1f3b35ea2cd740b892b99e6f5421f658&flowNumber=5120
            val mFlowExpressOnline = FlowExpressOnlineBean()
            val isWX = string.contains("wx.go189.cn")
            val isZT = string.contains("http://url.cn")
            var redirectUrl = string
            if (!isWX && !isZT) mFlowExpressOnline.retCode = -1
            mFlowExpressOnline.retCode = if (isWX) 0 else 1
            if (isZT) {
                //需要进行302跳转拿到关键信息
                val s = nHttp.Builder(string)
                        .setRequestHeader("Accept-Charset: UTF-8\n" +
                                "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" +
                                "User-Agent: Dalvik/9.5.0 (Linux; U; Android 9.0.1; iPhone 9S Build/LMY99Z)")
                        .Request()
                if (s.getStatusCode() == 302) {
                    redirectUrl = s.getRedirectURL()!!
                } else mFlowExpressOnline.retCode = -1
            }

            //开始匹配数据
            //http://wx.go189.cn/tysh/pages/yaoyiyao/receiveBigFLow3.html?ztInterSource=200777&platform=wap&rowId=1f3b35ea2cd740b892b99e6f5421f658&flowNumber=5120
            val p = Pattern.compile("ztInterSource=([0-9]{6,})&platform=wap&rowId=(.*?)&flowNumber=([0-9]{4,})")
            val m = p.matcher(redirectUrl + "&")
            if (m.find()) {
                mFlowExpressOnline.flowSize = m.group(3)
                mFlowExpressOnline.rowID = m.group(2)
                mFlowExpressOnline.ztInterSource = m.group(1)
            } else mFlowExpressOnline.retCode = -1

            //开始初始化用户信息
            val url = "http://wx.go189.cn/tysh/interface/doAjax.do"
            val data = "url=http%3A%2F%2F61.160.137.141%2Fjszt%2FflowSend%2FflowAndTimesLeft&para=rowId%3D${mFlowExpressOnline.rowID}%3BactionCode%3DjsztActionCode_flowSendflowAndTimesLeft%3BchannelCode_common%3D200777%3BpubAreaCode%3D025%3BpushUserId%3Djszt_${System.currentTimeMillis()}%3BcoachUser%3D%3BuserLogAccNbrType%3D%3BuserLogAccNbr%3D%3BuserTokenAccNbrType%3D2%3BztVersion%3D3.1.0%3BztInterSource%3D${mFlowExpressOnline.ztInterSource}%3BpubToken%3Dundefined%3B"
            val s = nHttp.Builder(url)
                    .setPostData(data)
                    .setRequestHeader("Accept-Charset: UTF-8\n" +
                            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" +
                            "User-Agent: Dalvik/9.5.0 (Linux; U; Android 9.0.1; iPhone 9S Build/LMY99Z)\n" +
                            "X-Requested-With:XMLHttpRequest\n"
                            + redirectUrl)
                    .Request()

            if (s.getStatusCode() == 200) {
                val g = Gson().fromJson(s.toString(Charset.defaultCharset()), FlowExpressUserInfo::class.java)
                mFlowExpressOnline.mainNum = g.userPhoneEncrypt
                mFlowExpressOnline.nickName = g.nickName
                mFlowExpressOnline.lastBag = g.timesLeft
            } else mFlowExpressOnline.retCode = -1
            return mFlowExpressOnline
        }

        /**
         * 领取流量 QiuChenly 2018.2.14
         * @param num 手机号码
         * @param rowID 礼包唯一编号
         * @return 成功返回真,失败返回假
         */
        fun Build_GetFlowByUser(num: String,
                                rowID: String): Boolean {
            val a = nHttp.Builder("http://wx.go189.cn/tysh/interface/doAjax.do")
                    .setPostData("url=http%3A%2F%2F61.160.137.141%2Fjszt%2FflowSend%2FsendDelivery&para=rowId%3D$rowID%3BaccNbr%3D$num%3Bconfirm%3DN%3BsendType%3D0%3BactionCode%3DjsztActionCode_flowSendsendDelivery%3BchannelCode_common%3D200777%3BpubAreaCode%3D025%3BpushUserId%3Djszt_660977987262%3BcoachUser%3D%3BuserLogAccNbrType%3D%3BuserLogAccNbr%3D%3BuserTokenAccNbrType%3D2%3BztVersion%3D3.1.0%3BztInterSource%3D200777%3BpubToken%3Dundefined%3B")
                    .setRequestHeader("Accept-Charset: UTF-8\n" +
                            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" +
                            "User-Agent: Dalvik/9.5.0 (Linux; U; Android 9.0.1; iPhone 9S Build/LMY99Z)\n" +
                            "X-Requested-With:XMLHttpRequest")
                    .Request()
            val ret = a.toString(Charset.defaultCharset())
            return ret.contains("领取成功")
        }

        /**
         * 获取登录接口信息
         */
        fun Build_Login(num: String,
                        pass: String) = XMethod().apply {
            url = "http://ztmall.go189.cn/zt-login/login/innerAccount"
            data = "accNbr=$num;password=$pass;accNbrType=2000004;PWDType=-1;areaCode=;isAutomatic=N;actionCode=login037;channelCode_common=011028;pubAreaCode=025;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=;userTokenAccNbrType=2;ztVersion=5.0.0;ztInterSource=android;pubToken=undefined;"
        }

        /**
         * 获取账户余额
         * @param num 手机号码
         * @param areaCode 手机号码
         * @param token 手机号码
         * @return XMETHOD请求
         */
        fun Build_searchCallBalanceReqWithCache(num: String,
                                                areaCode: String,
                                                token: String) = XMethod().apply {
            this.url = "http://61.160.137.141/jszt/uniformity/searchCallBalanceReqWithCache"
            this.data = "accNbr=$num;areaCode=$areaCode;dccQueryFlag=0;dccDestinationAttr=2;actionCode=jsztActionCode_uniformitysearchCallBalanceReqWithCache;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=5.0.0;ztInterSource=android;pubToken=$token;"
        }

        /**
         * 获取本月已用话费
         * @param num 手机号码
         * @param areaCode 手机号码
         * @param token 手机号码
         * @return XMETHOD请求
         */
        fun Build_GetTotalUsedBill(num: String,
                                   areaCode: String,
                                   token: String) = XMethod().apply {
            this.url = "http://61.160.137.141/jszt/uniformity/searchCallCurrMonthBillReqWithCache"
            val dccBillingCycle = Calendar.getInstance().get(Calendar.YEAR).toString() + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            this.data = "dccBillingCycle=$dccBillingCycle;productId=2;accNbr=$num;areaCode=$areaCode;actionCode=jsztActionCode_uniformitysearchCallCurrMonthBillReqWithCache;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=5.0.0;ztInterSource=android;pubToken=$token;"
        }

        /**
         * 获取账户剩余流量信息
         * @param num 手机号码
         * @param areaCode 手机号码
         * @param token 手机号码
         * @return XMETHOD请求
         */
        fun Build_GetTotalAcu(token: String,
                              areaCode: String,
                              num: String) = XMethod().apply {
            url = "http://61.160.137.141/jszt/uniformity/searchCurrAcuReqWithCache"
            data = "accNbr=$num;family=2;areaCode=$areaCode;actionCode=jsztActionCode_uniformitysearchCurrAcuReqWithCache;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=5.0.0;ztInterSource=android;pubToken=$token;"
        }

        /**
         * 获取状态栏显示的流量
         */
        fun Build_getNewFlowSearchTwo(token: String,
                                      areaCode: String,
                                      num: String) = XMethod().apply {
            url = "http://221.228.39.34/ZtFlowOrder/jszt/flow/flowSearchNotice/getNewFlowSearchTwo"
            data = "para=accNbr%3D$num%3BuserLogAccNbr%3D$num%3BareaCode%3D$areaCode%3BpubAreaCode%3D0515%3BztVersion%3D5.0.0%3BpubToken%3D$token%3Bfamily%3D2%3BactionCode%3DjsztActionCode_restgetNewFlowSearch%3BchannelCode_common%3D100003%3BpushUserId%3Djszt_${HttpApi.getAndroidID(num)}%3BcoachUser%3D%3BuserLogAccNbrType%3D2%3BuserTokenAccNbrType%3D2%3BztInterSource%3D100003%3B"
        }

        /**
         * 获取所有流量状态
         */
        fun Build_getNewFlowSearchSort(token: String,
                                       areaCode: String,
                                       num: String) = XMethod().apply {
            url = "http://61.160.137.141/jszt/liuliang/getNewFlowSearchSort"
            data = "accNbr=$num;family=2;areaCode=$areaCode;actionCode=jsztActionCode_liulianggetNewFlowSearchSort;channelCode_common=011015;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=5.0.0;ztInterSource=android;pubToken=$token;"
        }

        /**
         * 获取手机号码用户信息
         * @param num 手机号码
         * @param areaCode 手机号码
         * @param token 手机号码
         * @return XMETHOD请求
         *
         *
         */
        fun Build_login2UserInfo(token: String,
                                 areaCode: String,
                                 num: String) = XMethod().apply {
            this.url = "http://61.160.137.141/jszt/rest/login2UserInfo"
            this.data = "accNbr=$num;accNbrType=2000004;PWDType=-1;areaCode=$areaCode;actionCode=yw013;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=5.0.0;ztInterSource=android;pubToken=$token;"
        }

        /**
         * 获取本月所有账单
         */
        fun Build_GetZhangDan(num: String,
                              areaCode: String,
                              token: String) = XMethod().apply {
            url = "http://61.160.137.141/jszt/uniformity/searchZhangDan"
            val dccBillingCycle = Calendar.getInstance().get(Calendar.YEAR).toString() + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            this.data = "dccBillingCycle=$dccBillingCycle;dccDestinationAttr=2;accNbr=$num;areaCode=$areaCode;actionCode=jsztActionCode_uniformitysearchZhangDan;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=5.0.0;ztInterSource=android;pubToken=$token;"
        }

        /**
         * 根据offerSpecl领取礼包
         */
        fun Build_DeliveryOrder(num: String,
                                sendPhone: String,
                                offerSpecl: String,
                                token: String,
                                areaCode: String,
                                goodName: String = "2048") = XMethod().apply {
            url = "http://61.160.137.141/jszt/flowSend/flowDeliveryOrder"
            data = "accNbr=$num;sendPhone=$sendPhone;offerSpecl=$offerSpecl;family=2;goodName=$goodName;areaCode=$areaCode;actionCode=jsztActionCode_flowSendflowDeliveryOrder;channelCode_common=011015;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=5.0.0;ztInterSource=android;pubToken=$token;"
        }
    }

    open class XMethod {
        var url = ""
        var data = ""
    }
}