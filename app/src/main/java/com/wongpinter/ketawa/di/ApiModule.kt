package com.wongpinter.ketawa.di

import com.wongpinter.ketawa.data.remote.ApiServiceImpl
import com.wongpinter.ketawa.domain.remote.ApiService
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.jackson.jackson
import javax.inject.Singleton

@dagger.Module
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
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v("HTTP Client Log", null, message)
                    }
                }
                level = LogLevel.HEADERS
            }
            install(ContentNegotiation) {
                jackson()
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiService(httpClient: HttpClient): ApiService {
        return ApiServiceImpl(httpClient)
    }
}