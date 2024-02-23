package com.shivamgupta.newsapp.di

import com.shivamgupta.newsapp.data.repository.NewsRepositoryImpl
import com.shivamgupta.newsapp.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindsNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository
}