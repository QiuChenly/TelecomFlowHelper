package com.qiuchen.tianyicrack.Presenter

import android.os.Handler
import android.os.Looper.getMainLooper
import com.google.gson.Gson
import com.qiuchen.tianyicrack.Bean.*
import com.qiuchen.tianyicrack.TianYiApi.HttpApi
import com.qiuchen.tianyicrack.mSContext
import java.nio.charset.Charset

/**
 * Created by qiuchen on 2018/2/6.
 */
class presenter {
    companion object {
        private lateinit var loginResult: loginCallback
        val userList: HashMap<String, mBaseInfoCollection> = HashMap()
        val hand = Handler(getMainLooper())
        private val mDB = mSContext.getDB()

        fun login(user: String, pass: String, cb: loginCB) {
            if (!mSContext.hasNet()) {
                cb.loginCBS(-99)
                return
            }
            Thread {
                kotlin.run {
                    var s = HttpApi().exec(HttpApi.Build_Login()
                            .setUser(user, pass)
                            .get())
                    if (s.getStatusCode() == 200) {
                        val ret = s.toString(Charset.defaultCharset())
                        loginResult = Gson().fromJson(ret, loginCallback::class.java)
                        if (loginResult.TSR_CODE == "0000") {
                            mDB.saveSession(loginResult.accNbr, loginResult.token, loginResult.areaCode)
                            userList[user] = getBaseInfo(loginResult.token, loginResult.areaCode, loginResult.accNbr)
                            cb.loginCBS(1, loginResult)
                        } else cb.loginCBS(-1, loginResult)
                    }
                }
            }.start()
        }

        fun loginSingle(d: DB_PhoneInfoBean, refreshCallback: refreshCallback) {
            Thread {
                kotlin.run {
                    userList[d.user] = getBaseInfo(d.token, d.areaCode, d.user)
                    hand.post {
                        refreshCallback.onFlashed()
                    }
                }
            }.start()
        }

        fun refreshOnlineInfo(refreshCallback: refreshCallback) {
            Thread {
                kotlin.run {
                    for (a in userList) {
                        val s = a.value
                        userList[a.key] = getBaseInfo(s.token, s.areaCode, s.phoneNum)
                    }
                    hand.post {
                        refreshCallback.onFlashed()
                    }
                }
            }.start()
        }

        interface refreshCallback {
            fun onFlashed();
        }


        /**
         * 获取基本信息 QiuChenly
         * @param token 鉴权值
         * @param areaCode 区域代号
         * @param phone 手机号码
         * @return 返回基本信息集合
         */
        fun getBaseInfo(token: String, areaCode: String, phone: String): mBaseInfoCollection {
            val mBaseInfoCollection = mBaseInfoCollection()
            //登录成功,开始获取用户基本数据
            var s = HttpApi().exec(HttpApi.Build_login2UserInfo(token, areaCode, phone).get())
            var str = ""
            if (s.getStatusCode() == 200) {
                str = s.toString(Charset.defaultCharset())
                str = str.substring(5, str.length - 1)
                val userInfo = Gson().fromJson(str, userInfoBean::class.java)
                mBaseInfoCollection.areaCode = userInfo.areaCode
                mBaseInfoCollection.token = userInfo.token
                mBaseInfoCollection.phoneNum = userInfo.accNbr
                mBaseInfoCollection.childAreaCode = userInfo.childAreaCode
                mBaseInfoCollection.customerName = userInfo.customerName
                mBaseInfoCollection.indentNbr = userInfo.indentNbr
            }

            //获取流量数据
            s = HttpApi().exec(HttpApi.Build_GetTotalAcu(token, areaCode, phone).get())
            if (s.getStatusCode() == 200) {
                str = s.toString(Charset.defaultCharset())
                val CurrAcu = Gson().fromJson(str, CurrAcuBean::class.java)
                mBaseInfoCollection.flowInfo.leftFlow = CurrAcu.body.leftFlow
                mBaseInfoCollection.flowInfo.provinceLeftFlow = CurrAcu.body.provinceLeftFlow
                mBaseInfoCollection.flowInfo.totalFlow = CurrAcu.body.totalFlow
                mBaseInfoCollection.flowInfo.usedFlow = CurrAcu.body.usedFlow
            }
            //TODO 预留国家剩余流量和市内流量接口信息,以后备用

            //获取话费信息 - 余额
            s = HttpApi().exec(HttpApi().Build_searchCallBalanceReqWithCache(phone, areaCode, token))
            if (s.getStatusCode() == 200) {
                str = s.toString(Charset.defaultCharset())
                val CallBalance = Gson().fromJson(str, CallBalanceBean::class.java)
                mBaseInfoCollection.totalMoney = CallBalance.body.totalMoney
            }

            //获取话费信息 - 已用
            s = HttpApi().exec(HttpApi().Build_GetTotalUsedBill(phone, areaCode, token))
            if (s.getStatusCode() == 200) {
                str = s.toString(Charset.defaultCharset())
                val TotalUsedBill = Gson().fromJson(str, TotalUsedBillBean::class.java)
                mBaseInfoCollection.dccBillFee = TotalUsedBill.body.dccBillFee
            }
            return mBaseInfoCollection
        }

        interface loginCB {
            /**
             * 登录方法回调
             * @param state 成功=1,失败=-1 -99 = 无网络
             */
            fun loginCBS(state: Int, loginResult: loginCallback = loginCallback())
        }
    }
}