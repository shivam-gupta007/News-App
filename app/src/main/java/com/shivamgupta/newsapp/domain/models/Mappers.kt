package com.shivamgupta.newsapp.domain.models

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
        isBookmarked = false
    )
}

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
        isBookmarked = isBookmarked,
    )
}

fun News.asNewsEntity(): NewsEntity {
    return NewsEntity(
        sourceName = sourceName,
        title = title,
        description = description,
        authorName = authorName,
        articleUrl = articleUrl,
        headerImageUrl = headerImageUrl,
        publishedAt = publishedDate,
        content = content,
        isBookmarked = isBookmarked,
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