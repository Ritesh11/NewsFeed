package com.ritesh.newsfeed.data.datasource

import com.ritesh.newsfeed.data.db.NewsDao
import com.ritesh.newsfeed.data.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val newsDao: NewsDao) {
    suspend fun deleteFiveDaysOldArticles() {
        val fiveDaysInMillis = 5 * 24 * 60 * 60 * 1000L
        val threshold = System.currentTimeMillis() - fiveDaysInMillis
        newsDao.deleteOldArticles(threshold)
    }

    suspend fun insertArticles(articles: List<Article>) {
        newsDao.insert(articles)
    }

    fun getAllNewsArticles(): Flow<List<Article>> {
        return newsDao.getAllNewsFeeds()
    }

    suspend fun getCachedArticlesOnce(): List<Article>{
        return newsDao.getCachedArticlesOnce()

    }
}