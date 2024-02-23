package com.shivamgupta.newsapp.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class ErrorResponse(
    val status: String,
    val message: String?,
    val code: String?
)

@Serializable
data class NewsArticlesResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>
)

@Serializable
data class NewsArticle(
    val source: Source? = null,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null
)

@Serializable
data class Source(
    val id: String? = null,
    val name: String? = null
)


@Serializable
data class TopHeadlineSource(
    val id: String,
    val name: String,
    val description: String?,
    val url: String?,
    val category: String,
    val language: String,
    val country: String
)

@Serializable
data class TopHeadlineSourcesResponse(
    val status: String,
    val sources: List<TopHeadlineSource>
)
