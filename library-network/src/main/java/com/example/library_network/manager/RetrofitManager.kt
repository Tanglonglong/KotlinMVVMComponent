package com.example.library_network.manager

import android.util.Log
import com.example.library_base.utils.DebugUtils
import com.example.library_base.utils.NetworkUtils
import com.example.library_network.error.ERROR
import com.example.library_network.error.NoNetWorkException
import com.example.library_network.interceptor.CookiesInterceptor
import com.example.library_network.interceptor.HeaderInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object RetrofitManager {
    private val retrofitCache = ConcurrentHashMap<String, Retrofit>()
    private val defaultClient by lazy {
        initOkHttpClient()
    }

    fun getRetrofit(baseUrl: String): Retrofit {
        return retrofitCache.getOrPut(baseUrl) {
            Retrofit.Builder()
                .baseUrl(baseUrl.ensureEndsWithSlash())
                .client(defaultClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    fun <T> createService(baseUrl: String, apiService: Class<T>): T {
        return getRetrofit(baseUrl).create(apiService)
    }

    private fun String.ensureEndsWithSlash() =
        if (endsWith('/')) this else "$this/"

    private fun initOkHttpClient(): OkHttpClient {
        val build = OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
        build.addInterceptor(CookiesInterceptor())
        build.addInterceptor(HeaderInterceptor())

        //日志拦截器
        val logInterceptor = HttpLoggingInterceptor { message: String ->
            Log.i("okhttp", "data:$message")
        }
        if (DebugUtils.isDebug) {
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        }
        build.addInterceptor(logInterceptor)
        //网络状态拦截
        build.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                if (NetworkUtils.isConnected()) {
                    val request = chain.request()
                    return chain.proceed(request)
                } else {
                    throw NoNetWorkException(ERROR.NETWORD_ERROR)
                }
            }
        })
        return build.build()
    }


}
