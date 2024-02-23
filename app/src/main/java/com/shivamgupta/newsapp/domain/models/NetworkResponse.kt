package com.shivamgupta.newsapp.domain.models


sealed class NetworkResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResponse<T>()
    data class Error<out T : Any>(val message: String) : NetworkResponse<T>()
    data class Exception<out T : Any>(val throwable: Throwable) : NetworkResponse<T>()
}

suspend fun <T : Any> NetworkResponse<T>.onSuccess(
    execute: suspend (data: T) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Success) {
        execute(this.data)
    }
}

suspend fun <T : Any> NetworkResponse<T>.onError(
    execute: suspend (message: String?) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Error) {
        execute(this.message)
    }
}

suspend fun <T : Any> NetworkResponse<T>.onException(
    execute: suspend (t: Throwable) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Exception) {
        execute(this.throwable)
    }
}