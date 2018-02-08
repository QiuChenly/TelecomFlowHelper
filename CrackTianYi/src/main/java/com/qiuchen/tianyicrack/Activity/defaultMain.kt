package com.qiuchen.tianyicrack.Activity

import android.animation.ObjectAnimator
import android.support.design.widget.BottomSheetDialog
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
import com.qiuchen.tianyicrack.Base.BaseApp
import com.qiuchen.tianyicrack.Bean.DB_PhoneInfoBean
import com.qiuchen.tianyicrack.Other.onPageChange
import com.qiuchen.tianyicrack.Other.onePlus
import com.qiuchen.tianyicrack.R
import com.qiuchen.tianyicrack.mSContext
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.activity_default_main.*


class defaultMain : BaseApp(), onPageChange.PageChangeUtils {


    override fun getViewss(position: Int): View {
        return mList[position]
    }

    override fun getParams(vp: viewParams): viewParams {
        return vp.apply {
            this.layout = R.layout.activity_default_main
        }
    }

    lateinit var mList: ArrayList<View>
    lateinit var onPageChanges: onPageChange
    override fun onInit() {
        mContent_Menu.setOnClickListener(this)
        mContent_FB_Add.setOnClickListener(this)

        val views = arrayOf(R.layout.layout_oneplus)
        mList = ArrayList<View>()
        views.mapTo(mList) { LayoutInflater.from(this).inflate(it, null) }
        mContent_VP.adapter = Adapter_ContentVP(mList)
        onPageChanges = onPageChange(this)
        mContent_VP.addOnPageChangeListener(onPageChanges)
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
                    if (users.isNotEmpty() && passs.isNotEmpty() && users.length == 13 && passs.length == 6) {
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
                bt.setOnCancelListener {
                    val animal = ObjectAnimator.ofFloat(mContent_FB_Add, "translationY", 0f)
                    animal.interpolator = AccelerateInterpolator()
                    animal.duration = 200
                    animal.start()
                }
                bt.setContentView(v1)
                bt.show()
                val dp = resources.displayMetrics.heightPixels / resources.displayMetrics.densityDpi * 55f
                val animal = ObjectAnimator.ofFloat(mContent_FB_Add, "translationY", dp)
                animal.interpolator = DecelerateInterpolator()
                animal.duration = 200
                animal.start()
            }
        }
    }
}
