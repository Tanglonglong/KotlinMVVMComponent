package com.example.module_home.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.library_base.ui.BaseMvvMFragment
import com.example.library_base.utils.LogUtil
import com.example.library_base.utils.dpToPx
import com.example.library_data.constant.KEY_ID
import com.example.module_home.adapter.HomeTabItemAdapter
import com.example.module_home.databinding.HomeMTabFragmentBinding
import com.example.module_home.decoration.StaggeredItemDecoration
import com.example.module_home.viewModels.HomeViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

/**
 * @desc   首页资讯列表
 */
class HomeTabFragment : BaseMvvMFragment<HomeMTabFragmentBinding, HomeViewModel>(
    HomeMTabFragmentBinding::inflate
), OnRefreshListener, OnLoadMoreListener {
    private var mPage = 1
    private var mId: Int? = null
    private lateinit var mAdapter: HomeTabItemAdapter

    companion object {
        fun newInstance(id: Int): HomeTabFragment {
            val args = Bundle()
            args.putInt(KEY_ID, id)
            val fragment = HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        initObserve()
    }


    fun initView() {
        mBinding.refreshLayout.apply {
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener(this@HomeTabFragment)
            setOnLoadMoreListener(this@HomeTabFragment)
        }
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mAdapter = HomeTabItemAdapter()
        mBinding.recyclerView.apply {
            layoutManager = manager
            addItemDecoration(StaggeredItemDecoration(dpToPx(10)))
            adapter = mAdapter
        }
        mAdapter.onItemClickListener = { view, position ->

            LogUtil.d("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")
            val item = mAdapter.getItem(position)
            if (item != null && !item.link.isNullOrEmpty()) {
                TabDetailActivity.start(
                    requireContext(), item.link,
                    item.title ?: ""
                )
            }
        }
    }

    fun initData() {
        mId = arguments?.getInt(KEY_ID, 0)
        getProjectItemData()
    }

    fun initObserve() {
        mViewModel.subInfo.observe(viewLifecycleOwner) {
            mBinding.refreshLayout.finishRefresh()
            mBinding.refreshLayout.finishLoadMore()
            if (mPage == 1) {
                mAdapter.setData(it)

                if (it.isNullOrEmpty()) {
                    mBinding.viewEmptyData.visibility = View.VISIBLE
                } else {
                    mBinding.viewEmptyData.visibility = View.GONE
                }
            } else {
                mAdapter.addAll(it)
            }
        }
    }


    override fun getViewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    /**
     * 获取项目列表数据
     */
    private fun getProjectItemData() {
        mViewModel.getProjectList(mPage, mId ?: 0)
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        mPage = 1
        getProjectItemData()
        LogUtil.d(TAG, "ccccccccccccccccccconRefresh:" + this.mPage)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mPage++
        LogUtil.d(TAG, "dddddddddddddddddddddonLoadMore:$mPage")
        getProjectItemData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.d("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH")
    }
}