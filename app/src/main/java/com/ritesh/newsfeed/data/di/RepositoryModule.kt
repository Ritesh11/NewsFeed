package com.ritesh.newsfeed.data.di

import com.ritesh.newsfeed.data.datasource.LocalDataSource
import com.ritesh.newsfeed.data.datasource.RemoteDataSource
import com.ritesh.newsfeed.data.repository.NewsRepositoryImpl
import com.ritesh.newsfeed.domain.NewsRepository
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
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): NewsRepository {
        return NewsRepositoryImpl(remoteDataSource, localDataSource)
    }
}