package com.qiuchen.tianyicrack.Activity

import android.animation.ObjectAnimator
import android.support.design.widget.BottomSheetDialog
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.qiuchen.tianyicrack.Adapter.Adapter_ContentVP
import com.qiuchen.tianyicrack.Adapter.Adapter_NavigationRV
import com.qiuchen.tianyicrack.Base.BaseApp
import com.qiuchen.tianyicrack.Bean.DB_PhoneInfoBean
import com.qiuchen.tianyicrack.Bean.NavigationListItem
import com.qiuchen.tianyicrack.Other.onPageChange
import com.qiuchen.tianyicrack.Other.onePlus
import com.qiuchen.tianyicrack.R
import com.qiuchen.tianyicrack.mSContext
import kotlinx.android.synthetic.main.activity_default_main.*
import kotlinx.android.synthetic.main.activity_navigation.*


class DefaultMain : BaseApp(), onPageChange.PageChangeUtils, ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {

    }

    var fbHeight = 9999
    var nowValue = 0f
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val h = mSContext.getCtx().resources.displayMetrics.heightPixels
        val location: IntArray = kotlin.IntArray(2)
        mContent_FB_Add.getLocationInWindow(location)
        if (location[1] < fbHeight)
            fbHeight = location[1]
        if (position == 0) {
            //获取百分比
            var pcent = (positionOffset * 100).toInt().toFloat()
            if (pcent >= 90f) {
                pcent = 100f
            }
            //小球滚动
            val y = ((h - fbHeight) / 100f) * pcent
            mContent_FB_Add.translationY = y
            if (nowValue > positionOffset) {
                //小球出现
//                println("正在返回第一页")
            } else {
                //小球消失
//                println("正在进入第二页")
            }
            nowValue = positionOffset
        }
    }

    override fun onPageSelected(position: Int) {
    }

    override fun getView(position: Int): View {
        return mList[position]
    }

    override fun getParams(vp: viewParams): viewParams {
        return vp.apply {
            this.layout = R.layout.activity_default_main
        }
    }

    private lateinit var mList: ArrayList<View>
    private lateinit var onPageChanges: onPageChange
    override fun onInit() {
        mContent_Menu.setOnClickListener(this)
        mContent_FB_Add.setOnClickListener(this)


        val title = arrayListOf(NavigationListItem().apply {
            this.mItemName = "账户管理•AccountManager"
        }, NavigationListItem().apply {
            this.mItemName = "流量管理•FlowManager"
        }, NavigationListItem().apply {
            this.mItemName = "一键领取流量•OneKeyFlow"
        })

        mNavigation_List.layoutManager = LinearLayoutManager(this)
        mNavigation_List.setHasFixedSize(true)
        mNavigation_List.adapter = object : Adapter_NavigationRV(title) {
            override fun itemOnClick(position: Int) {
                mContent_VP.currentItem = position
                mContent_DLayout.closeDrawer(Gravity.START)
            }
        }

        val views = arrayOf(R.layout.layout_oneplus, R.layout.layout_flow_express_fuli, R.layout.layout_getflow)
        mList = ArrayList()
        views.mapTo(mList) { LayoutInflater.from(this).inflate(it, null) }
        mContent_VP.adapter = Adapter_ContentVP(mList)
        onPageChanges = object : onPageChange(this) {
            override fun onRecyclerViewScroll(scrollState: Int) {
                if (scrollState == 1) {
                    hideFB()
                } else {
                    showFB()
                }
            }
        }
        mContent_VP.addOnPageChangeListener(onPageChanges)
        mContent_VP.addOnPageChangeListener(this)
        onPageChanges.onPageSelected(0)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mContent_DLayout.isDrawerOpen(Gravity.START)) {
            mContent_DLayout.closeDrawer(Gravity.START)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onClick(v: View) {
        when (v.id) {
            mContent_Menu.id -> {
                mContent_DLayout.openDrawer(Gravity.START)
            }
            mContent_FB_Add.id -> {
                val bt = BottomSheetDialog(this)
                val v1 = LayoutInflater.from(this).inflate(R.layout.bottomsheet_addphone, null, false)
                val add = v1.findViewById<Button>(R.id.mAdd_add)
                val user = v1.findViewById<EditText>(R.id.mAdd_user)
                val pass = v1.findViewById<EditText>(R.id.mAdd_pass)
                add.setOnClickListener({
                    val users = user.text.toString()
                    val passs = pass.text.toString()
                    if (users.isNotEmpty() && passs.isNotEmpty() && users.length == 11 && passs.length == 6) {
                        val element = onPageChanges.getViewHelper(position = 0) as onePlus
                        if (element.hasPhone(users))
                            Toast.makeText(this, "此手机号已存在于本地中!请输入其他手机号!", Toast.LENGTH_SHORT).show()
                        else {
                            val d = DB_PhoneInfoBean()
                            d.user = users
                            d.pass = passs
                            element.addElement(d)
                            mSContext.getDB().addPhone(users, passs)
                            bt.cancel()
                        }
                    } else
                        Toast.makeText(this, "请输入有效数据!", Toast.LENGTH_SHORT)
                                .show()
                })
                bt.setOnCancelListener { hideFB() }
                bt.setContentView(v1)
                showFB()
                bt.show()
            }
        }
    }

    fun showFB() {
        val location: IntArray = kotlin.IntArray(2)
        mContent_FB_Add.getLocationInWindow(location)
        val dp = resources.displayMetrics.heightPixels - location[1] * 1f
        val animal = ObjectAnimator.ofFloat(mContent_FB_Add, "translationY", dp)
        animal.interpolator = DecelerateInterpolator()
        animal.duration = 100
        animal.start()
    }

    fun hideFB() {
        val animal = ObjectAnimator.ofFloat(mContent_FB_Add, "translationY", 0f)
        animal.interpolator = AccelerateInterpolator()
        animal.duration = 100
        animal.start()
    }
}
