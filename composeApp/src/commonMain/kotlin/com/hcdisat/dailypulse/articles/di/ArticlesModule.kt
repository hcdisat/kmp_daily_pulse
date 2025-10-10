package com.hcdisat.dailypulse.articles.di

import com.hcdisat.dailypulse.articles.domain.ArticleDataSource
import com.hcdisat.dailypulse.articles.dataaccess.network.RemoteArticleDataSource
import com.hcdisat.dailypulse.articles.domain.usecase.GetArticleUseCase
import com.hcdisat.dailypulse.articles.presentation.ArticlesViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val articlesModule = module {
    factory { providesArticleDataSource(get()) }
    factoryOf(::GetArticleUseCase)
    factory { ArticlesViewModel(get()) }
}

private fun providesArticleDataSource(
    client: HttpClient
): ArticleDataSource {
    return RemoteArticleDataSource(
        httpClient = client,
        config = RemoteArticleDataSource.Companion.Config()
    )
}