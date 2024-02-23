package com.shivamgupta.newsapp.domain.models

import com.shivamgupta.newsapp.data.local.entities.BookmarkedNewsEntity
import com.shivamgupta.newsapp.data.local.entities.NewsEntity

fun NewsArticle.asNewsEntity(): NewsEntity{
    return NewsEntity(
        sourceName = source?.name,
        title = title,
        description = description,
        authorName = author,
        articleUrl = url,
        headerImageUrl = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )
}



fun News.asBookmarkedNewsEntity(): BookmarkedNewsEntity {
    return BookmarkedNewsEntity(
        sourceName = sourceName,
        title = title,
        description = description,
        authorName = authorName,
        articleUrl = articleUrl,
        headerImageUrl = headerImageUrl,
        publishedAt = publishedDate,
        content = content,
        isBookmarked = isBookmarked
    )
}

