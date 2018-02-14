package com.qiuchen.tianyicrack

import org.junit.Test
import java.util.regex.Pattern

/**
 * Created by qiuchen on 2018/2/14.
 */
class RegexTest {
    @Test
    fun start() {
        //开始匹配数据
        val string = "http://wx.go189.cn/tysh/pages/yaoyiyao/receiveBigFLow3.html?ztInterSource=200777&platform=wap&rowId=1f3b35ea2cd740b892b99e6f5421f658&flowNumber=5120"
        val p = Pattern.compile("http://wx\\.go189\\.cn/tysh/pages/yaoyiyao/receiveBigFLow3\\.html\\?ztInterSource=(.*?)&platform=wap&rowId=(.*?)&flowNumber=(.*?)&")
        val m = p.matcher(string + "&")
        while (m.find()) {
            println(m.group(1))
            println(m.group(2))
            println(m.group(3))
        }
    }
}
