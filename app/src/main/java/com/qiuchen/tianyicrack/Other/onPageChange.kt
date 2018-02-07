package com.qiuchen.tianyicrack.Other

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by qiuchen on 2018/2/7.
 */
class onPageChange(val p: PageChangeUtils) : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    private val listMap = HashMap<Int, Boolean>()
    private val listMaps = HashMap<Int, BasePageView>()

    override fun onPageSelected(position: Int) {
        val v = p.getViewss(position)
        val state = listMap[position]
        if (state == null || state == false) {
            when (position) {
                0 -> {
                    listMaps[position] = onePlus(v)
                }
            }
            listMap[position] = true
        }
    }

    fun getViewHelper(position: Int): BasePageView? {
        return listMaps[position]
    }

    interface PageChangeUtils {
        fun getViewss(position: Int): View
    }
}