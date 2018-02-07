package com.qiuchen.tianyicrack.Presenter

import com.google.gson.Gson
import com.qiuchen.tianyicrack.Bean.loginCallback
import com.qiuchen.tianyicrack.TianYiApi.HttpApi

/**
 * Created by qiuchen on 2018/2/6.
 */
class presenter {
    companion object {

        lateinit var loginResult: loginCallback
        val userList: HashMap<String, loginCallback> = HashMap()

        fun login(user: String, pass: String, cb: loginCB) {
            Thread {
                kotlin.run {
                    val s = HttpApi().exec(HttpApi.Build_Login()
                            .setUser(user, pass)
                            .get())
                    if (s.getStatusCode() == 200) {
                        val ret = s.toString(Charsets.UTF_8)
                        loginResult = Gson().fromJson(ret, loginCallback::class.java)
                        if (loginResult.TSR_CODE == "0000") {
                            cb.loginCBS(1)
                            userList.put(user, loginResult)
                        } else cb.loginCBS(-1)
                    }
                }
            }.start()
        }

        interface loginCB {
            /**
             * 登录方法回调
             * @param state 成功=1,失败=-1
             */
            fun loginCBS(state: Int)
        }
    }
}