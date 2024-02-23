package com.shivamgupta.newsapp.domain.repository

import com.shivamgupta.newsapp.data.local.entities.BookmarkedNewsEntity
import kotlinx.coroutines.flow.Flow

interface BookmarkNewsRepository {
    fun getBookmarkedNews(): Flow<List<BookmarkedNewsEntity>>

    suspend fun getBookmarkedNewsByTitle(title: String): BookmarkedNewsEntity?

    suspend fun addOrIgnoreNews(news: BookmarkedNewsEntity)

    suspend fun deleteNews(newsId: Long)
}