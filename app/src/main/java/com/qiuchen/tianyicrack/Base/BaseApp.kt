package com.qiuchen.tianyicrack.Base

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * Created by qiuchen on 2018/2/6.
 */
abstract class BaseApp : AppCompatActivity(), View.OnClickListener {

    private var mViewParams = getParams()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewParams = getParams()
        setContentView(mViewParams.layout)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else
            onInit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        onInit()
    }

    abstract fun onInit()

    /**
     * 获取部分初始化参数
     * @param vp 提供部分数据在这里,并返回Self
     * @return 返回vp
     */
    abstract fun getParams(vp: viewParams = viewParams()): viewParams

    /**
     * 一个自定义的视图布局参数配置类
     */
    class viewParams {
        var layout = 0
    }
}