package com.example.library_network.sealed

sealed class RequestResult<out T> {
    data class Success<T>(val data: T) : RequestResult<T>()
    data class Error(val exception: Throwable?) : RequestResult<Nothing>()
    object Loading : RequestResult<Nothing>()
    object Completion : RequestResult<Nothing>()
}