package com.ritesh.newsfeed.data.service


import com.ritesh.newsfeed.BuildConfig
import com.ritesh.newsfeed.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApiService {

    @Headers("Cache-Control: no-cache")
    @GET("v2/top-headlines")
    suspend fun getNewsArticles(
        @Query("country")
        country: String,
        @Query("page")
        page: Int,
        @Query("apiKey")
        apiKey: String = BuildConfig.API_KEY
    ): Response<ApiResponse>


    @GET("v2/top-headlines")
    suspend fun getSearchedTopNewsHeadlines(
        @Query("country")
        country: String,
        @Query("q")
        searchQuery: String,
        @Query("page")
        page: Int,
        @Query("apiKey")
        apiKey: String = BuildConfig.API_KEY
    ): Response<ApiResponse>

}