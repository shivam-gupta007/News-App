package com.shivamgupta.newsapp.presentation.news_feed

import com.shivamgupta.newsapp.domain.models.News

data class LatestNewsUiState(
    val news: List<News> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: String? = null,
)





