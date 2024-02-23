package com.shivamgupta.newsapp.presentation.bookmark_news

import com.shivamgupta.newsapp.domain.models.News

data class BookmarkNewsUiState(
    val news: List<News> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: String? = null,
)