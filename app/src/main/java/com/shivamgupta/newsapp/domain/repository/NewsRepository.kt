package com.shivamgupta.newsapp.domain.repository

import androidx.paging.PagingData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shivamgupta.newsapp.data.local.entities.NewsEntity
import com.shivamgupta.newsapp.domain.models.NewsArticle
import com.shivamgupta.newsapp.domain.models.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getTopHeadlines(
        fetchFromRemote: Boolean,
        countryCode: String
    ): Flow<Resource<List<NewsEntity>>>

    suspend fun removeAndAddNews(news: List<NewsEntity>)

    fun searchNews(query: String): Flow<PagingData<NewsArticle>>

    fun getBookmarkedNews(): Flow<List<NewsEntity>>

    suspend fun addOrIgnoreNews(news: NewsEntity)

    suspend fun changeNewsBookmarkStatus(newsId: Long,isBookmarked: Boolean)
}