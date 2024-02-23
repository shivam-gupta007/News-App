package com.shivamgupta.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shivamgupta.newsapp.data.local.dao.NewsDao
import com.shivamgupta.newsapp.data.local.entities.NewsEntity
import com.shivamgupta.newsapp.data.remote.pagination.NewsSearchPagingSource
import com.shivamgupta.newsapp.data.remote.services.NewsApiService
import com.shivamgupta.newsapp.di.NewsSearchPagingSourceFactory
import com.shivamgupta.newsapp.domain.models.NewsArticle
import com.shivamgupta.newsapp.domain.models.Resource
import com.shivamgupta.newsapp.domain.models.asNewsEntity
import com.shivamgupta.newsapp.domain.models.onError
import com.shivamgupta.newsapp.domain.models.onException
import com.shivamgupta.newsapp.domain.models.onSuccess
import com.shivamgupta.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDao: NewsDao
) : NewsRepository {

    @Inject
    lateinit var newsSearchPagingSourceFactory: NewsSearchPagingSourceFactory

    override fun getTopHeadlines(
        fetchFromRemote: Boolean,
        countryCode: String
    ): Flow<Resource<List<NewsEntity>>> = flow {
        emit(Resource.Loading(true))

        try {
            val localNews = newsDao.fetchNews()
            val shouldLoadFromCache = localNews.isNotEmpty() && !fetchFromRemote

            if (shouldLoadFromCache) {
                emit(Resource.Success(data = localNews))
            } else {
                newsApiService.fetchTopHeadlines(countryCode).onSuccess { newsResponse ->
                    val articles = newsResponse.articles.map(NewsArticle::asNewsEntity)
                    removeAndAddNews(articles)
                    emit(Resource.Success(data = newsDao.fetchNews()))
                }.onError { message ->
                    emit(Resource.Error(message = message))
                }.onException {
                    emit(Resource.Exception(throwable = it))
                }
            }
        } catch (e: Exception){
            emit(Resource.Exception(e))
        } finally {
            emit(Resource.Loading(false))
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun removeAndAddNews(news: List<NewsEntity>) {
        withContext(Dispatchers.IO){
            newsDao.deleteAndInsertNews(news)
        }
    }

    override fun searchNews(query: String): Flow<PagingData<NewsArticle>> {
        val searchNewsPagingSource = newsSearchPagingSourceFactory.create(query)
        val pager = Pager(
            config = PagingConfig(
                pageSize = NewsSearchPagingSource.LOAD_SIZE,
                maxSize = NewsSearchPagingSource.MAX_ITEM_SIZE_IN_MEMORY
            ),
            pagingSourceFactory = {
                searchNewsPagingSource
            }
        )

        return pager.flow.flowOn(Dispatchers.IO)
    }

    override fun getBookmarkedNews(): Flow<List<NewsEntity>> {
        return newsDao.fetchBookmarkedNews().flowOn(Dispatchers.IO)
    }

    override suspend fun addOrIgnoreNews(news: NewsEntity) {
        withContext(Dispatchers.IO){
            newsDao.insertOrIgnoreNews(news)
        }
    }

    override suspend fun changeNewsBookmarkStatus(newsId: Long, isBookmarked: Boolean) {
        withContext(Dispatchers.IO){
            newsDao.updateNewsBookmarkStatus(newsId,isBookmarked)
        }
    }
}