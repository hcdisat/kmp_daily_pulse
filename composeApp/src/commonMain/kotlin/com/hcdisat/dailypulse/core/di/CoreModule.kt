package com.hcdisat.dailypulse.core.di

import com.hcdisat.dailypulse.DriverFactory
import com.hcdisat.dailypulse.core.dataaccess.database.NewsSourcesDataSource
import com.hcdisat.dailypulse.core.dataaccess.network.KermitKtorLogger
import com.hcdisat.dailypulse.core.dataaccess.network.NewsApiDataSource
import com.hcdisat.dailypulse.core.dataaccess.network.NewsApiDataSource.Companion.Config
import com.hcdisat.dailypulse.database.DailyPulseDatabase
import com.hcdisat.dailypulse.getPlatform
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    factory { providesHttpClient() }
    single<DailyPulseDatabase> { providesDailyPulseDatabase(get()) }
    factory { Config() }

    factoryOf(::NewsApiDataSource)
    singleOf(::NewsSourcesDataSource)
}

private fun providesHttpClient(): HttpClient {
    return HttpClient(getPlatform().getHttEngine()) {
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

private fun providesDailyPulseDatabase(driverFactory: DriverFactory) =
    DailyPulseDatabase(driverFactory.createDriver())