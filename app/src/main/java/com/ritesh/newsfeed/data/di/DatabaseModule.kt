package com.ritesh.newsfeed.data.di

import android.app.Application
import androidx.room.Room
import com.ritesh.newsfeed.data.db.NewsDao
import com.ritesh.newsfeed.data.db.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(app: Application): NewsDatabase {
        return Room.databaseBuilder(app, NewsDatabase::class.java, "news_db")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideNewsDao(articleDatabase: NewsDatabase): NewsDao {
        return articleDatabase.newsDao()
    }


}