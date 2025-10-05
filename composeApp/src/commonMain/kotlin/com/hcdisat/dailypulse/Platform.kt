package com.hcdisat.dailypulse

import io.ktor.client.engine.HttpClientEngine

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class Platform {
    val osName: String
    val osVersion: String
    val deviceModel: String
    val density: Int

    fun logSystemInfo()
    fun getHttEngine(): HttpClientEngine
}

expect fun getPlatform(): Platform