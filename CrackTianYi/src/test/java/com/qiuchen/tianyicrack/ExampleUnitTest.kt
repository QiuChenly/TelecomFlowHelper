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

        val ss = DesEncrypt(true).decrypt("1628c578c122f549d3044f153db71f39850f3f0d6d85e6d6e141a4718aff7ffe83d5e2b17fe66e964f1a2d1426200e237300b429b0d7764de894fc92ff95cd3b6e73e29834bc56562f3481f24f2bfa19fcc48c67d2d89564d954f4c8e51a4cd202b52fbf2aeaf8b05d6fd002ce2b74146c6b09a247186eebbe1da79a9d19f17a1752e4963c3c14c6d116a7a9ea1feeb24da1418aee4da96773b08e11a7b94678e6b6a7a487f59e711d108e3ea20265de83f5fce657f8aa2dbcd518fd5d49ce8b8bb3c67bdf4140207cd0ac7ae8cf630397b88f10fdae6f007689f5bf91627bb8a682a99c846a740234c1c9fcacaba4478123ae574d854b1d36774905f26a3badc95cf40a8c8140057689f5bf91627bb8755979383fd99de20d4c43dafd25e32624b6c97bbaf230cfdf14d8896daf36e6d8fae11c7911fc18722085790d57309abfe799c186f5b3734e02b491874be0cc74ea47c801de3282fd0118ed4b5abde0bd9a09fdc0a2ef0f")
        //println(ss)


        val s = nHttp.Builder("http://url.cn/5j6YHPS")
                .setRequestHeader("Accept-Charset: UTF-8\n" +
                        "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" +
                        "User-Agent: Dalvik/9.5.0 (Linux; U; Android 9.0.1; iPhone 9S Build/LMY99Z)")
                .Request()
        println(s.getStatusCode())
        println(s.getRedirectURL())
        println(s.toString(Charset.defaultCharset()))
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
