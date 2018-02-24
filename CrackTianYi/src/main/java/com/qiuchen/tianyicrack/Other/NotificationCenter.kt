package com.qiuchen.tianyicrack.Other

import android.graphics.Rect
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.qiuchen.tianyicrack.R

/**
 * Created by qiuchen on 2018/2/9.
 */
class NotificationCenter(v: View) : BasePageView(v) {


    lateinit var notificationRV: RecyclerView
    lateinit var notificationAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    override fun initView() {
        notificationRV = f(R.id.mNotificationCenterRV)

        with(notificationRV) {
            layoutManager = LinearLayoutManager(this@NotificationCenter.getContext())
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(false)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                    outRect?.bottom = 7
                }
            })
            adapter = notificationAdapter
        }
    }

    override fun onClick(v: View) {

    }
}