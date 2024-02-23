package com.shivamgupta.newsapp.domain.models

import com.shivamgupta.newsapp.data.local.entities.BookmarkedNewsEntity
import com.shivamgupta.newsapp.data.local.entities.NewsEntity

fun NewsEntity.asNews(): News {
    return News(
        id = id,
        sourceName = sourceName.orEmpty(),
        title = title.orEmpty(),
        description = description.orEmpty(),
        authorName = authorName.orEmpty(),
        articleUrl = articleUrl.orEmpty(),
        headerImageUrl = headerImageUrl.orEmpty(),
        publishedDate = publishedAt.orEmpty(),
        content = content.orEmpty(),
        isBookmarked = false
    )
}

fun BookmarkedNewsEntity.asNews(): News {
    return News(
        id = id,
        sourceName = sourceName.orEmpty(),
        title = title.orEmpty(),
        description = description.orEmpty(),
        authorName = authorName.orEmpty(),
        articleUrl = articleUrl.orEmpty(),
        headerImageUrl = headerImageUrl.orEmpty(),
        publishedDate = publishedAt.orEmpty(),
        content = content.orEmpty(),
        isBookmarked = isBookmarked
    )
}

fun NewsArticle.asNews(): News {
    return News(
        id = null,
        sourceName = source?.name.orEmpty(),
        title = title.orEmpty(),
        description = description.orEmpty(),
        authorName = author.orEmpty(),
        articleUrl = url.orEmpty(),
        headerImageUrl = urlToImage.orEmpty(),
        publishedDate = publishedAt.orEmpty(),
        content = content.orEmpty(),
        isBookmarked = false,
    )
}