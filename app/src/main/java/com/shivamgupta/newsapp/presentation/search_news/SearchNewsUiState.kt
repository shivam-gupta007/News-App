package com.shivamgupta.newsapp.presentation.search_news

import androidx.paging.PagingData
import com.shivamgupta.newsapp.domain.models.News

data class SearchNewsUiState(
    val searchedNews: PagingData<News> = PagingData.empty(),
    val searchedText: String? = null,
    val headerTitleText: String? = null
)