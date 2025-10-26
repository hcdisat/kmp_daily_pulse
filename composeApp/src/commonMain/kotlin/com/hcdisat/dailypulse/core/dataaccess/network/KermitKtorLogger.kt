package com.hcdisat.dailypulse.core.dataaccess.network

import io.ktor.client.plugins.logging.Logger

private const val KTOR_TAG = "KTOR"
private const val APP_TAG = "DailyPulse"

class KermitKtorLogger: Logger {
    override fun log(message: String) {
        co.touchlab.kermit.Logger.withTag(KTOR_TAG).d(message)
    }
}

fun logger() = co.touchlab.kermit.Logger.withTag(APP_TAG)

