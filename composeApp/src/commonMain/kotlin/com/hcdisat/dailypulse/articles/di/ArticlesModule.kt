package com.hcdisat.dailypulse.articles.di

import com.hcdisat.dailypulse.articles.dataaccess.ArticleDataSource
import com.hcdisat.dailypulse.articles.dataaccess.network.RemoteArticleDataSource
import com.hcdisat.dailypulse.articles.presentation.ArticlesViewModel
import com.hcdisat.dailypulse.core.KermitKtorLogger
import com.hcdisat.dailypulse.getPlatform
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val articlesModule = module {
    factory { providesHttpClient() }
    factory { providesArticleDataSource(get()) }
    factoryOf(::ArticlesViewModel)
}


private fun providesHttpClient(): HttpClient {
    return HttpClient(getPlatform().getHttEngine()).config {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                    coerceInputValues = true
                }
            )
        }

        install(Logging) {
            logger = KermitKtorLogger()
            level = LogLevel.ALL
        }
    }
}

private fun providesArticleDataSource(
    client: HttpClient
): ArticleDataSource {
    return RemoteArticleDataSource(
        httpClient = client,
        config = RemoteArticleDataSource.Companion.Config()
    )
}