package com.example.wanandroidapp

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_base.ui.BaseBindingActivity
import com.example.library_data.constant.MAIN_ACTIVITY_Spl
import com.example.wanandroidapp.databinding.ActivityMainBinding

class MainActivity : BaseBindingActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().build(MAIN_ACTIVITY_Spl).navigation()
        ARouter.printStackTrace()

    }
}
