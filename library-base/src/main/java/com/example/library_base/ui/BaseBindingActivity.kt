package com.example.library_base.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.library_base.R
import com.gyf.immersionbar.ImmersionBar

abstract class BaseBindingActivity<VB : ViewDataBinding>(private val bindingFactory: (LayoutInflater) -> VB) :
    AppCompatActivity() {

    val TAG: String get() = this::class.simpleName ?: "BaseBindingActivity"
    lateinit var mBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = bindingFactory(layoutInflater)
        setContentView(mBinding.root)
        mBinding.lifecycleOwner = this // 绑定生命周期
        ImmersionBar.with(this)
            .statusBarColor(R.color.white)
            .navigationBarColor(R.color.white)
            .fitsSystemWindows(true)
            .autoDarkModeEnable(true)
            .init()
    }

}