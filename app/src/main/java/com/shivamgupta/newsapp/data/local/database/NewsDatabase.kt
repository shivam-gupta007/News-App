package com.shivamgupta.newsapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shivamgupta.newsapp.data.local.dao.BookmarkedNewsDao
import com.shivamgupta.newsapp.data.local.dao.NewsDao
import com.shivamgupta.newsapp.data.local.entities.BookmarkedNewsEntity
import com.shivamgupta.newsapp.data.local.entities.NewsEntity

@Database(
    entities = [NewsEntity::class, BookmarkedNewsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
    abstract fun getBookmarkedNewsDao(): BookmarkedNewsDao

    companion object {
        const val DB_NAME = "news_database"
    }
}