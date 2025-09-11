package com.example.library_network.repository


import com.example.library_base.utils.LogUtil
import com.example.library_network.error.ApiException
import com.example.library_network.error.ExceptionHandler
import com.example.library_network.response.BaseResponse
import com.example.library_network.sealed.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * @desc   基础仓库
 */
open class BaseRepository {

    val mTimeOut = 6000L

    /**
     * IO中处理请求
     */
    suspend fun <T> requestResponse(requestCall: suspend () -> BaseResponse<T>?): T? {
        val response = withContext(Dispatchers.IO) {
            withTimeout(mTimeOut) {
                //这边refit okhttp可能抛出http异常
                requestCall()
            }
        } ?: return null

        //已经能拿到数据了，也完层异常抛出ApiException
        if (response.isFailed()) {
            throw ApiException(response.errorCode, response.errorMsg)
        }
        return response.data
    }

    suspend fun <T> requestResponseWithRequestResult(requestCall: suspend () -> BaseResponse<T>?): RequestResult<T?> {
        try {
            // 1. 执行网络请求
            val response = withContext(Dispatchers.IO) {
                withTimeout(mTimeOut) {
                    requestCall()
                }
            }
            response?.let {
                //业务逻辑异常处理
                if (response.isFailed()) {
                    val exception = ApiException(response.errorCode, response.errorMsg)
                    return RequestResult.Error(exception)
                } else {
                    //成功返回数据
                    return RequestResult.Success(response.data)
                }
            }
        } catch (e: Exception) {
            //http异常处理
            val exception = ExceptionHandler.handleException(e)
            LogUtil.v("exception.errCode:" + exception.errCode + " exception.errMsg:" + exception.errMsg)
           return RequestResult.Error(e)
        }
        return RequestResult.Error(null)
    }


    suspend fun <T> requestFlowResponse(requestCall: suspend () -> BaseResponse<T>?): Flow<RequestResult<T?>> {
        //1.执行请求
        val flow = flow {
            //设置超时时间
            val response = withTimeout(mTimeOut) {
                requestCall()
            }
            response?.let {
                //业务逻辑异常处理
                if (response.isFailed()) {
                    val exception = ApiException(response.errorCode, response.errorMsg)
                    emit (RequestResult.Error(exception))
                } else {
                    //成功返回数据
                    emit (RequestResult.Success(response.data))
                }
            }
        //3.指定运行的线程，flow {}执行的线程
        }.flowOn(Dispatchers.IO)
            .onStart {
                //4.请求开始，展示加载框
                emit(RequestResult.Loading)
            }
            //5.捕获异常
            .catch { e ->
                LogUtil.e(e)
                val exception = ExceptionHandler.handleException(e)
                emit(RequestResult.Error(exception))
            }
            //6.请求完成，包括成功和失败
            .onCompletion {
                emit(RequestResult.Completion)
            }
        return flow
    }





}