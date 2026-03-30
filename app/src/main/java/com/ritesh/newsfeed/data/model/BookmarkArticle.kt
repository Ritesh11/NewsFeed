package com.ritesh.newsfeed.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "bookmarks")
data class BookmarkArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String?,
    val author: String?,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
): Serializable
