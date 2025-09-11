package com.example.module_home.network

import com.example.library_network.response.BaseResponse
import com.example.module_home.model.Banner
import com.example.module_home.model.ProjectSubList
import com.example.module_home.model.ProjectTabItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApiInterface {

    /**
     * 首页轮播图
     */
    @GET("/banner/json")
    suspend fun getHomeBanner(): BaseResponse<MutableList<Banner>>?


    /**
     * 首页项目
     */
    @GET("/project/tree/json")
    suspend fun getProjectTab(): BaseResponse<MutableList<ProjectTabItem>>?

    /**
     * 项目二级列表
     * @param page  分页数量
     * @param cid    项目分类的id
     */
    @GET("/project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseResponse<ProjectSubList>?


}