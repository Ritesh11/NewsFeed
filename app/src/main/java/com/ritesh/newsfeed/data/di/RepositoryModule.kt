package com.ritesh.newsfeed.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ritesh.newsfeed.data.datasource.LocalDataSource
import com.ritesh.newsfeed.data.datasource.RemoteDataSource
import com.ritesh.newsfeed.data.repository.NewsRepositoryImpl
import com.ritesh.newsfeed.data.db.NewsDao
import com.ritesh.newsfeed.data.service.NewsApiService
import com.ritesh.newsfeed.domain.NewsRepository
import com.ritesh.newsfeed.domain.repository.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {


    @Provides
    @Singleton
    fun providesNewsRepository(
        dataStore: DataStore<Preferences>,
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): NewsRepository {
        return NewsRepositoryImpl(dataStore, remoteDataSource, localDataSource)
    }
}