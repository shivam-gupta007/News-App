package com.shivamgupta.newsapp.di

import android.app.Application
import androidx.room.Room
import com.shivamgupta.newsapp.data.local.database.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesNewsDatabase(
        app: Application
    ) = Room.databaseBuilder(
        app,
        NewsDatabase::class.java,
        NewsDatabase.DB_NAME
    ).build()

    @Singleton
    @Provides
    fun providesNewsDao(db:NewsDatabase) = db.getNewsDao()
}