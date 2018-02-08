package com.qiuchen.jingyi.nativeHttp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

/**
 * Created by qiuchen on 2018/1/22.
 * Kotlin 重写Http原生请求类,支持POST GET
 */
open class nHttp(ret: nHttpRet) {

    private var ret = ret

    /**
     * 转换服务器返回的数据为字符串
     */
    override fun toString(): String {
        return ret.RetByteArray.toString(Charset.forName("GBK"))
    }

    /**
     * 自定义编码格式,用于GB2312类型
     */
    fun toString(charset: Charset): String {
        return ret.RetByteArray.toString(charset)
    }

    fun getStatusCode(): Int {
        return ret.statusCode
    }

    /**
     * 获取302跳转的Url地址
     */
    fun getRedirectURL(): String? {
        return ret.retHeaders["Location"]?.get(0)
    }

    /**
     * 转换对应的ArrayByte为图片
     */
    fun toImage(): Bitmap? {
        return BitmapFactory.decodeByteArray(ret.RetByteArray, 0, ret.RetByteArray.size)
    }

    fun getBytes(): ByteArray {
        return ret.RetByteArray
    }

    companion object {
        val METHOD_GET = "GET"
        val METHOD_POST = "POST"
    }

    /**
     * 2018 1.24日 v1.1
     * 1.废弃setUrl(Url:String)方法,改为构造函数传参
     * 2.优化POST请求自动判断,当设置了DATA数据后自动为POST,仍然保留setMethod_POST(METHOD)方法
     * 2018 1.26日 v1.2
     * 1.废弃setMethod_POST(METHOD)方法,保留setMethod(METHOD = POST)方法
     */
    class Builder(mUrl: String = "") {
        private var url = mUrl
        private var method = METHOD_GET
        private var requestHeader = HashMap<String, String>()
        private var requestData: ByteArray = kotlin.ByteArray(0)
        private var nCook: nCookie? = null

        fun setCookieStore(nCookie: nCookie): Builder {
            nCook = nCookie
            return this
        }

        fun setMethod(method: String = METHOD_POST): Builder {
            this.method = method
            return this
        }

        fun setRequestHeader(head: String, body: String): Builder {
            requestHeader[head] = body
            return this
        }

        /**
         * 设置默认请求协议头,格式如下
         * /*       headerKey1: headerBody1
         *          headerKey2: headerBody2
         *  */      headerKey3: headerBody3
         * 一行一条.注意,其中": "可以省略其中的空格
         */
        fun setRequestHeader(requestHeader: String): Builder {
            requestHeader.split("\n")
                    .forEach {
                        val len = it.indexOf(":")
                        val key = it.substring(0, len)
                        val value = it.substring(len + 1, it.length)
                        this.requestHeader[key] = value.trim()
                    }
            return this
        }

        fun setPostData(str: String): Builder {
            setPostData(str.toByteArray())
            return this
        }

        /**
         * 原始数据流提交
         * @byteArray 提交的数据数组
         */
        fun setPostData(byteArray: ByteArray): Builder {
            this.requestData = byteArray
            return this
        }

        private var allowRedirect: Boolean = false

        /**
         * 设置是否支持302重定向,默认不自动302
         */
        fun setAllowRedirect(allowRedirect: Boolean): Builder {
            this.allowRedirect = allowRedirect
            return this
        }

        //区分同步还是异步
        //此方法为同步方法
        fun Request(): nHttp {
            val datas = RequestHttp(url, method, requestData, requestHeader, allowRedirect)
            return nHttp(datas)
        }

        /**
         * 实体请求方法
         * 2018.2.6 QiuChenly 修正 Method方法判断导致无法自动识别POST请求的问题
         * 2018.2.7 QiuChenly 修正 连接超时问题
         */
        private fun RequestHttp(u: String, m: String, data: ByteArray, RequestHeader: HashMap<String, String>, allowRedirect: Boolean): nHttpRet {
            val urlConn = URL(u).openConnection() as HttpURLConnection
            urlConn.requestMethod = m
            urlConn.connectTimeout = 15000
            urlConn.readTimeout = 15000
            if (data.isNotEmpty()) {
                urlConn.requestMethod = METHOD_POST
            }
            urlConn.instanceFollowRedirects = allowRedirect

            //添加默认RequestHeader
            urlConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
            urlConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
            urlConn.setRequestProperty("Cookie", nCook.toString())

            for (rh in RequestHeader.entries) {
                urlConn.setRequestProperty(rh.key, rh.value)
            }
            //修正连接超时导致闪退的BUG
            var code = -555
            return try {
                urlConn.doInput = true
                if (urlConn.requestMethod === METHOD_POST) {
                    urlConn.doOutput = true
                    urlConn.outputStream.write(data)
                }
                if (nCook != null) {
                    nCook?.addAll(getCookies(urlConn))
                }
                code = urlConn.responseCode
                nHttpRet(code, getBytes(urlConn.inputStream), urlConn.headerFields)
            } catch (e: Exception) {
                nHttpRet(code, kotlin.ByteArray(1024), HashMap())
            }
        }

        //异步请求方法
        fun RequestAsync(onRequestCallback: RequestCallback) {
            Thread {
                kotlin.run {
                    val datas = nHttp(RequestHttp(url, method, requestData, requestHeader, allowRedirect))
                    onRequestCallback.onSuccess(datas)
                }
            }.start()
        }


        //*****************************************
        //              其他工具类方法
        //*****************************************

        private fun getCookies(urlConnection: HttpURLConnection): ArrayList<CookieEx> {
            val mCK = ArrayList<CookieEx>()
            if (urlConnection.headerFields["Set-Cookie"] != null) {
                val list: List<String> = urlConnection.headerFields["Set-Cookie"] as List<String>
//                list.map { it.split(";")[0].split("=") }
//                        .mapTo(mCK) { CookieEx(it[0], it[1]) }
                list.forEach {
                    val b = it.split(";")[0]
                    val c = b.split("=")
                    if (c.size == 2) {
                        val d = CookieEx(c[0], c[1])
                        mCK.add(d)
                        println(c)
                    }
                }
            }
            return mCK
        }


        private fun getBytes(input: InputStream): ByteArray {
            val op = ByteArrayOutputStream()
            var len: Int
            var ba = ByteArray(1024)
            do {
                len = input.read(ba)
                if (len != -1)
                    op.write(ba, 0, len)
            } while (len != -1)
            return op.toByteArray()
        }
    }

    //*****************************************
    //              其他辅助类
    //*****************************************
    interface RequestCallback {
        fun onSuccess(ret: nHttp)
        fun onFailure(statusCode: Int, retData: String)
    }

    class nHttpRet(var statusCode: Int = -1, var RetByteArray: ByteArray = kotlin.ByteArray(0), var retHeaders: Map<String, List<String>> = HashMap<String, List<String>>())

    class CookieEx(var key: String = "", var value: String = "")

    class nCookie {

        private var mHash = HashMap<String, String>()

        /**
         * 从序列化的字符串读取Cookie
         */
        fun addAll(allCook: String) {
            allCook.split(";").forEach {
                val s = it.split("=")
                if (s.size > 1)
                    mHash[s[0]] = s[1]
            }
        }

        /**
         * 一次性读取所有Cookie
         */
        fun addAll(allCook: ArrayList<nHttp.CookieEx>?) {
            allCook?.forEach {
                mHash[it.key] = it.value
            }
        }

        fun add(key: String, value: String) {
            mHash[key] = value
        }

        override fun toString(): String {
            if (mHash.isEmpty())
                return ""
            var s = ""
            mHash.forEach {
                s += it.key + "=" + it.value + ";"
            }
            return s.substring(0, s.length - 1)
        }

        fun remove(key: String) {
            mHash.remove(key)
        }

        fun initStatus() {
            mHash = HashMap()
        }
    }
}