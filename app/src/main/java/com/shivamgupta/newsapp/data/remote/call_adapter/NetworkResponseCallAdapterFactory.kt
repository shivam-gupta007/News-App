package com.shivamgupta.newsapp.data.remote.call_adapter

import com.shivamgupta.newsapp.domain.models.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResponseCallAdapterFactory private constructor() : CallAdapter.Factory(){
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        //Check API method return type must be of `Call Type`
        val rawType: Type = getRawType(returnType)
        if(rawType != Call::class.java){
            return null
        }

        //Check call type must be of `NetworkResponse` type
        val callType: Type = getParameterUpperBound(0,returnType as ParameterizedType)
        if(getRawType(callType) != NetworkResponse::class.java){
            return null
        }

        //Pass the actual response type
        val resultType: Type = getParameterUpperBound(0,callType as ParameterizedType)
        return NetworkResponseCallAdapter(resultType)
    }

    companion object {
        fun create() = NetworkResponseCallAdapterFactory()
    }
}