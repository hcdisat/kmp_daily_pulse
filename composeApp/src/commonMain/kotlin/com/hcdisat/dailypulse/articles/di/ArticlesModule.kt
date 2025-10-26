package com.hcdisat.dailypulse.articles.di

import com.hcdisat.dailypulse.articles.dataaccess.ArticleRepositoryImpl
import com.hcdisat.dailypulse.articles.dataaccess.database.DatabaseArticleDataSource
import com.hcdisat.dailypulse.core.dataaccess.network.NewsApiDataSource
import com.hcdisat.dailypulse.articles.domain.ArticleRepository
import com.hcdisat.dailypulse.articles.domain.usecase.GetArticleUseCase
import com.hcdisat.dailypulse.articles.domain.usecase.RefreshArticlesUseCase
import com.hcdisat.dailypulse.articles.domain.usecase.TransactionHandlerUseCase
import com.hcdisat.dailypulse.articles.presentation.ArticlesViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val articlesModule = module {
    factory {
        ArticlesViewModel(
            getArticles = get(),
            transactionHandler = get(),
            refreshDbArticles = get(),
        )
    }
    factory { NewsApiDataSource(get()) }

    factory {
        NewsApiDataSource(
            httpClient = get(),
            config = NewsApiDataSource.Companion.Config()
        )
    }

    factoryOf(::GetArticleUseCase)
    factoryOf(::RefreshArticlesUseCase)
    factoryOf(::TransactionHandlerUseCase)
    factoryOf(::DatabaseArticleDataSource)
    factoryOf(::ArticleRepositoryImpl).bind<ArticleRepository>()
}
