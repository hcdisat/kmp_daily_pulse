package com.hcdisat.dailypulse

import android.content.res.Resources
import android.os.Build
import android.util.Log
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpEngine
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

actual fun getPlatform(): Platform = Platform()