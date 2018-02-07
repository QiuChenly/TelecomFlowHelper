package com.qiuchen.tianyicrack.Adapter

import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import com.qiuchen.tianyicrack.Bean.DB_PhoneInfoBean
import com.qiuchen.tianyicrack.Presenter.presenter
import com.qiuchen.tianyicrack.R

/**
 * Created by qiuchen on 2018/2/6.
 */
class Adapter_mContent_NumList(val mList: ArrayList<DB_PhoneInfoBean>,
                               val rv: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    internal var touchListener: View.OnTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                val upAnim = ObjectAnimator.ofFloat(view, "translationZ", 0f)
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
        presenter.login(i.user, i.pass, object : presenter.Companion.loginCB {
            override fun loginCBS(state: Int) {
                hand.post(Runnable {
                    if (state == 1) {
                        val tv = v.findViewById<TextView>(R.id.mItemList_NumList_UserName)
                        tv.text = presenter.userList[i.user]?.token
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
            //this.findViewById<CardView>(R.id.mItemList_NumList_CDV)setOnClickListener(this@Adapter_mContent_NumList)
        }
    }

    fun addItem(db: DB_PhoneInfoBean) {
        mList.add(0, db)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, mList.size)
        rv.scrollToPosition(0)
    }

    class VH(v: View) : RecyclerView.ViewHolder(v)
}