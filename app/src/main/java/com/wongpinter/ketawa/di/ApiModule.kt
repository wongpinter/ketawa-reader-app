package com.wongpinter.ketawa.di

import com.wongpinter.ketawa.data.remote.ApiServiceImpl
import com.wongpinter.ketawa.domain.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideBaseUrl(): String = "api.ketawa.com"

    @Provides
    @Singleton
    fun provideHttpClient(baseUrl: String): HttpClient {
        return HttpClient(CIO) {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = baseUrl
                }
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        prettyPrint = false
                        coerceInputValues = true
                    }
                )
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiService(httpClient: HttpClient): ApiService {
        return ApiServiceImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideJokeRepository(
        httpClient: HttpClient,
        jokeDao: com.wongpinter.ketawa.data.local.dao.JokeDao
    ): com.wongpinter.ketawa.domain.repository.JokeRepository {
        return com.wongpinter.ketawa.data.repository.JokeRepositoryImpl(httpClient, jokeDao)
    }
}
