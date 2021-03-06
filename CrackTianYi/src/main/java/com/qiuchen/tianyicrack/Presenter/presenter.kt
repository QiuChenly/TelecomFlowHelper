package com.qiuchen.tianyicrack.Presenter

import android.os.Handler
import android.os.Looper.getMainLooper
import com.google.gson.Gson
import com.qiuchen.jingyi.nativeHttp.nHttp
import com.qiuchen.tianyicrack.Bean.*
import com.qiuchen.tianyicrack.TianYiApi.DesEncrypt
import com.qiuchen.tianyicrack.TianYiApi.HttpApi
import com.qiuchen.tianyicrack.mSContext
import java.nio.charset.Charset

/**
 * Created by qiuchen on 2018/2/6.
 */
class presenter {
    companion object {
        private lateinit var loginResult: loginCallback
        val userList: HashMap<String, mBaseInfoCollection?> = HashMap()
        val hand = Handler(getMainLooper())
        val mDB = mSContext.getDB()!!

        /**
         * 登录
         */
        fun login(user: String, pass: String, cb: loginCB) {
            if (!mSContext.hasNet()) {
                cb.loginCBS(-99)
                return
            }
            Thread {
                kotlin.run {
                    val s = HttpApi.exec(HttpApi.Build_Login(user, pass))
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

        /**
         * 登录单个账户
         */
        fun loginSingle(d: DB_PhoneInfoBean, RefreshCallback: RefreshCallback) {
            Thread {
                kotlin.run {
                    userList[d.user] = getBaseInfo(d.token, d.areaCode, d.user)
                    hand.post {
                        RefreshCallback.onFlashed()
                    }
                }
            }.start()
        }

        /**
         * 弃用方法,数据绑定后自动刷新,此方法不再生效
         * @param RefreshCallback 刷新成功回调
         * @return 异步方法,不返回任何数据.
         */
        fun refreshOnlineInfo(RefreshCallback: RefreshCallback) {
            Thread {
                kotlin.run {
                    for (a in userList) {
                        val s = a.value!!
                        userList[a.key] = getBaseInfo(s.token, s.areaCode, s.phoneNum)
                    }
                    hand.post {
                        RefreshCallback.onFlashed()
                    }
                }
            }.start()
        }

        /**
         * 数据刷新回调
         */
        interface RefreshCallback {
            fun onFlashed()
        }

        /**
         * 获取流量礼包信息
         */
        fun getFlowInfo(token: String, areaCode: String, phone: String): FlowExpress {
            val data = "userPhone=$phone;accNbr=$phone;myWin=;myGet=G;actionCode=jsztActionCode_flowSendgetFlowInfo;channelCode_common=011028;pubAreaCode=$areaCode;pushUserId=android_${HttpApi.getAndroidID(phone)};coachUser=;userLogAccNbrType=2;userLogAccNbr=$phone;userTokenAccNbrType=2;ztVersion=5.0.0;ztInterSource=android;pubToken=$token;"
            val s = nHttp.Builder("http://61.160.137.141/jszt/flowSend/getFlowInfo")
                    .setPostData("para=" + DesEncrypt(true).encrypt(data))
                    .setRequestHeader("Accept-Charset: UTF-8\n" +
                            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" +
                            "User-Agent: Dalvik/9.5.0 (Linux; U; Android 9.0.1; iPhone 9S Build/LMY99Z)")
                    .Request()
            var ret = FlowExpress()
            if (s.getStatusCode() == 200) {
                val d = s.toString(Charset.defaultCharset())
                try {
                    ret = Gson().fromJson(d, FlowExpress::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return ret
        }

        /**
         * 获取基本信息 QiuChenly
         * @param token 鉴权值
         * @param areaCode 区域代号
         * @param phone 手机号码
         * @return 返回基本信息集合
         */
        fun getBaseInfo(token: String, areaCode: String, phone: String): mBaseInfoCollection? {
            val mBaseInfoCollection = mBaseInfoCollection()
            //登录成功,开始获取用户基本数据
            var s = HttpApi.exec(HttpApi.Build_login2UserInfo(token, areaCode, phone))
            var str: String
            if (s.getStatusCode() == 200) {
                str = s.toString(Charset.defaultCharset())
                //{"TSR_RESULT":"110001","TSR_MSG":"鉴权不通过，请重新登录"}
                if (str.contains("110001")) return null
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
            s = HttpApi.exec(HttpApi.Build_GetTotalAcu(token, areaCode, phone))
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
            s = HttpApi.exec(HttpApi.Build_searchCallBalanceReqWithCache(phone, areaCode, token))
            if (s.getStatusCode() == 200) {
                str = s.toString(Charset.defaultCharset())
                val CallBalance = Gson().fromJson(str, CallBalanceBean::class.java)
                mBaseInfoCollection.totalMoney = CallBalance.body.totalMoney
            }

            //获取话费信息 - 已用
            s = HttpApi.exec(HttpApi.Build_GetTotalUsedBill(phone, areaCode, token))
            if (s.getStatusCode() == 200) {
                str = s.toString(Charset.defaultCharset())
                val TotalUsedBill = Gson().fromJson(str, TotalUsedBillBean::class.java)
                //如果出现首月查询话费失败,报出错误
                //{"TSR_RESULT":"100001","TSR_MSG":"查询本月消费失败！"}
                mBaseInfoCollection.dccBillFee = if (TotalUsedBill.body != null) TotalUsedBill.body.dccBillFee else TotalUsedBill.TSR_MSG
            }
            mBaseInfoCollection.flowListBean = getFlowInfo(token, areaCode, phone)
            return mBaseInfoCollection
        }

        /**
         * 获取流量快递礼包基本信息
         */
        fun getFlowExpressInfo(str: String): FlowExpressOnlineBean {
            return HttpApi.InitFlowExpressSharedInfo(str)
        }

        /**
         * 领取流量快递with 用户
         */
        fun getFlowExpress(str: String, row: String): Boolean {
            return HttpApi.Build_GetFlowByUser(str, row)
        }

        /**
         * 传一个手机号码,直接领取免费流量
         * @return 返回值: -1|没有此号码的缓存 -2|此号码没有待领取的流量 0|success 1|领取失败
         */
        fun getFlowReal(num: String): Int {
            val info = userList[num] ?: return -1
            info.flowListBean = getFlowInfo(info.token, info.areaCode, info.phoneNum)

            //check first item is WillGetState
            val first = info.flowListBean.getFlowList[0]
            if (first.isGet == "1") return -2

            val method = HttpApi.Build_DeliveryOrder(info.phoneNum,
                    first.sendNbr, first.offerId,
                    info.token, info.areaCode,
                    "8192")
            val s = nHttp.Builder(method.url)
                    .setPostData("para=" + DesEncrypt(true).encrypt(method.data))
                    .setRequestHeader("Accept-Charset: UTF-8\n" +
                            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" +
                            "User-Agent: Dalvik/9.5.0 (Linux; U; Android 9.0.1; iPhone 9S Build/LMY99Z)").Request()
            if (s.getStatusCode() == 200) {
                val d = s.toString(Charset.defaultCharset())
            }
            return 0
        }

        /**
         * 登录回调类
         */
        interface loginCB {
            /**
             * 登录方法回调
             * @param state 成功=1,失败=-1 -99 = 无网络
             */
            fun loginCBS(state: Int, loginResult: loginCallback = loginCallback())
        }
    }
}