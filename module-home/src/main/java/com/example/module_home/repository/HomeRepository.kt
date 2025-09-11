package com.example.module_home.repository

import androidx.lifecycle.LiveData
import com.example.library_network.repository.BaseRepository
import com.example.library_network.sealed.RequestResult
import com.example.module_home.model.Banner
import com.example.module_home.model.ProjectSubList
import com.example.module_home.model.ProjectTabItem
import com.example.module_home.network.HomeApiManager
import kotlinx.coroutines.flow.Flow


/**
 * @desc   首页请求仓库
 */
class HomeRepository : BaseRepository() {
    /**
     * 首页Banner
     */
    suspend fun getHomeBanner(): MutableList<Banner>? {
        return requestResponse {
            HomeApiManager.homeApi.getHomeBanner()
        }
    }


    /**
     * 首页Banner
     * 返回数据封装，包括成功，失败
     */
    suspend fun getHomeBannerResult(): RequestResult<MutableList<Banner>?> {
        return requestResponseWithRequestResult {
            HomeApiManager.homeApi.getHomeBanner()
        }
    }

    /**
     * 首页Banner 用Flow
     * 返回数据封装，包括成功，失败
     */
    suspend fun getHomeBannerResultFlow(): Flow<RequestResult<MutableList<Banner>?>>{
        return requestFlowResponse {
            HomeApiManager.homeApi.getHomeBanner()
        }
    }


    /**
     * 项目tab
     */
    suspend fun getProjectTab(): MutableList<ProjectTabItem>? {
        return requestResponse {
            HomeApiManager.homeApi.getProjectTab()
        }
    }

    /**
     * 项目列表
     * @param page
     * @param cid
     */
    suspend fun getProjectList(page: Int, cid: Int): ProjectSubList? {
        return requestResponse {
            HomeApiManager.homeApi.getProjectList(page, cid)
        }
    }


}