package com.example.library_network.manager

import com.example.library_network.api.ApiInterface
import com.example.library_network.constant.BASE_URL


/**
 * @desc   API管理器
 */
object ApiManager {
    val baseApi: ApiInterface by lazy {
        RetrofitManager.getRetrofit(BASE_URL).create(ApiInterface::class.java)
    }

    //此处可扩展其他平台接口

}