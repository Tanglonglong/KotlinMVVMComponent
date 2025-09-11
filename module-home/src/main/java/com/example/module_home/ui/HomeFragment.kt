package com.example.module_home.ui

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.example.library_base.ui.BaseMvvMFragment
import com.example.library_data.constant.HOME_FRAGMENT
import com.example.module_home.adapter.ViewPage2FragmentAdapter
import com.example.module_home.databinding.HomeMFragmentHomeBinding
import com.example.module_home.holder.BannerHolder
import com.example.module_home.model.ProjectTabItem
import com.example.module_home.viewModels.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout
import androidx.core.util.size
import androidx.lifecycle.lifecycleScope
import com.example.library_base.utils.LogUtil
import com.example.library_network.sealed.RequestResult
import com.example.module_home.model.Banner
import kotlinx.coroutines.launch


@Route(path = HOME_FRAGMENT)
class HomeFragment : BaseMvvMFragment<HomeMFragmentHomeBinding, HomeViewModel>
    (HomeMFragmentHomeBinding::inflate) {

    private var mTabLayoutMediator: TabLayoutMediator? = null
    private var mFragmentAdapter: ViewPage2FragmentAdapter? = null
    private var mProjectTabs: MutableList<ProjectTabItem> = mutableListOf()

    private val mArrayTabFragments = SparseArray<Fragment>()


    init {
        val videoItem = ProjectTabItem(1, "短视频")
        mProjectTabs.add(videoItem)
        mArrayTabFragments.append(0, HomeVideoFragment())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.vm = mViewModel
        mFragmentAdapter =
            ViewPage2FragmentAdapter(childFragmentManager, lifecycle, mArrayTabFragments)
        LogUtil.d("onViewCreatedonViewCreatedMMMMMMMMMMMMMMMMMMMMMMMMM")
        initObserve()
        initData()
    }

    override fun getViewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    fun initObserve() {
        //这个是监听viewMode里面直接定义的liveData变化来处理
        mViewModel.bannerList.observe(viewLifecycleOwner) { banners ->
            banners?.let {
                mBinding.bannerHome.visibility = View.VISIBLE
                mBinding.bannerHome.setPages(CBViewHolderCreator {
                    BannerHolder()
                }, banners).apply {
                    setPointViewVisible(true)
                    startTurning(5000)
                }
            } ?: kotlin.run {
                mBinding.bannerHome.visibility = View.GONE
            }
        }

        mViewModel.tabList.observe(viewLifecycleOwner) { tabs ->

            tabs?.forEachIndexed { index, item ->
                mProjectTabs.add(item)
                mArrayTabFragments.append(index + 1, HomeTabFragment.newInstance(tabs[index].id))
            }
            mBinding.let {
                it.viewPager.adapter = mFragmentAdapter
                //可左右滑动
                it.viewPager.isUserInputEnabled = true
                it.viewPager.offscreenPageLimit = 2
            }
            mTabLayoutMediator = TabLayoutMediator(
                mBinding.tabHome,
                mBinding.viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = mProjectTabs[position].name
            }
            //tabLayout和viewPager2关联起来
            mTabLayoutMediator?.attach()
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.bannerHome.startTurning(5000)
    }

    override fun onPause() {
        super.onPause()
        mBinding.bannerHome.stopTurning()
    }


    private fun initData() {
        mViewModel.getProjectTab()
        //mViewModel.getBannerList()
        //这个无需在viewModel里面定义livedata，直接方法返回的就是，并且对于成功失败有封装
//        mViewModel.getBannerListLiveData().observe(viewLifecycleOwner) { result ->
//            bannerDataProcess(result)
//        }

        //Flow请求
        lifecycleScope.launch {
            mViewModel.getBannerWithFlow().collect {
                    result ->
                bannerDataProcess(result)
            }
        }
    }
    fun bannerDataProcess(result: RequestResult<MutableList<Banner>?>){
        when (result) {
            is RequestResult.Loading -> {
                //显示加载框等操作
            }
            is RequestResult.Success -> {
                mBinding.bannerHome.visibility = View.VISIBLE
                mBinding.bannerHome.setPages(CBViewHolderCreator {
                    BannerHolder()
                }, result.data).apply {
                    setPointViewVisible(true)
                    startTurning(5000)
                }
            }
            is RequestResult.Error -> {
                mBinding.bannerHome.visibility = View.GONE
                LogUtil.d(TAG,"getBannerListLiveData Error:${result.exception}")
            }
            is RequestResult.Completion -> {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTabLayoutMediator?.detach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.d("onDestroyViewonDestroyViewonDestroyViewFFFFFFFFFFFFFFFFFFFF")
        mBinding.viewPager.adapter = null
    }

}