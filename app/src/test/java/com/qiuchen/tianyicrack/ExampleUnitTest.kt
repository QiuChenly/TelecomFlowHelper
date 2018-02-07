package com.qiuchen.tianyicrack

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
            if (i and 0xff < 0x10){
                sb.append("0")
            }
            sb.append(Integer.toHexString(i and 0xff))
        }
        return sb.toString()
    }
    @Test
    fun addition_isCorrect() {
        val s = HttpApi().exec(HttpApi.Build_Login()
                .setUser("17714602936", "224365")
                .get())
//        println(getMD5("123"))
        println(s.toString(Charset.defaultCharset()))
    }
}
