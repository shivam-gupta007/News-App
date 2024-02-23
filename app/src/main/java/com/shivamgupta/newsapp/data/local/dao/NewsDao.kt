package com.shivamgupta.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.shivamgupta.newsapp.data.local.database.NewsDatabase
import com.shivamgupta.newsapp.data.local.entities.NewsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface NewsDao {

    @Query("SELECT * FROM ${NewsDatabase.NEWS_TABLE}")
    suspend fun fetchNews(): List<NewsEntity>

    @Query("DELETE FROM ${NewsDatabase.NEWS_TABLE}")
    suspend fun deleteAllNews()

    @Transaction
    suspend fun deleteAndInsertNews(news: List<NewsEntity>){
        deleteAllNews()
        insertOrIgnoreBunchOfNews(news)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreBunchOfNews(news: List<NewsEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreNews(news: NewsEntity)

    @Query("UPDATE news_table SET is_bookmarked =:isBookmarked WHERE id =:newsId")
    suspend fun updateNewsBookmarkStatus(newsId: Long,isBookmarked: Boolean)

    @Query("SELECT * FROM ${NewsDatabase.NEWS_TABLE} WHERE is_bookmarked =:isBookmarked")
    fun fetchBookmarkedNews(isBookmarked: Boolean = true): Flow<List<NewsEntity>>
}