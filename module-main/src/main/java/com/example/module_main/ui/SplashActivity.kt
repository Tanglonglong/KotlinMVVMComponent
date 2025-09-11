package com.example.module_main.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.library_common.ext.countDownCoroutines
import com.example.library_base.ui.BaseBindingActivity
import com.example.library_data.constant.MAIN_ACTIVITY_Spl
import com.example.module_main.MainActivity
import com.example.module_main.R
import com.example.module_main.databinding.MainMActivitySplashBinding

@Route(path = MAIN_ACTIVITY_Spl)
class SplashActivity : BaseBindingActivity<MainMActivitySplashBinding>(
    MainMActivitySplashBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.tvSkip.setOnClickListener {
            MainActivity.start(this@SplashActivity, index = 0);
        }
        //倒计时
        countDownCoroutines(2, lifecycleScope, onTick = {
            mBinding.tvSkip.text = getString(R.string.main_m_splash_time, it.plus(1).toString())
        }) {
             MainActivity.start(this@SplashActivity, index = 0);
            finish()
        }
    }
}