package com.qiuchen.tianyicrack.Adapter

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.*
import com.github.florent37.expansionpanel.ExpansionLayout
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.qiuchen.tianyicrack.Bean.DB_PhoneInfoBean
import com.qiuchen.tianyicrack.Bean.loginCallback
import com.qiuchen.tianyicrack.Presenter.presenter
import com.qiuchen.tianyicrack.R
import com.qiuchen.tianyicrack.mSContext

/**
 * Created by qiuchen on 2018/2/6.
 */
class Adapter_mContent_NumList(val mList: ArrayList<DB_PhoneInfoBean>,
                               val rv: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener, OnChartValueSelectedListener {
    override fun onNothingSelected() {

    }

    override fun onValueSelected(p0: Entry?, p1: Int, p2: Highlight?) {
    }

    fun hasPhone(string: String): Boolean {
        return mList.any { it.user == string }
    }

    private var touchListener: View.OnTouchListener = View.OnTouchListener { view, motionEvent ->
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
        return BaseVH(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun generateCenterSpannableString(num: String): SpannableString {
        val info = presenter.userList[num]?.flowInfo!!
        val tmp1 = (info.leftFlow.toInt() - info.provinceLeftFlow.toInt()).toString()
        val str: String = "流量数据\n" +
                "收费流量剩余:${tmp1}MB\n" +
                "免费流量剩余:${info.provinceLeftFlow}MB\n" +
                "本月已使用量:${info.usedFlow}MB\n" +
                "本月总数据量:${info.totalFlow}MB"
        val a = SpannableString(str)
        a.setSpan(RelativeSizeSpan(1.3f), 0, 4, 0)
        var mFlowS = str.indexOf("收费流量剩余:", 0) + "收费流量剩余:".length
        a.setSpan(ForegroundColorSpan(Color.RED), mFlowS, mFlowS + tmp1.length, 0)
        mFlowS = str.indexOf("免费流量剩余:") + "免费流量剩余:".length
        a.setSpan(ForegroundColorSpan(Color.GRAY), mFlowS, mFlowS + info.provinceLeftFlow.length, 0)
        mFlowS = str.indexOf("本月已使用量:") + "本月已使用量:".length
        a.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), mFlowS, mFlowS + info.usedFlow.length, 0)
        mFlowS = str.indexOf("本月总数据量:") + "本月总数据量:".length
        a.setSpan(ForegroundColorSpan(Color.parseColor("#4a148c")), mFlowS, mFlowS + info.totalFlow.length, 0)
/*        s.setSpan(RelativeSizeSpan(1.7f), 0, 4, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 4, s.length - 5, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 4, s.length - 5, 0)
        s.setSpan(RelativeSizeSpan(.8f), 4, s.length - 5, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 4, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 4, s.length, 0)*/
        return a
    }

    private fun setData(mPieChart: PieChart, mParties: Array<String>, num: String) {
        val info = presenter.userList[num]?.flowInfo!!
        val yVals1 = java.util.ArrayList<Entry>()
        yVals1.add(Entry((info.leftFlow.toInt() - info.provinceLeftFlow.toInt()).toFloat(), 0))
        yVals1.add(Entry(info.provinceLeftFlow.toFloat(), 1))
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        val xVals = java.util.ArrayList<String>()

        (0 until mParties.size).mapTo(xVals) { mParties[it % mParties.size] }

        val dataSet = PieDataSet(yVals1, "")
        dataSet.sliceSpace = 2f
        dataSet.selectionShift = 5f

        // add a lot of colors

        val colors = java.util.ArrayList<Int>()

        colors.add(Color.RED)
        colors.add(Color.GRAY)

        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);

        val data = PieData(xVals, dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        mPieChart.data = data

        // undo all highlights
        mPieChart.highlightValues(null)

        mPieChart.invalidate()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder.itemView) {
            tag = position

            setOnTouchListener(touchListener)
            setOnLongClickListener {
                val v = LayoutInflater.from(context)
                        .inflate(R.layout.alert_contextalert, null)
                val alert_deleteThisAccount: LinearLayout = v.findViewById(R.id.alert_deleteThisAccount)
                val alert_ChangeThisAccount: LinearLayout = v.findViewById(R.id.alert_ChangeThisAccount)
                val alert_FuckThisAccount: LinearLayout = v.findViewById(R.id.alert_FuckThisAccount)

                val alertDialog = AlertDialog.Builder(this.context).create()
                alert_deleteThisAccount.setOnClickListener {
                    try {
                        presenter.mDB.removePhone(mList[position].user)
                        mList.removeAt(position)
                        this@Adapter_mContent_NumList.notifyItemRemoved(position)
                        this@Adapter_mContent_NumList.notifyItemRangeChanged(0, mList.size)
                    } catch (e: Exception) {
                        Toast.makeText(context, "删除手机账户时发生未知错误!", Toast.LENGTH_SHORT)
                                .show()
                    }
                    alertDialog.cancel()
                }
                alert_ChangeThisAccount.setOnClickListener {
                    val changeDialog = AlertDialog.Builder(context).create()
                    val changeView = LayoutInflater.from(context)
                            .inflate(R.layout.alert_change_info, null)

                    val user: EditText = changeView.findViewById(R.id.alert_newAccount)
                    val pass: EditText = changeView.findViewById(R.id.alert_newAccountPass)
                    val saveAccount = changeView.findViewById<Button>(R.id.alert_SaveAccountAndPass)
                    val oldUser = mList[position].user
                    val oldPass = mList[position].pass
                    user.setText(oldUser)
                    pass.setText(oldPass)
                    saveAccount.setOnClickListener {
                        val u = user.text.toString()
                        val p = pass.text.toString()
                        if (u.isNotEmpty() && p.isNotEmpty()) {
                            presenter.mDB.updateAccount(oldUser, u, p)
                            mList[position].user = u
                            mList[position].pass = p
                        }
                        this@Adapter_mContent_NumList.notifyItemChanged(position)
                        changeDialog.cancel()
                    }
                    presenter.mDB.saveSession(mList[position].user, "", "")
                    changeDialog.setView(changeView)
                    changeDialog.show()
                    alertDialog.cancel()
                }
                alert_FuckThisAccount.setOnClickListener { alertDialog.cancel() }
                alertDialog.setView(v)
                alertDialog.show()
                false
            }

            if (mList[position].token == null || mList[position].token == "")
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
                    presenter.loginSingle(mList[position], object : presenter.Companion.RefreshCallback {
                        override fun onFlashed() {
                            val info = presenter.userList[mList[position].user]
                            if (info != null) {
                                mID = R.drawable.user_online
                                val tv_UserName = findViewById<TextView>(R.id.mItemList_NumList_UserName)
                                tv_UserName.text = info.customerName
                                val tv_Bill: TextView = findViewById(R.id.mItemList_NumList_Acur)
                                tv_Bill.text = "话费:剩余 ${info.totalMoney} 元"
                                val tv_FlowInfo: TextView = findViewById(R.id.mItemList_NumList_Flow)
                                tv_FlowInfo.text = "流量:剩余 ${info.flowInfo.provinceLeftFlow} MB"
                                tv_OnlineState.text = "已登录(刷新数据成功!)"

                                //不多BB,成功了直接刷新,不成功不刷新
                                val expansionLayout: ExpansionLayout = findViewById(R.id.expansionLayout)
                                val mPieChart = findViewById<PieChart>(R.id.mItemList_NumList_PieChart)
                                expansionLayout.addListener { expansionLayout, expanded ->
                                    mPieChart.setDescription("流量详细数据")
                                    mPieChart.setUsePercentValues(true)
                                    mPieChart.setExtraOffsets(5f, 5f, 5f, 5f)
                                    mPieChart.dragDecelerationFrictionCoef = 0.95f
                                    mPieChart.centerText = generateCenterSpannableString(mList[position].user)
                                    mPieChart.isDrawHoleEnabled = true
                                    mPieChart.setHoleColorTransparent(true)

                                    mPieChart.setTransparentCircleColor(Color.WHITE)
                                    mPieChart.setTransparentCircleAlpha(110)

                                    mPieChart.holeRadius = 58f
                                    mPieChart.transparentCircleRadius = 61f

                                    mPieChart.setDrawCenterText(true)

                                    // add a selection listener
                                    mPieChart.setOnChartValueSelectedListener(this@Adapter_mContent_NumList)
                                    //mPieChart.data = null

                                    setData(mPieChart, arrayOf("收费流量", "免费流量"), mList[position].user)
                                    // mChart.spin(2000, 0, 360);

                                    val l = mPieChart.legend
                                    l.position = Legend.LegendPosition.RIGHT_OF_CHART
                                    l.xEntrySpace = 7f
                                    l.yEntrySpace = 0f
                                    l.yOffset = 0f
                                }
                            } else {
                                //解决重复刷新登录失败掉线账号的问题
//                                mList[position].token = ""
//                                presenter.mDB.saveSession(mList[position].user, "", "")
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
}