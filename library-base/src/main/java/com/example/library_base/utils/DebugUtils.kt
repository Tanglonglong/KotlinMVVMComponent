package com.example.library_base.utils

import android.os.StrictMode
import com.example.library_base.BuildConfig


object DebugUtils {
    // 调试模式开关（建议在Application中初始化）
    var isDebug = BuildConfig.DEBUG

    // 日志标签统一管理
    private const val GLOBAL_TAG = "AppDebug"

    // 控制台日志输出

    fun logI(message: String, tag: String = GLOBAL_TAG) {
        if (isDebug) LogUtil.i(tag, message)
    }

    fun logV(message: String, tag: String = GLOBAL_TAG) {
        if (isDebug) LogUtil.v(tag, message)
    }

    fun logD(message: String, tag: String = GLOBAL_TAG) {
        if (isDebug) LogUtil.d(tag, message)
    }

    fun logE(message: String, tag: String = GLOBAL_TAG) {
        if (isDebug) LogUtil.e(tag, message)
    }

    // 调试功能开关
    fun enableStrictMode() {
        if (isDebug) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
    }

    // 安全模式检查（防止生产环境误操作）
    fun safeExecute(debugAction: () -> Unit) {
        if (isDebug) debugAction()
    }
}
