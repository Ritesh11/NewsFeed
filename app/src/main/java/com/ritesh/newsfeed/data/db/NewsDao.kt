package com.ritesh.newsfeed.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.ritesh.newsfeed.data.model.Article
import com.ritesh.newsfeed.data.model.BookmarkArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(articles: List<Article>)

    @Query("SELECT * FROM news_feed ORDER BY publishedAt DESC")
    fun getAllNewsFeeds(): Flow<List<Article>>
    @Query("SELECT * FROM news_feed ORDER BY publishedAt DESC")
    suspend fun getCachedArticlesOnce(): List<Article>

    @Insert(onConflict = REPLACE)
    suspend fun insertBookmark(article: BookmarkArticle)

    @Query("SELECT * FROM bookmarks ORDER BY publishedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkArticle>>


    // Delete articles older than 5 days
    @Query("DELETE FROM news_feed WHERE fetchedAt < :threshold")
    suspend fun deleteOldArticles(threshold: Long)

    @Delete
    suspend fun deleteBookmark(article: BookmarkArticle)
}