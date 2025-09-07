package com.example.library_network.interceptor

import com.example.library_base.utils.LogUtil
import com.example.library_network.constant.KEY_SET_COOKIE
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @desc   Cookies拦截器
 * Cookie会在后续请求中自动附加到请求头，实现用户身份验证（如登录状态维持）
 *无cookie的一般都是基于Token的验证机制
 */
class CookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()
        val response = chain.proceed(newBuilder.build())
        if(request.headers(KEY_SET_COOKIE).isNotEmpty()){
            val cookies = response.headers(KEY_SET_COOKIE)
            val cookiesStr = CookiesManager.encodeCookie(cookies)
            CookiesManager.saveCookies(cookiesStr)
            LogUtil.e("CookiesInterceptor:cookies:$cookies")
        }
        return response
    }
}