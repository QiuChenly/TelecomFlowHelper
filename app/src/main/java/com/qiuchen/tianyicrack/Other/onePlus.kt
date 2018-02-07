package com.qiuchen.tianyicrack.Other

import android.graphics.Rect
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.qiuchen.tianyicrack.Adapter.Adapter_mContent_NumList
import com.qiuchen.tianyicrack.Bean.DB_PhoneInfoBean
import com.qiuchen.tianyicrack.R
import com.qiuchen.tianyicrack.mSharedContext

/**
 * Created by qiuchen on 2018/2/7.
 */
class onePlus(v: View) : BasePageView(v), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        //TODO 网络请求
    }

    lateinit var numList_Adapter: Adapter_mContent_NumList

    val mContent_SwipeLayout = f<SwipeRefreshLayout>(R.id.mContent_SwipeLayout)
    val mContent_NumList = f<RecyclerView>(R.id.mContent_NumList)

    override fun initView() {
        mContent_SwipeLayout.setOnRefreshListener(this)
        mContent_NumList.layoutManager = LinearLayoutManager(getContext())
        mContent_NumList.setHasFixedSize(false)
        mContent_NumList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.left = 10
                outRect?.right = 10
                outRect?.bottom = 0
                outRect?.top = 10
            }
        })
        numList_Adapter = Adapter_mContent_NumList(mSharedContext.getDB().AllPhone(), mContent_NumList)
        mContent_NumList.adapter = numList_Adapter
    }

    fun addElement(db: DB_PhoneInfoBean) {
        numList_Adapter.addItem(db)
    }

    override fun onClick(v: View?) {

    }
}