package com.shivamgupta.newsapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val id: Long?,
    val sourceName: String,
    val title: String,
    val description: String,
    val authorName: String,
    val articleUrl: String,
    val headerImageUrl: String,
    val publishedDate: String,
    val isBookmarked: Boolean,
    val content: String,
): Parcelable {
    fun isArticleUrlValid() = articleUrl.isNotEmpty()
}