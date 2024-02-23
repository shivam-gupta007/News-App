package com.shivamgupta.newsapp.data.remote.call_adapter

import com.shivamgupta.newsapp.domain.models.ErrorResponse
import com.shivamgupta.newsapp.domain.models.NetworkResponse
import kotlinx.serialization.json.Json.Default.decodeFromString
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkResponseCall<T : Any>(
    private val proxy: Call<T>
): Call<NetworkResponse<T>> {
    override fun clone(): Call<NetworkResponse<T>> = NetworkResponseCall(proxy)

    override fun execute(): Response<NetworkResponse<T>> {
        throw NotImplementedError()
    }

    override fun isExecuted(): Boolean = proxy.isExecuted

    override fun cancel() {
        proxy.cancel()
    }

    override fun isCanceled(): Boolean = proxy.isCanceled

    override fun request(): Request = proxy.request()

    override fun timeout(): Timeout = proxy.timeout()

    override fun enqueue(callback: Callback<NetworkResponse<T>>) {
        proxy.enqueue(object: Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val networkResponse = handleApi(response)
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResponse = NetworkResponse.Exception<T>(t)
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }

        })
    }

}

private fun <T : Any>handleApi(response: Response<T>): NetworkResponse<T> {
    return try {
        if(response.isSuccessful && response.body() != null){
            NetworkResponse.Success(data = response.body()!!)
        } else {
            val errorBody = response.errorBody()?.charStream()?.readText().toString()
            val errorResponse = decodeFromString(ErrorResponse.serializer(),errorBody)
            NetworkResponse.Error(message = errorResponse.message.orEmpty())
        }
    } catch (e : Exception){
        NetworkResponse.Exception(e)
    }
}