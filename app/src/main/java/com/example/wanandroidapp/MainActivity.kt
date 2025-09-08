package com.example.wanandroidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.alibaba.android.arouter.launcher.ARouter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().build("/login/RegisterActivity").navigation()
        ARouter.printStackTrace()

    }
}
