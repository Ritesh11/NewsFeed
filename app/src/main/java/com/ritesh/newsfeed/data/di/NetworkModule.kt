package com.ritesh.newsfeed.data.di

import android.content.Context
import com.ritesh.newsfeed.BuildConfig
import com.ritesh.newsfeed.data.repository.NetworkMonitorImpl
import com.ritesh.newsfeed.data.service.NewsApiService
import com.ritesh.newsfeed.domain.repository.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitorImpl(context)
    }


    @Provides
    @Singleton
    fun providesKtorClient(): HttpClient{
        return HttpClient(OkHttp){
            install(ContentNegotiation){
                json(Json {
                    // 1. Ignore extra fields the API might add in the future
                    ignoreUnknownKeys = true
                    // 2. Be lenient with malformed JSON (like missing quotes)
                    isLenient = true
                    // 3. Handle cases where a key is missing from the JSON entirely
                    explicitNulls = false
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }

            defaultRequest{
                url(BuildConfig.BASE_URL)
                url.parameters.append("apiKey", BuildConfig.API_KEY)
            }

            install(Logging){
                level = LogLevel.INFO
            }
        }
    }

    @Provides
    @Singleton
    fun providesNewsApiService(client: HttpClient): NewsApiService {
        return NewsApiService(client)
    }

}