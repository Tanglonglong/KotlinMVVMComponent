package com.example.module_home.viewModels

import android.content.res.AssetManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.library_base.utils.LogUtil
import com.example.library_data.constant.FILE_VIDEO_LIST
import com.example.library_network.error.ApiException
import com.example.library_network.error.ExceptionHandler
import com.example.library_network.sealed.RequestResult
import com.example.module_home.model.Banner
import com.example.module_home.model.ProjectSubInfo
import com.example.module_home.model.ProjectTabItem
import com.example.module_home.model.VideoInfo
import com.example.module_home.repository.HomeRepository
import com.example.module_home.utils.ParseFileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class HomeViewModel : ViewModel() {

    private val _bannerList = MutableLiveData<MutableList<Banner>?>()
    val bannerList: LiveData<MutableList<Banner>?> get() = _bannerList

    private val _tabList = MutableLiveData<MutableList<ProjectTabItem>?>()
    val tabList: LiveData<MutableList<ProjectTabItem>?> get() = _tabList

    private val _subInfo = MutableLiveData<MutableList<ProjectSubInfo>?>()
    val subInfo: LiveData<MutableList<ProjectSubInfo>?> get() = _subInfo

    private val _videoInfo = MutableLiveData<MutableList<VideoInfo>?>()
    val videoInfo: LiveData<MutableList<VideoInfo>?> get() = _videoInfo


    val homeRepository by lazy { HomeRepository() }

    /**
     *
     * 首页banner
     * **/
    fun getBannerList() {
        viewModelScope.launch {
            try {
                _bannerList.value = homeRepository.getHomeBanner()
            } catch (e: Exception) {
                // 处理错误
                _bannerList.value = null
                //http异常转换Api异常，统一api异常处理逻辑
                val exception = ExceptionHandler.handleException(e)
                LogUtil.v("exception.errCode:" + exception.errCode + " exception.errMsg:" + exception.errMsg)
            }
        }
    }

    /**
     * 首页banner
     * 使用liveData{}自带扩展协程封装请求，并将返回值封装到一个密封类里，方便业务层处理
     * **/

    fun getBannerListLiveData(): LiveData<RequestResult<MutableList<Banner>?>> {
        return liveData {
            emit(RequestResult.Loading)
            emit(homeRepository.getHomeBannerResult())
        }
    }

    /**
     * 首页banner
     * 使用Flow封装请求，并将返回值封装到一个密封类里，方便业务层处理
     *
     *
     * **/

    suspend fun getBannerWithFlow(): Flow<RequestResult<MutableList<Banner>?>> {
        return homeRepository.getHomeBannerResultFlow()
    }


    /**
     * 首页Project tab
     */
    fun getProjectTab() {
        viewModelScope.launch {
            try {
                _tabList.value = homeRepository.getProjectTab()
            } catch (e: Exception) {
                // 处理错误
                _tabList.value = null
                //http异常转换Api异常，统一api异常处理逻辑
                val exception = ExceptionHandler.handleException(e)
                LogUtil.v("exception.errCode:" + exception.errCode + " exception.errMsg:" + exception.errMsg)
            }
        }
    }

    /**
     * 获取项目列表数据
     * @param page
     * @param cid
     */
    fun getProjectList(page: Int, cid: Int) {
        viewModelScope.launch {
            try {
                _subInfo.value = homeRepository.getProjectList(page, cid)?.datas
            } catch (e: Exception) {
                // 处理错误
                _subInfo.value = null
                //http异常转换Api异常，统一api异常处理逻辑
                val exception = ExceptionHandler.handleException(e)
                LogUtil.v("exception.errCode:" + exception.errCode + " exception.errMsg:" + exception.errMsg)
            }
        }
    }

    /**
     * 首页视频列表
     */
    fun getVideoList(assetManager: AssetManager) {
        viewModelScope.launch {
            try {
                _videoInfo.value = ParseFileUtils.parseAssetsFile(assetManager, FILE_VIDEO_LIST)
            } catch (e: Exception) {
                // 处理错误
                _videoInfo.value = null
                //http异常转换Api异常，统一api异常处理逻辑
                val exception = ExceptionHandler.handleException(e)
                LogUtil.v("exception.errCode:" + exception.errCode + " exception.errMsg:" + exception.errMsg)
            }
        }
    }


}