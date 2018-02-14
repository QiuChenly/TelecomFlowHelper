package com.qiuchen.tianyicrack.Other

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.qiuchen.tianyicrack.Adapter.Adapter_FlowGetList
import com.qiuchen.tianyicrack.Presenter.presenter
import com.qiuchen.tianyicrack.R
import com.qiuchen.tianyicrack.mSContext
import java.util.concurrent.Executors
import kotlin.concurrent.thread

/**
 * Created by qiuchen on 2018/2/14.
 */
class FlowOneKeyGet(v: View) : BasePageView(v), Handler.Callback {
    lateinit var getFlow_url: EditText
    lateinit var getFlow_CustomNum: EditText

    lateinit var getFlow_urlResolve: Button
    lateinit var getFlow_CustomNumAdd: Button
    lateinit var getFlow_onekey: Button

    lateinit var getFlow_urlResolveResult: TextView

    lateinit var getFlow_autoGetFlow: CheckBox

    lateinit var getFlow_userList: RecyclerView

    var hand = Handler(this)

    lateinit var adapter: Adapter_FlowGetList

    override fun initView() {
        getFlow_url = f(R.id.getFlow_url)
        getFlow_CustomNum = f(R.id.getFlow_CustomNum)

        getFlow_urlResolve = f(R.id.getFlow_urlResolve, true)
        getFlow_CustomNumAdd = f(R.id.getFlow_CustomNumAdd, true)
        getFlow_onekey = f(R.id.getFlow_onekey, true)

        getFlow_urlResolveResult = f(R.id.getFlow_urlResolveResult)

        getFlow_autoGetFlow = f(R.id.getFlow_autoGetFlow)

        getFlow_autoGetFlow.isChecked = true

        getFlow_userList = f(R.id.getFlow_userList)

        val arr = ArrayList<String>()
        var i = 0
        //自动从已登录的列表加载前5个账户
        for (num in presenter.userList) {
            if (i <= 4)
                arr.add(num.key)
            else
                break
            i++
        }
        adapter = Adapter_FlowGetList(arr)

        getFlow_userList.layoutManager = LinearLayoutManager(getContext())
        getFlow_userList.itemAnimator = DefaultItemAnimator()
        getFlow_userList.setHasFixedSize(false)
        getFlow_userList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.bottom = 7
            }
        })
        getFlow_userList.adapter = adapter
    }

    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            0 -> {
                val bundle = msg.data
                getFlow_urlResolveResult.gravity = Gravity.LEFT
                getFlow_urlResolveResult.height = WindowManager.LayoutParams.WRAP_CONTENT
                if (bundle.getInt("retCode") == -1) {
                    getFlow_urlResolveResult.text = "网址解析失败!"
                } else {
                    var str = "流量包分享者:${bundle.getString("nickName")}(${bundle.getString("mainNum")})\n" +
                            "流量包大小:${bundle.getString("flowSize")} MB\n" +
                            "平均分配大小:${bundle.getString("flowSize").toInt() / 5} MB\n" +
                            "剩余待领取数量:${bundle.getInt("lastBag")}"
                    if (bundle.getInt("lastBag") == 0) {
                        getFlow_url.setText("")
                        str += "\n建议换一个,这个已经领完了..."
                    }
                    getFlow_urlResolveResult.text = str
                }
            }
            1 -> {
                val result = msg.data["isOK"] as Boolean
                val id = msg.data["id"] as Int
                val item = getFlow_userList.findViewHolderForPosition(id)
                with(item.itemView) {
                    val getFlow_item_getState = findViewById<TextView>(R.id.getFlow_item_getState)
                    getFlow_item_getState.text = "用户信息( ${if (result) "领取成功" else "领取失败"} )"
                }
            }
        }
        return true
    }

    var mFlowExpressOnline = com.qiuchen.tianyicrack.Bean.FlowExpressOnlineBean()
    override fun onClick(v: View?) {
        when (v?.id) {
            getFlow_CustomNumAdd.id -> {
                val phone = getFlow_CustomNum.text.toString()
                if (phone.isEmpty() || !mSContext.hasNet() || phone.length < 11) {
                    Toast.makeText(getContext(), "手机号码错误或者无网络连接!", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.addPhone(phone)
                    getFlow_CustomNum.setText("")
                }
            }
            getFlow_onekey.id -> {
                onekeyGetFlow()
            }
            getFlow_urlResolve.id -> {
                //URL处理
                val url = getFlow_url.text.toString()
                if (url.isEmpty()) {
                    Toast.makeText(getContext(), "网址不能为空!", Toast.LENGTH_SHORT).show()
                    return
                }
                if (!mSContext.hasNet()) {
                    Toast.makeText(getContext(), "无网络连接!", Toast.LENGTH_SHORT).show()
                    return
                }
                getFlow_urlResolveResult.text = "解析中..."
                thread {
                    mFlowExpressOnline = presenter.getFlowExpressInfo(url)
                    val msg = Message()
                    msg.data = Bundle().apply {
                        putInt("retCode", mFlowExpressOnline.retCode)
                        putString("nickName", mFlowExpressOnline.nickName)
                        putString("mainNum", mFlowExpressOnline.mainNum)
                        putString("flowSize", mFlowExpressOnline.flowSize)
                        putInt("lastBag", mFlowExpressOnline.lastBag)
                    }
                    msg.what = 0
                    hand.sendMessage(msg)
                }.start()
            }
        }

    }


    val threadPools = Executors.newFixedThreadPool(3)
    fun onekeyGetFlow() {
        val num = adapter.getNums()
        for ((i, n) in num.withIndex()) {
            threadPools.execute {
                val result = presenter.getFlowExpress(n, mFlowExpressOnline.rowID)
                val msg = Message()
                msg.data = Bundle().apply {
                    putBoolean("isOK", result)
                    putInt("id", i)
                }
                msg.what = 1
                hand.sendMessage(msg)
            }
        }
    }
}