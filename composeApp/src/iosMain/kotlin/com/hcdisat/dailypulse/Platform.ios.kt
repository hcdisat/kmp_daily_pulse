package com.hcdisat.dailypulse

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import platform.Foundation.NSLog
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Platform {
    actual val osName: String
        get() = UIDevice.currentDevice.systemName

    actual val osVersion: String
        get() = UIDevice.currentDevice.systemVersion
    actual val deviceModel: String
        get() = UIDevice.currentDevice.model
    actual val density: Int
        get() = UIScreen.mainScreen.scale.toInt()

    actual fun logSystemInfo() {
        NSLog("($osName, $osVersion, $deviceModel, $density)")
    }

    actual fun getHttEngine(): HttpClientEngine = Darwin.create()
}

actual fun getPlatform(): Platform = Platform()