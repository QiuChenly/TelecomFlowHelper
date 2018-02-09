package com.qiuchen.tianyicrack.Adapter

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.qiuchen.tianyicrack.Bean.NavigationListItem
import com.qiuchen.tianyicrack.R
import com.qiuchen.tianyicrack.mSContext

/**
 * Created by qiuchen on 2018/2/8.
 */
abstract class Adapter_NavigationRV(val mList: ArrayList<NavigationListItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val thisItem = mList[position]
        with(holder.itemView) {
            val iv_icon: ImageView = findViewById(R.id.iv_navigation_item_image)
            val tv_title: TextView = findViewById(R.id.iv_navigation_item_title)
            iv_icon.setImageDrawable(getDrawableByID(thisItem.mItemIcoID))
            tv_title.text = thisItem.mItemName
            this.setOnClickListener {
                itemOnClick(position)
            }
        }
    }

    abstract fun itemOnClick(position: Int)

    fun getDrawableByID(id: Int): Drawable {
        return mSContext.getCtx().getDrawable(id)
    }

    override fun getItemCount() = mList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseVH(LayoutInflater.from(parent.context).inflate(R.layout.item_navigation, parent, false))

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}