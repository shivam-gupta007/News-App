package com.shivamgupta.newsapp.data.remote.services

import com.shivamgupta.newsapp.data.remote.utils.Endpoints
import com.shivamgupta.newsapp.domain.models.NetworkResponse
import com.shivamgupta.newsapp.domain.models.NewsArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET(Endpoints.PATH_TOP_HEADLINES)
    suspend fun fetchTopHeadlines(
        @Query("country") countryCode: String,
    ): NetworkResponse<NewsArticlesResponse>

    @GET(Endpoints.PATH_NEWS_BY_QUERY)
    suspend fun searchNewsByQuery(
        @Query("q") searchQuery: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
    ): NetworkResponse<NewsArticlesResponse>

}