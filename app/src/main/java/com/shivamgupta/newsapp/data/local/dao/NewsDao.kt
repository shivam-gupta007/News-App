package com.shivamgupta.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.shivamgupta.newsapp.data.local.entities.NewsEntity


@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table")
    suspend fun fetchNews(): List<NewsEntity>

    @Query("DELETE FROM news_table")
    suspend fun deleteAllNews()

    @Transaction
    suspend fun deleteAndInsertNews(news: List<NewsEntity>){
        deleteAllNews()
        resetPrimaryKey()
        insertOrIgnoreBunchOfNews(news)
    }

    @Query("DELETE FROM sqlite_sequence WHERE name='news_table'")
    suspend fun resetPrimaryKey()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreBunchOfNews(news: List<NewsEntity>)
}