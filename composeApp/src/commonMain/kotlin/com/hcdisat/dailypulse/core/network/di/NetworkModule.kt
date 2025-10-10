package com.hcdisat.dailypulse.core.network.di

import com.hcdisat.dailypulse.core.network.KermitKtorLogger
import com.hcdisat.dailypulse.getPlatform
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    factory { providesHttpClient() }
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