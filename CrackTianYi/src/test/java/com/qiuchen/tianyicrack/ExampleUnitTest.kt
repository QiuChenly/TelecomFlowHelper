package com.qiuchen.tianyicrack

import com.google.gson.Gson
import com.qiuchen.jingyi.nativeHttp.nHttp
import com.qiuchen.tianyicrack.TianYiApi.DesEncrypt
import com.qiuchen.tianyicrack.TianYiApi.HttpApi
import org.junit.Test
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
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

    @Test
    fun addition_isCorrect() {
//        val s = HttpApi().exec(HttpApi.Build_Login()
//                .setUser("17714602936", "224365")
//                .get())
////        println(getMD5("123"))
//        println(s.toString(Charset.defaultCharset()))

        val ss = DesEncrypt(true).decrypt("e73234d312a5a8a2b63296f6045f016871adcda3782bc0da547cea923da70b61108fad92e57caac0a0e73dd688a2d6295f7263cca6c6a3667905e678a8ccc4b8af70558c0ea881dffd44948475a21b2ddd13a29f096a555376584d475d4eff9fe894fc92ff95cd3b6e73e29834bc56562f3481f24f2bfa19fcc48c67d2d895649e65d110699a3e9bb1abee08b97f3797ba72ac3f1648834911932588cddb755511f2ce83b497f00616cbce96cecbfb213bda1ad191561f396338e252359cd7015019cfdc3a0bac383076a4fadc66ffb389fe197802dfd2226fdb8bf2d88994122a286828a04581cf2b6ec9a9433ed7e4c61e285a2bf2d17402aa65711464f691a7f0d8a3bf237c0268b19a6ff27965b54642826f275dc02d9c49cb132e17d48ecb8d7ef9bf971b65abb31a94da2b07336d2e4dd3ce07386668b19a6ff27965b574be815cc4720d86531b525be4d382eb5b0ae6c186cfd1e0537bfded97520d7a177aea4e7f42a77af2bbf2f7d92e341009af362b7afe07267de5ce700855d4046ff02025533bb23167296ba53aff366ec8c6d7fdff3a185c")
        //accNbr=17714602936;sendPhone=17768742864;offerSpecl=106024;family=2;goodName=1024;areaCode=0515;actionCode=jsztActionCode_flowSendflowDeliveryOrder;channelCode_common=011015;pubAreaCode=0515;pushUserId=android_355572cc6baaff7820180222012227183;coachUser=;userLogAccNbrType=2;userLogAccNbr=17714602936;userTokenAccNbrType=2;ztVersion=5.0.0;ztInterSource=android;pubToken=4bf823a866c94a3fbc369bb5351afc82;

        println(ss)
    }

    @Test
    fun FlowInfoGet() {
        val s = HttpApi().InitFlowExpressSharedInfo("http://wx.go189.cn/tysh/pages/yaoyiyao/receiveBigFLow5.html?ztInterSource=200777&platform=wap&rowId=0d37d4a435dd45e1b3d517a39486f324&flowNumber=5120")
        println(Gson().toJson(s))
        val a = HttpApi().Build_GetFlowByUser("17714602936",s.rowID)
        println(if(a) "SB" else "Fail")
        //{"TSR_RESULT":"0","TSR_MSG":"领取成功，快去“天翼生活”客户端领取吧~","flowLeft":"2048"}
    }
}
