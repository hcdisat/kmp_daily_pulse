package com.hcdisat.dailypulse.di

import com.hcdisat.dailypulse.articles.di.articlesModule
import com.hcdisat.dailypulse.core.dataaccess.di.dataAccessModule
import com.hcdisat.dailypulse.sources.di.sourcesModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    val appModules = listOf(
        platformModule,
        dataAccessModule,
        articlesModule,
        sourcesModule,
    )

    startKoin {
        appDeclaration()
        modules(appModules)
    }
}

expect val platformModule: Module