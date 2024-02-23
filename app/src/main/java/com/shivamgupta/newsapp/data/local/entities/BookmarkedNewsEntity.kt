package com.shivamgupta.newsapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_news_table", indices = [Index(value = ["title"], unique = true)])
data class BookmarkedNewsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "source_name") val sourceName: String?,
    val title: String?,
    val description: String?,
    @ColumnInfo(name = "author_name") val authorName: String?,
    @ColumnInfo(name = "article_url") val articleUrl: String?,
    @ColumnInfo(name = "header_img_url") val headerImageUrl: String?,
    @ColumnInfo(name = "published_at") val publishedAt: String?,
    @ColumnInfo(name = "is_bookmarked") val isBookmarked: Boolean,
    val content: String?,
)