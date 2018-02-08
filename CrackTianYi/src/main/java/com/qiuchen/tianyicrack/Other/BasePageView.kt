package com.qiuchen.tianyicrack.Other

import android.view.View

/**
 * Created by qiuchen on 2018/2/7.
 */
open abstract class BasePageView(private val v: View) : View.OnClickListener {
    fun <T : View> f(id: Int): T {
        return v.findViewById(id)
    }

    fun <T : View> f(id: Int, click: Boolean): T {
        val v: View = v.findViewById(id)
        if (click)
            v.setOnClickListener(this)
        return v as T
    }

    fun getContext() = v.context

    abstract fun initView()

    init {
        initView()
    }
}