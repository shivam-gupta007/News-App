package com.shivamgupta.newsapp.data.remote.call_adapter

import com.shivamgupta.newsapp.domain.models.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NetworkResponseCallAdapter(
    private val resultType: Type
): CallAdapter<Type, Call<NetworkResponse<Type>>>{
    override fun responseType(): Type  = resultType

    override fun adapt(call: Call<Type>): Call<NetworkResponse<Type>> {
        return NetworkResponseCall(call)
    }
}

