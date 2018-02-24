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
abstract class UserManagerPage(val v: View) : BasePageView(v), SwipeRefreshLayout.OnRefreshListener, presenter.Companion.RefreshCallback {
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
                outRect?.left = 15
                outRect?.right = 15
                outRect?.bottom = 10
                outRect?.top = 10
            }
        })
        mContent_NumList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy < 0 && !stateUP) {
                    stateUP = true
                    stateDown = false
                    onRecyclerViewScrolled(1)
                } else if (dy > 0 && !stateDown) {
                    stateDown = true
                    stateUP = false
                    onRecyclerViewScrolled(0)
                }
            }
        })
        numList_Adapter = Adapter_mContent_NumList(mSContext.getDB().AllPhone(), mContent_NumList)
        mContent_NumList.adapter = numList_Adapter
    }

    var stateUP = false
    var stateDown = false


    /**
     * 触发滑动事件
     * @param scrollState  upScroll = 1 downScroll = 0
     */
    abstract fun onRecyclerViewScrolled(scrollState: Int)

    fun addElement(db: DB_PhoneInfoBean) {
        numList_Adapter.addItem(db)
    }

    fun hasPhone(string: String): Boolean {
        return numList_Adapter.hasPhone(string)
    }

    fun refreshAllUser() {
        numList_Adapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {

    }
}