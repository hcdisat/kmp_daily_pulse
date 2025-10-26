package com.hcdisat.dailypulse.core.dataaccess.network

import com.hcdisat.dailypulse.articles.dataaccess.network.NewsResponse
import com.hcdisat.dailypulse.core.model.NewsCategory
import com.hcdisat.dailypulse.sources.dataaccess.model.SourcesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path

class NewsApiDataSource(
    private val httpClient: HttpClient,
    private val config: Config = Config()
) {
    suspend fun fetchArticles(): Result<NewsResponse> = runCatching {
        val response = httpClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = config.baseUrl
                path(config.apiVersion, config.topHeadlinesEndpoint)
                parameters.apply {
                    append("category", config.category)
                    append("country", config.country)
                    append("apiKey", config.apiKey)
                }
            }
        }

        response.body<NewsResponse>()
    }

    suspend fun getSources(category: NewsCategory? = null): Result<SourcesResponse> = runCatching {
        val request = getDefaultRequest().apply {
            url {
                path(*(encodedPathSegments + config.sourcesEndpoint).toTypedArray())
                category?.let {
                    parameters.append("category", it.value)
                }
            }
        }

        httpClient.get(request).body<SourcesResponse>()
    }

    private fun getDefaultRequest() = HttpRequestBuilder().apply {
        expectSuccess = true
        url {
            protocol = URLProtocol.Companion.HTTPS
            host = config.baseUrl
            path(config.apiVersion, config.topHeadlinesEndpoint)
            parameters.append("apiKey", config.apiKey)
        }
    }

    companion object Companion {
        data class Config(
            val apiKey: String = "23726f19e6d54690ab6ccb5333c64813",
            val baseUrl: String = "newsapi.org",
            val apiVersion: String = "v2",
            val topHeadlinesEndpoint: String = "top-headlines",
            val sourcesEndpoint: String = "sources",
            val category: String = "business",
            val country: String = "us"
        )
    }
}