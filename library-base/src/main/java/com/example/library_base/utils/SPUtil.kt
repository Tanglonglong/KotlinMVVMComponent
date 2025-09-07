package com.example.library_base.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SPUtil {

        lateinit var sharedPrefs: SharedPreferences

        // 初始化（需在Application中调用）
        @JvmStatic
        @JvmOverloads
        fun init(context: Context, name: String? = "app_sp") {
            sharedPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }

        // 存储数据（支持泛型）
        inline fun <reified T> put(key: String, value: T) = with(sharedPrefs.edit()) {
            when (T::class) {
                String::class -> putString(key, value as String)
                Int::class -> putInt(key, value as Int)
                Boolean::class -> putBoolean(key, value as Boolean)
                Float::class -> putFloat(key, value as Float)
                Long::class -> putLong(key, value as Long)
                else -> throw IllegalArgumentException("Unsupported type")
            }.apply()
        }

        // 获取数据（支持默认值）
        inline fun <reified T> get(key: String, defaultValue: T): T {
            return when (T::class) {
                String::class -> sharedPrefs.getString(key, defaultValue as String) as T
                Int::class -> sharedPrefs.getInt(key, defaultValue as Int) as T
                Boolean::class -> sharedPrefs.getBoolean(key, defaultValue as Boolean) as T
                Float::class -> sharedPrefs.getFloat(key, defaultValue as Float) as T
                Long::class -> sharedPrefs.getLong(key, defaultValue as Long) as T
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }

        // 删除数据
        fun remove(key: String) = sharedPrefs.edit { remove(key) }

        // 清空数据
        fun clear() = sharedPrefs.edit { clear() }


}