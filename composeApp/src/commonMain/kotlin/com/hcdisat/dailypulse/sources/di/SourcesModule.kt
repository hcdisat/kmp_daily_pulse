package com.hcdisat.dailypulse.sources.di

import com.hcdisat.dailypulse.sources.dataaccess.NewsSourceRepositoryImpl
import com.hcdisat.dailypulse.sources.dataaccess.database.NewsSourcesDataSource
import com.hcdisat.dailypulse.sources.domain.NewsSourceRepository
import com.hcdisat.dailypulse.sources.domain.usecase.GetSourceConfigUseCase
import com.hcdisat.dailypulse.sources.domain.usecase.LoadNewsSourcesUseCase
import com.hcdisat.dailypulse.sources.domain.usecase.UpdateCategoryUseCase
import com.hcdisat.dailypulse.sources.domain.usecase.UpdateNewsSourceUseCase
import com.hcdisat.dailypulse.sources.presentation.NewsSourceViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sourcesModule = module {
    factoryOf(::NewsSourceViewModel)

    singleOf(::NewsSourcesDataSource)
    single<NewsSourceRepository> {
        NewsSourceRepositoryImpl(
            dataSource = get(),
            newsApi = get()
        )
    }

    factoryOf(::GetSourceConfigUseCase)
    factoryOf(::UpdateCategoryUseCase)
    factoryOf(::LoadNewsSourcesUseCase)
    factoryOf(::UpdateNewsSourceUseCase)

    single<CoroutineDispatcher> { Dispatchers.IO }
}