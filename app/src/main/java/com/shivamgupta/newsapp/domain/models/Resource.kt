package com.shivamgupta.newsapp.domain.models


sealed class Resource<out T: Any> {
    data class Loading(val isLoading: Boolean = true): Resource<Nothing>()
    data class Success<T: Any>(val data: T): Resource<T>()
    data class Error(val message: String?): Resource<Nothing>()
    data class Exception(val throwable: Throwable): Resource<Nothing>()
}

fun <T : Any>Resource<T>.onSuccess(
    execute: (data: T) -> Unit
) : Resource<T> = apply{
    if(this is Resource.Success){
        execute(this.data)
    }
}

fun <T : Any>Resource<T>.onError(
    execute: (message: String?) -> Unit
) : Resource<T> = apply{
    if(this is Resource.Error){
        execute(this.message)
    }
}

fun <T : Any>Resource<T>.onException(
    execute: (throwable: Throwable) -> Unit
) : Resource<T> = apply{
    if(this is Resource.Exception){
        execute(this.throwable)
    }
}

fun <T : Any>Resource<T>.onLoading(
    execute: (isLoading: Boolean) -> Unit
) : Resource<T> = apply{
    if(this is Resource.Loading){
        execute(this.isLoading)
    }
}