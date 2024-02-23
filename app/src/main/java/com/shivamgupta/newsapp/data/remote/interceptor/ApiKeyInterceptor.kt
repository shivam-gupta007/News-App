package com.shivamgupta.newsapp.data.remote.interceptor

import com.shivamgupta.newsapp.util.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder: Request.Builder = originalRequest.newBuilder()
            .addHeader("X-Api-Key", Constants.API_KEY)

        //TODO("API_KEY should be retrieved from the local preferences. Here I've just added the API_KEY in the Constants file")

        val modifiedRequest = requestBuilder.build()
        return chain.proceed(modifiedRequest)
    }
}