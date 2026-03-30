package com.ritesh.newsfeed.presentation

sealed class NewsArticleEvents {

    data class FetchNewsArticle(
        val country: String,
        val page: Int
    ) : NewsArticleEvents()

    object OnApplyNewUpdates : NewsArticleEvents()
    object Idle : NewsArticleEvents()
}