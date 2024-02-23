package com.shivamgupta.newsapp.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.shivamgupta.newsapp.data.remote.call_adapter.NetworkResponseCallAdapterFactory
import com.shivamgupta.newsapp.data.remote.interceptor.ApiKeyInterceptor
import com.shivamgupta.newsapp.data.remote.services.NewsApiService
import com.shivamgupta.newsapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofitClient(): Retrofit {
        val okkHttpClient = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            addInterceptor(ApiKeyInterceptor())
        }

        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .addCallAdapterFactory(NetworkResponseCallAdapterFactory.create())
            .client(okkHttpClient.build())
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl(Constants.BASE_URL)
             //TODO(Here, the BASE_URL retrieved from the constants. However in realtime scenarios, BASE_URL should be accessed from the `buildConfigField` based on the build type (eg. staging, release))
            .build()
    }

    @Provides
    @Singleton
    fun providesNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
}