package com.ritesh.newsfeed.domain.usecase

import com.ritesh.newsfeed.data.model.ApiResponse
import com.ritesh.newsfeed.data.model.Article
import com.ritesh.newsfeed.data.util.Resource
import com.ritesh.newsfeed.domain.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend fun execute(country: String, page: Int, forceRefresh: Boolean): Flow<Resource<List<Article>>> {
        return newsRepository.getHeadlines(country, page, forceRefresh)
    }
}