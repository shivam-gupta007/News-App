package com.shivamgupta.newsapp.data.remote.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shivamgupta.newsapp.data.remote.services.NewsApiService
import com.shivamgupta.newsapp.domain.models.NetworkResponse
import com.shivamgupta.newsapp.domain.models.NewsArticle
import com.shivamgupta.newsapp.util.ApiException
import com.shivamgupta.newsapp.util.EmptyResultException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class NewsSearchPagingSource @AssistedInject constructor(
    private val apiService: NewsApiService,
    @Assisted private val query: String
): PagingSource<Int,NewsArticle>(){

    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?:
            state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        val position = params.key ?: STARTING_KEY
        val result = apiService.fetchNewsWithQuery(
            searchQuery = query,
            pageNo = position,
            pageSize = LOAD_SIZE
        )

        return when(result){
            is NetworkResponse.Success -> {
                val data = result.data.articles
                val totalPages = result.data.totalResults / LOAD_SIZE
                val prevKey = if(position == STARTING_KEY) null else position - 1
                val nextKey = if(position == totalPages) null else position + 1

                val noResultsOnFirstPage = data.isEmpty() && position == STARTING_KEY
                if (noResultsOnFirstPage) {
                    LoadResult.Error(EmptyResultException("No results found"))
                } else {
                    LoadResult.Page(
                        data = data,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
            }
            is NetworkResponse.Error -> {
                LoadResult.Error(ApiException(errorMessage = result.message))
            }
            is NetworkResponse.Exception -> {
                LoadResult.Error(result.throwable)
            }
        }
    }

    companion object {
        const val STARTING_KEY = 1
        const val LOAD_SIZE = 30
        const val MAX_ITEM_SIZE_IN_MEMORY = 100
    }
}