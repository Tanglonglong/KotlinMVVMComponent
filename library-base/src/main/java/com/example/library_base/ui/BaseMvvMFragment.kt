package com.example.library_base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

abstract class BaseMvvMFragment<VB : ViewDataBinding, VM : ViewModel>(
    private val bindingFactory: (LayoutInflater) -> VB
) : BaseBindingFragment<VB>(bindingFactory) {
    protected lateinit var mViewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this).get(getViewModelClass())
    }

    abstract fun getViewModelClass(): Class<VM>
}