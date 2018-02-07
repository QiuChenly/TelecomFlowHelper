package com.qiuchen.tianyicrack.TianYiApi

import com.qiuchen.jingyi.nativeHttp.nHttp
import java.security.MessageDigest
import java.util.*

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
    }

    open class XMethod {
        var url = ""
        var data = ""
    }

    /**
     * 执行Http请求
     * @param X_Method 回调的XMethod子类实例
     * @return 返回nHttp原生返回数据类实例
     */
    fun exec(X_Method: XMethod): nHttp {
        return nHttp.Builder(X_Method.url)
                .setPostData(X_Method.data)
                .setRequestHeader("Accept-Charset: UTF-8\n" +
                        "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" +
                        "User-Agent: Dalvik/9.5.0 (Linux; U; Android 9.0.1; iPhone 9S Build/LMY99Z)")
                .Request()
    }

    class Build_Login {
        private val LOGIN_API = "http://ztmall.go189.cn/zt-login/login/innerAccount"
        private var LOGIN_PARAMS = "accNbr=17777777777;password=123456;accNbrType=2000004;PWDType=-1;areaCode=;isAutomatic=N;actionCode=login037;channelCode_common=011028;pubAreaCode=025;pushUserId=android_86971802216375920180206115131956;coachUser=;userLogAccNbrType=2;userLogAccNbr=;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=undefined;"

        private var phone = ""
        private var ps: String = ""

        fun setUser(num: String, pass: String): Build_Login {
            phone = num
            ps = pass
            LOGIN_PARAMS = "accNbr=${phone};password=${ps};accNbrType=2000004;PWDType=-1;areaCode=;isAutomatic=N;actionCode=login037;channelCode_common=011028;pubAreaCode=025;pushUserId=android_${HttpApi.getAndroidID(phone)};coachUser=;userLogAccNbrType=2;userLogAccNbr=;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=undefined;"
            //LOGIN_PARAMS="accNbr=17714602936;password=224365;accNbrType=2000004;PWDType=-1;areaCode=0515;isAutomatic=Y;actionCode=login037;channelCode_common=011028;pubAreaCode=0515;pushUserId=android_86971802216375920180206115131956;coachUser=;userLogAccNbrType=2;userLogAccNbr=;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=undefined;"
            return this
        }

        fun get(): XMethod {
            val m = XMethod()
            m.url = LOGIN_API
            m.data = getData(LOGIN_PARAMS)
            return m
        }
    }

    /*获取账户余额
["http://61.160.137.141/jszt/uniformity/searchCallBalanceReqWithCache",{"para":"accNbr=17714602936;areaCode=0515;dccQueryFlag=0;dccDestinationAttr=2;actionCode=jsztActionCode_uniformitysearchCallBalanceReqWithCache;channelCode_common=011028;pubAreaCode=0515;pushUserId=android_86971802216375920180206115131956;coachUser=;userLogAccNbrType=2;userLogAccNbr=17714602936;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=05ce0079846c4e518dd84ce197775e18;"}]
*/
    /**
     * 获取账户余额
     * @param num 手机号码
     * @param areaCode 手机号码
     * @param token 手机号码
     * @return XMETHOD请求
     */
    fun Build_searchCallBalanceReqWithCache(num: String, areaCode: String, token: String) = XMethod().apply {
        this.url = "http://61.160.137.141/jszt/uniformity/searchCallBalanceReqWithCache"
        this.data = getData("accNbr=$num;areaCode=$areaCode;dccQueryFlag=0;dccDestinationAttr=2;actionCode=jsztActionCode_uniformitysearchCallBalanceReqWithCache;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=$token;")
    }

/*
获取本月已用话费
["http://61.160.137.141/jszt/uniformity/searchCallCurrMonthBillReqWithCache",{"para":"dccBillingCycle=201802;productId=2;accNbr=17714602936;areaCode=0515;actionCode=jsztActionCode_uniformitysearchCallCurrMonthBillReqWithCache;channelCode_common=011028;pubAreaCode=0515;pushUserId=android_86971802216375920180206115131956;coachUser=;userLogAccNbrType=2;userLogAccNbr=17714602936;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=05ce0079846c4e518dd84ce197775e18;"}]


*/
    /**
     * 获取本月已用话费
     * @param num 手机号码
     * @param areaCode 手机号码
     * @param token 手机号码
     * @return XMETHOD请求
     */
    fun Build_GetTotalUsedBill(num: String, areaCode: String, token: String) = XMethod().apply {
        this.url = "http://61.160.137.141/jszt/uniformity/searchCallCurrMonthBillReqWithCache"
        val dccBillingCycle = Calendar.getInstance().get(Calendar.YEAR).toString() + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        this.data = getData("dccBillingCycle=$dccBillingCycle;productId=2;accNbr=$num;areaCode=$areaCode;actionCode=jsztActionCode_uniformitysearchCallCurrMonthBillReqWithCache;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=$token;")
    }

    /*
     * 获取账户剩余流量信息
    ["http://61.160.137.141/jszt/uniformity/searchCurrAcuReqWithCache",{"para":"accNbr=17714602936;family=2;areaCode=0515;actionCode=jsztActionCode_uniformitysearchCurrAcuReqWithCache;channelCode_common=011028;pubAreaCode=0515;pushUserId=android_86971802216375920180206115131956;coachUser=;userLogAccNbrType=2;userLogAccNbr=17714602936;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=05ce0079846c4e518dd84ce197775e18;"}]
     */

    /**
     * 获取账户剩余流量信息
     * @param num 手机号码
     * @param areaCode 手机号码
     * @param token 手机号码
     * @return XMETHOD请求
     */
    class Build_GetTotalAcu(token: String, areaCode: String, num: String) {
        private val mUrl = "http://61.160.137.141/jszt/uniformity/searchCurrAcuReqWithCache"
        private var para = "accNbr=$num;family=2;areaCode=$areaCode;actionCode=jsztActionCode_uniformitysearchCurrAcuReqWithCache;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=$token;"

        fun get(): XMethod {
            return XMethod().apply {
                this.url = mUrl
                this.data = getData(para)
            }
        }
    }


    /*
    获取手机号码用户信息
["http://61.160.137.141/jszt/rest/login2UserInfo",{"para":"accNbr=17714602936;accNbrType=2000004;PWDType=-1;areaCode=0515;actionCode=yw013;channelCode_common=011028;pubAreaCode=0515;pushUserId=android_86971802216375920180206115131956;coachUser=;userLogAccNbrType=2;userLogAccNbr=17714602936;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=ff6ea6e0c6654c0a80ea585f7538d192;"}]
     */

    /**
     * 获取手机号码用户信息
     * @param num 手机号码
     * @param areaCode 手机号码
     * @param token 手机号码
     * @return XMETHOD请求
     *
     *
     */
    class Build_login2UserInfo(token: String, areaCode: String, num: String) {
        private val mUrl = "http://61.160.137.141/jszt/rest/login2UserInfo"
        private var para = "accNbr=$num;accNbrType=2000004;PWDType=-1;areaCode=$areaCode;actionCode=yw013;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(num)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=$token;"
        fun get(): XMethod {
//            null({"managerAccount":"0","mobileInfo":[{"prodId":"152287363266","mobileNum":"17714602936"}],"iTVInfo":[],"broadbandInfo":[],"phoneInfo":[],"xltInfo":[],"c+wInfo":[],"smState":"0","state34G":"1","zqState":"1","customerName":"陈春","indentNbr":"320924197212102954","indentNbrType":"1","accNbr":"17714602936","areaCode":"0515","childAreaCode":"39","payMethod":"1","TSR_RESULT":"0","TSR_CODE":"0000","TSR_MSG":"","token":"c8ce46cdb22044aca0681db000a5a7ad","cacheState":"0","JSZT_MEMCACHED_STATUS":"1"})
            return XMethod().apply {
                this.url = mUrl
                this.data = getData(para)
            }
        }
    }
}