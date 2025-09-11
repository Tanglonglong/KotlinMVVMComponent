package com.example.library_base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.library_base.utils.LogUtil

abstract class BaseBindingFragment<VB : ViewDataBinding>(private val bindingFactory: (LayoutInflater) -> VB) :
    Fragment() {

    val TAG: String get() = this::class.simpleName ?: "BaseBindingFragment"
    lateinit var mBinding: VB

    /**
     * Fragment生命周期 onAttach -> onCreate -> onCreatedView -> onActivityCreated
     * -> onStart -> onResume -> onPause -> onStop -> onDestroyView -> onDestroy
     * -> onDetach 对于 ViewPager + Fragment 的实现我们需要关注的几个生命周期有： onCreatedView +
     * onActivityCreated + onResume + onPause + onDestroyView
     */
    protected var rootView: View? = null

    /**
     * 布局是否创建完成
     */
    protected var isViewCreated: Boolean = false

    /**
     * 当前可见状态
     */
    protected var currentVisibleState: Boolean = false

    /**
     * 是否第一次可见
     */
    protected var mIsFirstVisible: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (null == rootView) {
            mBinding = bindingFactory(layoutInflater)
            rootView = mBinding.root
        }
        isViewCreated = true
        LogUtil.d(TAG, " : onCreateView")
        mBinding.lifecycleOwner = viewLifecycleOwner // 绑定生命周期
        return rootView
    }

}