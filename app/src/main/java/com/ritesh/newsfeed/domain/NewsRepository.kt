package com.ritesh.newsfeed.domain

import com.ritesh.newsfeed.data.model.ApiResponse
import com.ritesh.newsfeed.data.model.Article
import com.ritesh.newsfeed.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getHeadlines(country: String, page: Int, forceRefresh: Boolean): Flow<Resource<List<Article>>>

    suspend fun getSearchedNews(country: String, searchQuery: String, page: Int): Resource<ApiResponse>

    suspend fun saveNews(articles: List<Article>)

    suspend fun getSavedNews(): Flow<List<Article>>

    suspend fun bookMarkNews(article: Article)

}