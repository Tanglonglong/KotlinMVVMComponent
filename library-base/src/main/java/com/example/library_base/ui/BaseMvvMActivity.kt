package com.example.library_base.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 *
 * 工厂函数实现
 * 通过构造函数传入一个函数：参数类型LayoutInflater 返回值ViewDataBinding
 * 用来得到dataBing
 *
 * **/
abstract class BaseMvvMActivity<VB : ViewDataBinding, VM : ViewModel>
    (private val bindingFactory: (LayoutInflater) -> VB): BaseBindingActivity<VB>(bindingFactory){
    protected lateinit var mViewModel: VM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(getViewModelClass())

    }
    abstract fun getViewModelClass(): Class<VM>
}