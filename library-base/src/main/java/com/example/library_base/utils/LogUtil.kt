package com.example.library_base.utils


import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale.getDefault

object LogUtil {

    private const val TAG = "AppLog"

    // 控制开关
    private var isDebug = false
    private const val MAX_LOG_LENGTH = 4000
    private const val LOG_DIR = "app_logs"
    private val logFormatter = SimpleDateFormat("MM-dd HH:mm:ss.SSS", getDefault())

    // 日志级别枚举
    enum class Level {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }

    // 初始化配置
    @JvmStatic
    fun init(debug: Boolean = true) {
        isDebug = debug
    }

    // 快捷方法
    @JvmStatic
    fun v(msg: String) = log(Level.VERBOSE, null, msg)

    @JvmStatic
    fun v(tag: String, msg: String) = log(Level.VERBOSE, tag, msg)

    @JvmStatic
    fun d(msg: String, tag: String) = log(Level.DEBUG, tag, msg)

    @JvmStatic
    fun d(msg: String) = log(Level.DEBUG, null, msg)

    @JvmStatic
    fun i(tag: String, msg: String) = log(Level.INFO, tag, msg)

    @JvmStatic
    fun i(msg: String) = log(Level.INFO, null, msg)

    @JvmStatic
    fun w(tag: String, msg: String) = log(Level.WARN, tag, msg)

    @JvmStatic
    fun w(msg: String) = log(Level.WARN, null, msg)

    @JvmStatic
    fun e(tag: String, msg: String) = log(Level.ERROR, tag, msg)
    @JvmStatic
    fun e(tag: String,msg: String, throwable: Throwable) = log(Level.ERROR, tag, msg, throwable)
    @JvmStatic
    fun e(msg: String, throwable: Throwable) = log(Level.ERROR, msg, throwable)
    @JvmStatic
    fun e(msg: String) = log(Level.ERROR, null, msg)
    @JvmStatic
    fun e(throwable: Throwable) = log(Level.ERROR, null, throwable)

    // 通用打印方法
    private fun log(level: Level, tag: String?, message: Any?, throwable: Throwable? = null) {
        if (!isDebug) return

        val stackTrace = Thread.currentThread().stackTrace
        val className = stackTrace[4].fileName
        val methodName = stackTrace[4].methodName
        val lineNumber = stackTrace[4].lineNumber
        val fullTag = tag ?: TAG
        val logMsg = buildString {
            append("($methodName:$lineNumber) ")
            append(message?.toString() ?: "NULL")
            throwable?.let { append("\n${Log.getStackTraceString(it)}") }
        }

        // 分段打印超长日志
        if (logMsg.length > MAX_LOG_LENGTH) {
            logMsg.chunked(MAX_LOG_LENGTH).forEach {
                printLog(level, fullTag, it)
            }
        } else {
            printLog(level, fullTag, logMsg)
        }

        saveToFile(level, fullTag, logMsg) // 可选：保存到文件
    }

    private fun printLog(level: Level, tag: String, msg: String) {
        when (level) {
            Level.VERBOSE -> Log.v(tag, msg)
            Level.DEBUG -> Log.d(tag, msg)
            Level.INFO -> Log.i(tag, msg)
            Level.WARN -> Log.w(tag, msg)
            Level.ERROR -> Log.e(tag, msg)
        }
    }

    // 可选：保存日志到文件
    private fun saveToFile(level: Level, tag: String, message: String) {
        // 实现需添加文件写入权限
    }
}
