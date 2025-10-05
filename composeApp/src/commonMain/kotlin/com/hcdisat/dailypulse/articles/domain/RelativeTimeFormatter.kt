@file:OptIn(ExperimentalTime::class)

package com.hcdisat.dailypulse.articles.domain

import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.*

object RelativeTimeFormatter {
    fun format(instant: Instant, now: Instant = Clock.System.now()): String {
        val duration = now - instant

        return when  {
            duration.isNegative() -> "coming soon"
            duration < 10.seconds -> "just now"
            duration < 1.minutes -> "a few seconds ago"
            duration < 2.minutes -> "a minute ago"
            duration < 1.hours -> "${duration.inWholeMinutes} minutes ago"
            duration < 2.hours -> "an hour ago"
            duration < 1.days -> "${duration.inWholeHours} hours ago"
            else -> {
                val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                val month = localDateTime.month
                val day = localDateTime.day
                val hour = localDateTime.hour.toString().padStart(2,'0')
                val minute = localDateTime.minute.toString().padStart(2, '0')

                "$month $day, $hour:$minute"
            }
        }
    }
}