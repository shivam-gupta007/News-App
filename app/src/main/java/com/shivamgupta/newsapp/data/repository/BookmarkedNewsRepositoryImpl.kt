package com.shivamgupta.newsapp.data.repository

import com.shivamgupta.newsapp.data.local.dao.BookmarkedNewsDao
import com.shivamgupta.newsapp.data.local.entities.BookmarkedNewsEntity
import com.shivamgupta.newsapp.domain.repository.BookmarkNewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookmarkedNewsRepositoryImpl @Inject constructor(
    private val bookmarkedNewsDao: BookmarkedNewsDao
): BookmarkNewsRepository{
    override fun getBookmarkedNews(): Flow<List<BookmarkedNewsEntity>> {
        return bookmarkedNewsDao.fetchBookmarkedNews().flowOn(Dispatchers.IO)
    }

    override suspend fun addOrIgnoreNews(news: BookmarkedNewsEntity) {
        withContext(Dispatchers.IO){
            bookmarkedNewsDao.insertOrIgnoreNews(news)
        }
    }

    override suspend fun deleteNews(newsId: Long) {
        withContext(Dispatchers.IO){
            bookmarkedNewsDao.deleteNews(newsId)
        }
    }

    override suspend fun getBookmarkedNewsByTitle(title: String): BookmarkedNewsEntity? {
        return withContext(Dispatchers.IO){
            bookmarkedNewsDao.fetchBookmarkedNewsByTitle(title)
        }
    }
}