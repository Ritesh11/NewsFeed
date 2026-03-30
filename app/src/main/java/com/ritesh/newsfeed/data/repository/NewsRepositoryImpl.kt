package com.ritesh.newsfeed.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ritesh.newsfeed.data.datasource.LocalDataSource
import com.ritesh.newsfeed.data.datasource.RemoteDataSource
import com.ritesh.newsfeed.data.model.ApiResponse
import com.ritesh.newsfeed.data.model.Article
import com.ritesh.newsfeed.data.model.BookmarkArticle
import com.ritesh.newsfeed.data.util.Resource
import com.ritesh.newsfeed.domain.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.Instant

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : NewsRepository {


    override suspend fun getHeadlines(
        country: String,
        page: Int,
        forceRefresh: Boolean
    ): Flow<Resource<List<Article>>> = flow {
        // Offline First

        // Step 1: Cleanup old data
        localDataSource.deleteFiveDaysOldArticles()

//      Check if we have data locally first
        val localArticles = localDataSource.getCachedArticlesOnce()


//        Step 2: Fetch from Network and save to DB
        try {
            if (localArticles.isEmpty() || forceRefresh) {
                emit(Resource.Loading())
                when (val resource =
                    remoteDataSource.fetchNewsArticleRemoteWithNetworkCheck(country, page)) {
                    is Resource.Success -> {
                        // Save to DB
                        val entity = resource.data?.articles?.map {
                            it.toNewsEntity()
                        }
                        entity?.let {
                            localDataSource.insertArticles(it)
                        }
                    }

                    is Resource.Error -> {
                        emit(Resource.Error(resource.message ?: "Something went wrong"))
                    }

                    else -> {}
                }
            }

        } catch (e: Exception) {
            emit(Resource.Error("Something went wrong"))
        }
//        Collect from Room and emit to the ViewModel
//        Single Source of Truth
        localDataSource.getAllNewsArticles().collect { articles ->
            emit(Resource.Success(articles))
        }

    }.catch { e ->
        emit(Resource.Error(e.localizedMessage ?: "Something went wrong"))
    }

    override suspend fun getSearchedNews(
        country: String,
        searchQuery: String,
        page: Int
    ): Resource<ApiResponse> {
        return remoteDataSource.fetchSearchedNews(country, searchQuery, page)
    }

    override suspend fun saveNews(articles: List<Article>) {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedNews(): Flow<List<Article>> {
        TODO("Not yet implemented")
    }

    override suspend fun bookMarkNews(article: Article) {
        TODO("Not yet implemented")
    }


    fun Article.toNewsEntity() = Article(
        url = url ?: "",
        title = title,
        author = author,
        description = description,
        urlToImage = urlToImage,
        publishedAt = getDaysAgo(publishedAt),
        content = content,
        source = source
    )

    fun Article.toBookmarkEntity() = BookmarkArticle(
        url = url ?: "",
        title = title,
        author = author,
        description = description,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )

    private fun getDaysAgo(date: String?): String {
        return if (date != null && date.isNotBlank()) {
            try {
                val today = Clock.System.todayIn(TimeZone.Companion.currentSystemDefault())
                val days = today.daysUntil(
                    Instant.Companion.parse(date)
                        .toLocalDateTime(TimeZone.Companion.currentSystemDefault()).date
                )

                when {
                    abs(days) > 1 -> "${abs(days)} days ago"
                    abs(days) == 1 -> "Yesterday"
                    else -> "Today"
                }
            } catch (e: Exception) {
                "NA"
            }
        } else {
            "NA"
        }
    }

}