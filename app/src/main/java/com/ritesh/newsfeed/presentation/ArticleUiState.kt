package com.ritesh.newsfeed.presentation

import com.ritesh.newsfeed.data.model.ApiResponse
import com.ritesh.newsfeed.data.model.Article

data class ArticleUiState(
    val isLoading: Boolean = false,
    val displayedArticles: List<Article> = emptyList(),
    val pendingArticles: List<Article> = emptyList(),
    val hasNewUpdates: Boolean = false,
    val error: String? = null
)
