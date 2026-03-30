package com.ritesh.newsfeed.data.datasource

import com.ritesh.newsfeed.data.model.ApiResponse
import com.ritesh.newsfeed.data.service.NewsApiService
import com.ritesh.newsfeed.data.util.Resource
import com.ritesh.newsfeed.domain.repository.NetworkMonitor
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val newsApiService: NewsApiService,
    private val networkMonitor: NetworkMonitor,
) {


    suspend fun fetchNewsArticleRemoteWithNetworkCheck(
        country: String,
        page: Int
    ): Resource<ApiResponse> {
        val canFetch = networkMonitor.isOnline.first()
        return if (canFetch) {
            responseToResource(newsApiService.getNewsArticles(country, page))
        } else {
            Resource.Error("No Internet Connection")
        }
    }


    suspend fun fetchSearchedNews(
        country: String,
        searchQuery: String,
        page: Int
    ): Resource<ApiResponse> {
        return responseToResource(
            newsApiService.getSearchedTopNewsHeadlines(
                country = country,
                searchQuery = searchQuery,
                page = page
            )
        )
    }


    private fun responseToResource(response: Response<ApiResponse>): Resource<ApiResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                println("${response.body()}")
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}