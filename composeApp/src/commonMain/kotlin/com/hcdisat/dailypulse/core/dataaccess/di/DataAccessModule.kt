package com.hcdisat.dailypulse.core.dataaccess.di

import com.hcdisat.dailypulse.DriverFactory
import com.hcdisat.dailypulse.core.dataaccess.network.KermitKtorLogger
import com.hcdisat.dailypulse.database.DailyPulseDatabase
import com.hcdisat.dailypulse.getPlatform
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataAccessModule = module {
    factory { providesHttpClient() }
    single<DailyPulseDatabase> { providesDailyPulseDatabase(get()) }
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