package com.example.module_home.network

import com.example.library_network.constant.BASE_URL
import com.example.library_network.manager.RetrofitManager

object HomeApiManager {


    val homeApi: HomeApiInterface by lazy {
        RetrofitManager.getRetrofit(BASE_URL).create(HomeApiInterface::class.java)
    }


}