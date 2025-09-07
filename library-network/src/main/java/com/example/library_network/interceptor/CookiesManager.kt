package com.example.library_network.interceptor

import com.example.library_base.utils.LogUtil
import com.example.library_base.utils.SPUtil
import kotlin.text.toSet

object CookiesManager {

    /**
     * 保存Cookies
     * @param cookies
     */
    fun saveCookies(cookies: String) {
        SPUtil.put("cookies", cookies.toSet())
    }

    /**
     * 获取Cookies
     * @return cookies
     */
    fun getCookies(): String? {
        return SPUtil.get("cookies", "")
    }

    /**
     * 解析Cookies
     * @param cookies
     */
    fun encodeCookie(cookies: List<String>?): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookies
            ?.map { cookie ->
                cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            ?.forEach {
                it.filterNot { set.contains(it) }.forEach { set.add(it) }
            }
        LogUtil.e("cookiesList:$cookies")
        val ite = set.iterator()
        while (ite.hasNext()) {
            val cookie = ite.next()
            sb.append(cookie).append(";")
        }
        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }
        return sb.toString()
    }


}