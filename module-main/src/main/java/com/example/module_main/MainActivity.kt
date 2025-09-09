package com.example.module_main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_base.ui.BaseMvvMActivity
import com.example.library_data.constant.CATEGORIES_FRAGMENT
import com.example.library_data.constant.HOME_FRAGMENT
import com.example.library_data.constant.MAIN_ACTIVITY_HOME
import com.example.library_data.constant.MINE_FRAGMENT
import com.example.library_data.constant.SCHEME_FRAGMENT
import com.example.module_main.adapter.MainPageAdapter
import com.example.module_main.databinding.MainMActivityMainBinding
import com.example.module_main.utils.ColorUtils
import com.example.module_main.viewModels.MainViewModel
import com.gyf.immersionbar.ImmersionBar

import me.majiajie.pagerbottomtabstrip.NavigationController


@Route(path = MAIN_ACTIVITY_HOME)
class MainActivity :
    BaseMvvMActivity<MainMActivityMainBinding, MainViewModel>(MainMActivityMainBinding::inflate) {


    companion object {
        fun start(context: Context, index: Int = 0) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var fragments: MutableList<Fragment?>? = null

    private var adapter: MainPageAdapter? = null

    private var mNavigationController: NavigationController? = null
    
    
    
    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.vm = mViewModel
        ImmersionBar.with(this)
            .statusBarColor(R.color.main_m_white)
            .navigationBarColor(R.color.main_m_white)
            .fitsSystemWindows(true)
            .autoDarkModeEnable(true)
            .init()
        initView()
        initFragment()
    }

    private fun initView() {
        mNavigationController = mBinding.bottomView.material()
            .addItem(
                R.mipmap.main_m_ic_navi_home,
                R.mipmap.main_m_ic_navi_home_select,
                "首页",
                ColorUtils.getColor(this, com.example.module_view.R.color.color_0159a5)
            )
            .addItem(
                R.mipmap.main_m_ic_navi_categories,
                R.mipmap.main_m_ic_navi_categories_select,
                "分类",
                ColorUtils.getColor(this, com.example.module_view.R.color.color_0159a5)
            )
            .addItem(
                R.mipmap.main_m_ic_navi_find,
                R.mipmap.main_m_ic_navi_find_select,
                "体系",
                ColorUtils.getColor(this, com.example.module_view.R.color.color_0159a5)
            )
            .addItem(
                R.mipmap.main_m_ic_navi_mine,
                R.mipmap.main_m_ic_navi_mine_select,
                "我的",
                ColorUtils.getColor(this, com.example.module_view.R.color.color_0159a5)
            )
            .setDefaultColor(
                ColorUtils.getColor(this, com.example.module_view.R.color.color_0159a5)
            )
            .enableAnimateLayoutChanges()
            .build()
        mNavigationController!!.setHasMessage(2, true)
        mNavigationController!!.setMessageNumber(3, 6)
        adapter = MainPageAdapter(
            getSupportFragmentManager(),
            FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
        )
        mBinding.cvContentView.setOffscreenPageLimit(1)
        mBinding.cvContentView.setAdapter(adapter)
        mNavigationController!!.setupWithViewPager(mBinding.cvContentView)
    }

    private fun initFragment() {
        fragments = ArrayList<Fragment?>()
        //通过ARouter 获取其他组件提供的fragment
        val homeFragment = ARouter.getInstance().build(HOME_FRAGMENT)
            .navigation() as Fragment?
        val categoryFragment =
            ARouter.getInstance().build(CATEGORIES_FRAGMENT)
                .navigation() as Fragment?
        val systemFragment = ARouter.getInstance().build(SCHEME_FRAGMENT)
            .navigation() as Fragment?
        val userFragment = ARouter.getInstance().build(MINE_FRAGMENT)
            .navigation() as Fragment?
        fragments!!.add(homeFragment)
        fragments!!.add(categoryFragment)
        fragments!!.add(systemFragment)
        fragments!!.add(userFragment)
        adapter!!.setData(fragments)
    }



}