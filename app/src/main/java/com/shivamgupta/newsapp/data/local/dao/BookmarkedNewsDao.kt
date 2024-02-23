package com.shivamgupta.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shivamgupta.newsapp.data.local.entities.BookmarkedNewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedNewsDao {
    @Query("SELECT * FROM bookmarked_news_table WHERE is_bookmarked = :bookmarked ORDER BY id DESC")
    fun fetchBookmarkedNews(bookmarked: Boolean = true): Flow<List<BookmarkedNewsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreNews(news: BookmarkedNewsEntity)

    @Query("DELETE FROM bookmarked_news_table WHERE id =:newsId")
    suspend fun deleteNews(newsId: Long)

    @Query("SELECT * FROM bookmarked_news_table WHERE title =:title LIMIT 1")
    suspend fun fetchBookmarkedNewsByTitle(title: String): BookmarkedNewsEntity?
}