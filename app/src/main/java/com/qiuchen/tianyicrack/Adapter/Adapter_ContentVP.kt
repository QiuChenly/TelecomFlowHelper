package com.qiuchen.tianyicrack.Adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * Created by qiuchen on 2018/2/7.
 */
class Adapter_ContentVP(val viewList: ArrayList<View>) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`
    override fun getCount(): Int = viewList.size
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = viewList[position]
        container.addView(v)
        return v
    }
}