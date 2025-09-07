package com.example.library_network.interceptor

import android.os.Build
import com.example.library_base.manager.AppManager
import com.example.library_base.utils.LogUtil
import com.example.library_network.BuildConfig
import com.example.library_network.constant.KEY_COOKIE
import okhttp3.Interceptor
import okhttp3.Response
import java.net.URLEncoder

/**
 * @desc   头信息拦截器
 * 添加头信息
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()
        newBuilder.addHeader("Content-type", "application/json; charset=utf-8")

        val host = request.url.host
        val url = request.url.toString()

        //给有需要的接口添加Cookies
        if (host.isNotEmpty()) {
            val cookies = CookiesManager.getCookies()
            LogUtil.e("HeaderInterceptor:cookies:$cookies")
            if (!cookies.isNullOrEmpty()) {
                newBuilder.addHeader(KEY_COOKIE, cookies)
            }
        }
        newBuilder.apply {
            addHeader("os", "android")
            addHeader("appVersionCode", BuildConfig.VERSION_CODE.toString())
            addHeader("device-os-version", Build.VERSION.RELEASE)//获取手机系统版本号
            val deviceNameStr = AppManager.getDeviceBuildBrand()
            addHeader("device-name", URLEncoder.encode(deviceNameStr, "UTF-8"))//获取设备类型
        }
        return chain.proceed(newBuilder.build())
    }
}