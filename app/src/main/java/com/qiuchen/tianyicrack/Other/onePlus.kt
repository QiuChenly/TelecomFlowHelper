package com.qiuchen.tianyicrack.Other

import android.graphics.Rect
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.qiuchen.tianyicrack.Adapter.Adapter_mContent_NumList
import com.qiuchen.tianyicrack.Bean.DB_PhoneInfoBean
import com.qiuchen.tianyicrack.Presenter.presenter
import com.qiuchen.tianyicrack.R
import com.qiuchen.tianyicrack.mSContext

/**
 * Created by qiuchen on 2018/2/7.
 */
class onePlus(val v: View) : BasePageView(v), SwipeRefreshLayout.OnRefreshListener, presenter.Companion.refreshCallback {
    override fun onFlashed() {
        if (mContent_SwipeLayout.isRefreshing)
            mContent_SwipeLayout.isRefreshing = false
        refreshAllUser()
        //Snackbar.make(v, "刷新数据成功!", Snackbar.LENGTH_SHORT).show()
    }

    override fun onRefresh() {
        if (mSContext.hasNet())
        //presenter.refreshOnlineInfo(this)
            onFlashed()
        else {
            mContent_SwipeLayout.isRefreshing = false
            Snackbar.make(v, "无网络连接!", Snackbar.LENGTH_SHORT).show()
        }
    }

    lateinit var numList_Adapter: Adapter_mContent_NumList
    lateinit var mContent_SwipeLayout: SwipeRefreshLayout
    lateinit var mContent_NumList: RecyclerView
    override fun initView() {
        mContent_SwipeLayout = f(R.id.mContent_SwipeLayout)
        mContent_NumList = f(R.id.mContent_NumList)
        mContent_SwipeLayout.setOnRefreshListener(this)
        mContent_NumList.layoutManager = LinearLayoutManager(getContext())
        mContent_NumList.setHasFixedSize(false)
        mContent_NumList.itemAnimator = DefaultItemAnimator()
        mContent_NumList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.left = 10
                outRect?.right = 10
                outRect?.bottom = 0
                outRect?.top = 10
            }
        })
        numList_Adapter = Adapter_mContent_NumList(mSContext.getDB().AllPhone(), mContent_NumList)
        mContent_NumList.adapter = numList_Adapter
    }

    fun addElement(db: DB_PhoneInfoBean) {
        numList_Adapter.addItem(db)
    }

    fun refreshAllUser() {
        numList_Adapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {

    }
}