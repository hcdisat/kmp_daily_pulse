@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.hcdisat.dailypulse

import app.cash.sqldelight.db.SqlDriver
import io.ktor.client.engine.HttpClientEngine

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

expect class Platform {
    val osName: String
    val osVersion: String
    val deviceModel: String
    val density: Int

    fun logSystemInfo()
    fun getHttEngine(): HttpClientEngine
}

expect fun getPlatform(): Platform