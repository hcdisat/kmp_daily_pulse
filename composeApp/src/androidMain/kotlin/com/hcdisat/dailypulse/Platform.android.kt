@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.hcdisat.dailypulse

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.Log
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.hcdisat.dailypulse.core.Constants
import com.hcdisat.dailypulse.database.DailyPulseDatabase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import kotlin.math.round

const val TAG = "DailyPulse"

actual class Platform {
    actual val osName: String = "Android"

    actual val osVersion: String
        get() = Build.VERSION.SDK_INT.toString()

    actual val deviceModel: String
        get() = "${Build.MANUFACTURER} ${Build.MODEL}"

    actual val density: Int
        get() = round(Resources.getSystem().displayMetrics.density).toInt()

    actual fun logSystemInfo() {
        Log.d(TAG, "($osName, $osVersion, $deviceModel, $density)")
    }

    actual fun getHttEngine(): HttpClientEngine = OkHttp.create()
}

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(
        DailyPulseDatabase.Schema,
        context,
        Constants.DATABASE_NAME
    )
}

actual fun getPlatform(): Platform = Platform()