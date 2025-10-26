@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.hcdisat.dailypulse

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.hcdisat.dailypulse.core.Constants
import com.hcdisat.dailypulse.database.DailyPulseDatabase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import platform.Foundation.NSLog
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen

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

actual class DriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(DailyPulseDatabase.Schema, Constants.DATABASE_NAME)
}

actual fun getPlatform(): Platform = Platform()

