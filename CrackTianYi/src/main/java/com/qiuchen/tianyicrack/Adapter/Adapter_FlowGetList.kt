package com.qiuchen.tianyicrack.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.qiuchen.tianyicrack.Presenter.presenter
import com.qiuchen.tianyicrack.R

/**
 * Created by qiuchen on 2018/2/14.
 */
class Adapter_FlowGetList(private val mList: ArrayList<String>) : RecyclerView.Adapter<BaseVH>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = BaseVH(LayoutInflater.from(parent?.context).inflate(R.layout.item_getflow_account, parent, false))
    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        with(holder.itemView) {
            val getFlow_item_detailsRemove = findViewById<TextView>(R.id.getFlow_item_detailsRemove)
            getFlow_item_detailsRemove.setOnClickListener {
                mList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(0, mList.size)
            }
            val getFlow_item_userNick = findViewById<TextView>(R.id.getFlow_item_userNick)
            val getFlow_item_userNum = findViewById<TextView>(R.id.getFlow_item_userNum)
            var str = "无数据(未登录)"
            if (presenter.userList[mList[position]] != null)
                str = presenter.userList[mList[position]]?.customerName.toString()
            getFlow_item_userNick.text = str
            getFlow_item_userNum.text = mList[position]
        }
    }

    fun addPhone(num: String) {
        mList.add(num)
        notifyItemInserted(mList.size)
        notifyItemRangeChanged(0, mList.size)
    }

    fun getNums() = mList
}