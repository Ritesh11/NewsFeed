package com.ritesh.newsfeed.data.model


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "news_feed",
    indices = [Index(value = ["url"], unique = true)])
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @SerialName("author")
    val author: String?,
    @SerialName("content")
    val content: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("publishedAt")
    val publishedAt: String?,
    @SerialName("source")
    val source: Source?,
    @SerialName("title")
    val title: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("urlToImage")
    val urlToImage: String?,
    val fetchedAt: Long = System.currentTimeMillis()
)