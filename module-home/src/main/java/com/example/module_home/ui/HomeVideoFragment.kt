package com.example.module_home.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.library_base.ui.BaseMvvMFragment
import com.example.library_base.utils.LogUtil
import com.example.library_base.utils.dpToPx
import com.example.module_home.adapter.HomeVideoItemAdapter
import com.example.module_home.databinding.HomeMTabVideoFragmentBinding
import com.example.module_home.decoration.StaggeredItemDecoration
import com.example.module_home.viewModels.HomeViewModel
import com.example.module_view.LoadingUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

/**
 * @desc   首页视频列表
 */
class HomeVideoFragment : BaseMvvMFragment<HomeMTabVideoFragmentBinding, HomeViewModel>(
    HomeMTabVideoFragmentBinding::inflate), OnRefreshListener, OnLoadMoreListener {
    lateinit var videoAdapter: HomeVideoItemAdapter

    private val mDialogUtils by lazy {
        LoadingUtils(requireContext())
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
            setOnRefreshListener(this@HomeVideoFragment)
            setOnLoadMoreListener(this@HomeVideoFragment)
        }
        val spanCount = 2
        val manager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        videoAdapter = HomeVideoItemAdapter(requireContext())
        mBinding.recyclerView.apply {
            layoutManager = manager
            addItemDecoration(StaggeredItemDecoration(dpToPx(10)))
            adapter = videoAdapter
        }

        videoAdapter.onItemClickListener = { view: View, position: Int ->
//            RxPermissions(this).request(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ).subscribe { granted ->
//                if (granted) {
//                    ARouter.getInstance().build(VIDEO_ACTIVITY_PLAYER)
//                            .withParcelableArrayList(KEY_VIDEO_PLAY_LIST, videoAdapter.getData() as ArrayList<VideoInfo>)
//                            .navigation()
//                } else {
//                    TipsToast.showTips(com.sum.common.R.string.default_agree_permission)
//                }
//            }
        }
    }

    fun initData() {
        mDialogUtils.showLoading()
        mViewModel.getVideoList(requireContext().assets)
    }

    fun initObserve() {
        mViewModel.videoInfo.observe(viewLifecycleOwner) {
            mBinding.refreshLayout.finishRefresh()
            mBinding.refreshLayout.finishLoadMore()
            mDialogUtils.dismissLoading()
            if (it.isNullOrEmpty()) {
                mBinding.viewEmptyData.visibility = View.VISIBLE
            } else {
                mBinding.viewEmptyData.visibility = View.GONE
                videoAdapter.setData(it)
            }
        }
    }

    override fun getViewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        LogUtil.d(TAG,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaonRefresh")
        mViewModel.getVideoList(requireContext().assets)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        LogUtil.d(TAG,"bbbbbbbbbbbbbbbbbbbbonLoadMore")
        mViewModel.getVideoList(requireContext().assets)
    }
}