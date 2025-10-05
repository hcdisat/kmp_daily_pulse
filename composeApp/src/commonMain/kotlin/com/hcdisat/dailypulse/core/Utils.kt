package com.hcdisat.dailypulse.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.hcdisat.dailypulse.articles.domain.RelativeTimeFormatter
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
fun Instant.rememberRelativeState(updateInterval: Duration = 1.minutes): String {
    val now by produceState(initialValue = Clock.System.now(), key1 = updateInterval) {
        while (true) {
            delay(updateInterval)
            value = Clock.System.now()
        }
    }

    return RelativeTimeFormatter.format(this, now)
}

@OptIn(ExperimentalTime::class)
fun String.toInstant() =
    try { Instant.parse(this) } catch (_: Exception) { Clock.System.now() }