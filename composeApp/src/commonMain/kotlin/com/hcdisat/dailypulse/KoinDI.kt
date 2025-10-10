package com.hcdisat.dailypulse

import com.hcdisat.dailypulse.articles.di.articlesModule
import com.hcdisat.dailypulse.core.network.di.networkModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            // core components
            networkModule,

            // features
            articlesModule
        )
    }
}