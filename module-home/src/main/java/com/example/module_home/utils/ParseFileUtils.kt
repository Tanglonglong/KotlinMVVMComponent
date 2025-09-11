package com.example.module_home.utils

import android.content.res.AssetManager
import com.example.library_base.utils.LogUtil
import com.example.library_common.ext.toBean
import com.example.module_home.model.VideoInfo
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import kotlin.coroutines.resumeWithException

/**
 * @desc   解析JSON文件
 * 以异步的方式读取assets目录下的文件，并且适配协程的写法，让他真正挂起函数
 * 方便调用，直接以同步的方法拿到返回值
 */
object ParseFileUtils {
    suspend fun parseAssetsFile(assetManager: AssetManager, fileName: String): MutableList<VideoInfo> {
        return suspendCancellableCoroutine { continuation ->
            try {
                val inputStream = assetManager.open(fileName)
                //逐行读取，不用逐个字节读取
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                val stringBuilder = StringBuilder()
                //kotlin中不允许这样写
//                while ((line = bufferedReader.readLine()) != null) {
//
//                }

                do {
                    line = bufferedReader.readLine()
                    if (line != null) stringBuilder.append(line) else break
                } while (true)

                inputStream.close()
                bufferedReader.close()

                val list = stringBuilder.toString().toBean<MutableList<VideoInfo>>()
                continuation.resumeWith(Result.success(list))
            } catch (e: Exception) {
                e.printStackTrace()
                continuation.resumeWithException(e)
                LogUtil.e(e)
            }
        }
    }
}