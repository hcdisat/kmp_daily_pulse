package com.hcdisat.dailypulse

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hcdisat.dailypulse.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "DailyPulse",
    ) {
        App()
    }
}