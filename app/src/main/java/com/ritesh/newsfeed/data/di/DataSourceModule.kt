package com.ritesh.newsfeed.data.di

import com.ritesh.newsfeed.data.datasource.LocalDataSource
import com.ritesh.newsfeed.data.datasource.RemoteDataSource
import com.ritesh.newsfeed.data.db.NewsDao
import com.ritesh.newsfeed.data.service.NewsApiService
import com.ritesh.newsfeed.domain.repository.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        newsApiService: NewsApiService,
        networkMonitor: NetworkMonitor
    ): RemoteDataSource{
        return RemoteDataSource(newsApiService, networkMonitor)

    }


    @Singleton
    @Provides
    fun provideLocalDataSource(newsDao: NewsDao): LocalDataSource {
        return LocalDataSource(newsDao)
    }
}