package com.shivamgupta.newsapp.di

import com.shivamgupta.newsapp.data.remote.pagination.NewsSearchPagingSource
import dagger.assisted.AssistedFactory

@AssistedFactory
interface SearchNewsPagingSourceFactory {
    fun create(query: String): NewsSearchPagingSource
}