package com.qiuchen.tianyicrack.TianYiApi

import com.qiuchen.jingyi.nativeHttp.nHttp

/**
 * Created by qiuchen on 2018/2/6.
 */
class HttpApi {
    companion object {
        fun getData(string: String): String {
            return "para=" + DesEncrypt(true).encrypt(string)
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
                        "User-Agent: Dalvik/9.5.0 (Linux; U; Android 9.0.1; iPhone 9S Build/LMY99Z)\n" +
                        "Host: ztmall.go189.cn")
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
            LOGIN_PARAMS = "accNbr=${phone};password=${ps};accNbrType=2000004;PWDType=-1;areaCode=;isAutomatic=N;actionCode=login037;channelCode_common=011028;pubAreaCode=025;pushUserId=android_86971802216375920180206115131956;coachUser=;userLogAccNbrType=2;userLogAccNbr=;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=undefined;"
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

获取本月已用话费
["http://61.160.137.141/jszt/uniformity/searchCallCurrMonthBillReqWithCache",{"para":"dccBillingCycle=201802;productId=2;accNbr=17714602936;areaCode=0515;actionCode=jsztActionCode_uniformitysearchCallCurrMonthBillReqWithCache;channelCode_common=011028;pubAreaCode=0515;pushUserId=android_86971802216375920180206115131956;coachUser=;userLogAccNbrType=2;userLogAccNbr=17714602936;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=05ce0079846c4e518dd84ce197775e18;"}]


*/

    /**
     * 获取账户剩余流量信息
    ["http://61.160.137.141/jszt/uniformity/searchCurrAcuReqWithCache",{"para":"accNbr=17714602936;family=2;areaCode=0515;actionCode=jsztActionCode_uniformitysearchCurrAcuReqWithCache;channelCode_common=011028;pubAreaCode=0515;pushUserId=android_86971802216375920180206115131956;coachUser=;userLogAccNbrType=2;userLogAccNbr=17714602936;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=05ce0079846c4e518dd84ce197775e18;"}]
     */
    class Build_GetTotalAcu(token: String, areaCode: String, num: String) {
        private val mUrl = "http://61.160.137.141/jszt/uniformity/searchCurrAcuReqWithCache"
        private var para = "accNbr=$num;family=2;areaCode=$areaCode;actionCode=jsztActionCode_uniformitysearchCurrAcuReqWithCache;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_86971802216375920180206115131956;coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=$token;"

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
    class Build_login2UserInfo(token: String, areaCode: String, num: String) {
        private val mUrl = "http://61.160.137.141/jszt/uniformity/searchCurrAcuReqWithCache"
        private var para = "accNbr=$num;accNbrType=2000004;PWDType=-1;areaCode=$areaCode;actionCode=yw013;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_86971802216375920180206115131956;coachUser=;userLogAccNbrType=2;userLogAccNbr=$num;userTokenAccNbrType=2;ztVersion=4.5.0;ztInterSource=android;pubToken=$token;"

        fun get(): XMethod {
            return XMethod().apply {
                this.url = mUrl
                this.data = getData(para)
            }
        }
    }
}