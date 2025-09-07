package com.example.module_login.repositories

import com.example.library_base.model.User
import com.example.library_network.manager.ApiManager
import com.example.library_network.repository.BaseRepository


class LoginAndRegisterRepository : BaseRepository(){

    suspend fun register(username: String, password: String, repassword: String): User? {
        return requestResponse {
            ApiManager.baseApi.register(username,password,repassword)
        }
    }


}