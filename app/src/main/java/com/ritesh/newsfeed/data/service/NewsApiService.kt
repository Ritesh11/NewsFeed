package com.ritesh.newsfeed.data.service

import com.ritesh.newsfeed.data.model.ApiResponse
import com.ritesh.newsfeed.data.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class NewsApiService(private val client: HttpClient) {

    suspend fun getNewsArticles(
        country: String,
        page: Int
    ): Resource<ApiResponse>{
        return try{
            val response = client.get("v2/top-headlines"){
                header(HttpHeaders.CacheControl, "no-cache")
                url {
                    parameters.append("country", country)
                    parameters.append("page", page.toString())
                }
            }.body<ApiResponse>()
            println("Response: $response")
            Resource.Success(response)
        }catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }

    }
    suspend fun getSearchedTopNewsHeadlines(
        country: String,
        searchQuery: String,
        page: Int
    ): Resource<ApiResponse>{
        return try{
            val response = client.get("v2/top-headlines"){
                header(HttpHeaders.CacheControl, "no-cache")
                url {
                    parameters.append("country", country)
                    parameters.append("q", searchQuery)
                    parameters.append("page", page.toString())
                }
            }.body<ApiResponse>()
            Resource.Success(response)
        }catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }

    }

}