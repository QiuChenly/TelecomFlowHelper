package com.qiuchen.tianyicrack.Adapter

import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.qiuchen.tianyicrack.Bean.DB_PhoneInfoBean
import com.qiuchen.tianyicrack.Bean.loginCallback
import com.qiuchen.tianyicrack.Presenter.presenter
import com.qiuchen.tianyicrack.R
import com.qiuchen.tianyicrack.mSContext

/**
 * Created by qiuchen on 2018/2/6.
 */
class Adapter_mContent_NumList(val mList: ArrayList<DB_PhoneInfoBean>,
                               val rv: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    fun hasPhone(string: String): Boolean {
        return mList.any { it.user == string }
    }

    internal var touchListener: View.OnTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                val upAnim = ObjectAnimator.ofFloat(view, "translationZ", -1f)
                upAnim.duration = 200
                upAnim.interpolator = DecelerateInterpolator()
                upAnim.start()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val downAnim = ObjectAnimator.ofFloat(view, "translationZ", 5f)
                downAnim.duration = 200
                downAnim.interpolator = AccelerateInterpolator()
                downAnim.start()
            }
        }
        false
    }

    val hand = Handler(Looper.getMainLooper())
    override fun onClick(v: View?) {
        val i = mList[v?.tag as Int]
        val tv_OnlineState: TextView = v.findViewById(R.id.mItemList_NumList_OnlineState)
        tv_OnlineState.text = "登录中..."
        presenter.login(i.user, i.pass, object : presenter.Companion.loginCB {
            override fun loginCBS(state: Int, loginResult: loginCallback) {
                hand.post({
                    when (state) {
                        1 -> {
                            val info = presenter.userList[i.user]!!
                            //初次登陆成功必须更新数据
                            mList[v.tag as Int].token = info.token
                            mList[v.tag as Int].areaCode = info.areaCode
                            tv_OnlineState.text = "已登录"
                            val iv: ImageView = v.findViewById(R.id.mItemList_NumList_OnlineStateBack)
                            iv.setImageDrawable(mSContext.getCtx().getDrawable(R.drawable.user_online))
                            val tv_UserName = v.findViewById<TextView>(R.id.mItemList_NumList_UserName)
                            tv_UserName.text = info.customerName
                            val tv_Bill: TextView = v.findViewById(R.id.mItemList_NumList_Acur)
                            tv_Bill.text = "话费:本月消费 ${info.dccBillFee} 元,剩余 ${info.totalMoney} 元"
                            val tv_FlowInfo: TextView = v.findViewById(R.id.mItemList_NumList_Flow)
                            tv_FlowInfo.text = "流量:本月共 ${info.flowInfo.totalFlow} MB,剩余 ${info.flowInfo.leftFlow} MB"
                            v.setOnClickListener(null)
                        }
                        -1 -> tv_OnlineState.text = "登录失败:" + loginResult.TSR_MSG
                        -99 -> tv_OnlineState.text = "登录失败:无网络连接"
                    }
                })
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_numlist, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder.itemView) {
            tag = position
            setOnTouchListener(touchListener)
            if (mList[position].token == null)
                setOnClickListener(this@Adapter_mContent_NumList)
            (findViewById<TextView>(R.id.mItemList_NumList_Numbers)).text =
                    "号码:" + mList[position].user
            val tv_OnlineState: TextView = findViewById(R.id.mItemList_NumList_OnlineState)
            val iv: ImageView = findViewById(R.id.mItemList_NumList_OnlineStateBack)
            var mID = R.drawable.user_offline
            if (mList[position].token != null && mList[position].token != "") {
                mID = R.drawable.user_online
                tv_OnlineState.text = "已登录(Session本地保存,正在尝试刷新数据...)"
                if (!mSContext.hasNet()) {
                    tv_OnlineState.text = "已登录(无网络连接!请联网后下拉刷新!)"
                } else {
                    presenter.loginSingle(mList[position], object : presenter.Companion.refreshCallback {
                        override fun onFlashed() {
                            val info = presenter.userList[mList[position].user]
                            if (info != null) {
                                mID = R.drawable.user_online
                                val tv_UserName = findViewById<TextView>(R.id.mItemList_NumList_UserName)
                                tv_UserName.text = info.customerName
                                val tv_Bill: TextView = findViewById(R.id.mItemList_NumList_Acur)
                                tv_Bill.text = "话费:本月消费 ${info.dccBillFee} 元,剩余 ${info.totalMoney} 元"
                                val tv_FlowInfo: TextView = findViewById(R.id.mItemList_NumList_Flow)
                                tv_FlowInfo.text = "流量:本月共 ${info.flowInfo.totalFlow} MB,剩余 ${info.flowInfo.leftFlow} MB"
                                tv_OnlineState.text = "已登录(刷新数据成功!)"
                            } else {
                                //解决重复刷新登录失败掉线账号的问题
                                mList[position].token = ""
                                presenter.mDB.saveSession(mList[position].user, "", "")
                                mID = R.drawable.user_offline
                                tv_OnlineState.text = "离线(疑似在官方客户端登录,请点击重新登录.)"
                                this@with.setOnClickListener(this@Adapter_mContent_NumList)
                            }
                            iv.setImageDrawable(mSContext.getCtx().getDrawable(mID))
                        }
                    })
                }
            } else {
                tv_OnlineState.text = "未登录"
            }
            iv.setImageDrawable(mSContext.getCtx().getDrawable(mID))
        }
    }

    fun addItem(db: DB_PhoneInfoBean) {
        mList.add(db)
        notifyItemInserted(mList.size - 1)
    }

    class VH(v: View) : RecyclerView.ViewHolder(v)
}