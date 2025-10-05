package com.hcdisat.dailypulse

import com.hcdisat.dailypulse.articles.di.articlesModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            articlesModule
        )
    }
}