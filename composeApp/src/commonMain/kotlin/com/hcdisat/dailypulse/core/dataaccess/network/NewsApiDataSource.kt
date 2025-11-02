package com.hcdisat.dailypulse.core.dataaccess.network

import com.hcdisat.dailypulse.articles.dataaccess.network.NewsResponse
import com.hcdisat.dailypulse.core.model.NewsCategory
import com.hcdisat.dailypulse.sources.dataaccess.model.SourcesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.path

class NewsApiDataSource(
    private val httpClient: HttpClient,
    private val config: Config = Config(),
) {
    suspend fun fetchArticles(source: String): Result<NewsResponse> = runCatching {
        val request = getDefaultRequest { parameters.append(SOURCES, source) }
        httpClient.get(request).body<NewsResponse>()
    }

    suspend fun getSources(category: NewsCategory? = null): Result<SourcesResponse> = runCatching {
        val request = getDefaultRequest {
            path(*(encodedPathSegments + config.sourcesEndpoint).toTypedArray())
            category?.let {
                parameters.append(CATEGORY, it.value)
            }
        }

        httpClient.get(request).body<SourcesResponse>()
    }

    private fun getDefaultRequest(block: URLBuilder.() -> Unit) = HttpRequestBuilder().apply {
        expectSuccess = true
        url { buildUrl(block) }
    }

    private inline fun URLBuilder.buildUrl(block: URLBuilder.() -> Unit): URLBuilder {
        protocol = URLProtocol.Companion.HTTPS
        host = config.baseUrl
        path(config.apiVersion, config.topHeadlinesEndpoint)
        parameters.append(API_KEY, config.apiKey)
        return apply(block)
    }

    companion object Companion {
        private const val SOURCES = "sources"
        private const val API_KEY = "apiKey"
        private const val CATEGORY = "category"

        data class Config(
            val apiKey: String = "23726f19e6d54690ab6ccb5333c64813",
            val baseUrl: String = "newsapi.org",
            val apiVersion: String = "v2",
            val topHeadlinesEndpoint: String = "top-headlines",
            val sourcesEndpoint: String = SOURCES,
        )
    }
}