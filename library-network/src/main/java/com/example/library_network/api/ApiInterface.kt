package com.example.library_network.api

import com.example.library_data.model.User
import com.example.library_network.response.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * @desc   API接口类
 */
interface ApiInterface {


    /**
     * 登录
     * @param username  用户名
     * @param password  密码
     */
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): BaseResponse<User?>?

    /**
     * 注册
     * @param username  用户名
     * @param password  密码
     * @param repassword  确认密码
     */
    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): BaseResponse<User?>?

    /**
     * 登出
     * @param username  用户名
     * @param password  密码
     * @param repassword  确认密码
     */
    @GET("/user/logout/json")
    suspend fun logout(): BaseResponse<Any?>?


}