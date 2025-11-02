package com.hcdisat.dailypulse.articles.di

import com.hcdisat.dailypulse.articles.dataaccess.ArticleRepositoryImpl
import com.hcdisat.dailypulse.articles.dataaccess.database.DatabaseArticleDataSource
import com.hcdisat.dailypulse.articles.domain.ArticleRepository
import com.hcdisat.dailypulse.articles.domain.usecase.GetArticleUseCase
import com.hcdisat.dailypulse.articles.domain.usecase.RefreshArticlesUseCase
import com.hcdisat.dailypulse.articles.domain.usecase.TransactionHandlerUseCase
import com.hcdisat.dailypulse.articles.presentation.ArticlesViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val articlesModule = module {
    factoryOf(::ArticlesViewModel)

    factoryOf(::DatabaseArticleDataSource)
    factoryOf(::ArticleRepositoryImpl).bind<ArticleRepository>()

    factoryOf(::GetArticleUseCase)
    factoryOf(::RefreshArticlesUseCase)
    factoryOf(::TransactionHandlerUseCase)
}
