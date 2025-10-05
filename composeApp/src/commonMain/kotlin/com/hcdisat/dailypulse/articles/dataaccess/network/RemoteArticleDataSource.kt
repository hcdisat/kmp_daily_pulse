package com.hcdisat.dailypulse.articles.dataaccess.network

import com.hcdisat.dailypulse.articles.domain.ArticleDataSource
import com.hcdisat.dailypulse.articles.domain.Article
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path

class RemoteArticleDataSource(
    private val httpClient: HttpClient,
    private val config: Config = Config()
) : ArticleDataSource {
    override suspend fun fetchArticles(shouldFail: Boolean): Result<List<Article>> = runCatching {
        val response = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = config.baseUrl
                path(config.apiVersion, config.articlesEndpoint)
                parameters.apply {
                    append("category", config.category)
                    append("country", config.country)
                    append("apiKey", config.apiKey)
                }
            }
        }

        val newsArticles = response.body<NewsResponse>()
        newsArticles.toArticles()
    }

    companion object {
        data class Config(
            val apiKey: String = "23726f19e6d54690ab6ccb5333c64813",
            val baseUrl: String = "newsapi.org",
            val apiVersion: String = "v2",
            val articlesEndpoint: String = "top-headlines",
            val category: String = "business",
            val country: String = "us"
        )
    }
}