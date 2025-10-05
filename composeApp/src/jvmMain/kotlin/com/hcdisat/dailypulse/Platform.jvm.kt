package com.hcdisat.dailypulse

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import java.awt.HeadlessException
import java.awt.Toolkit

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Platform {
    private fun getInfo(key: String) = System.getProperty(key) ?: "N/A"

    actual val osName: String
        get() = getInfo("os.name")

    actual val osVersion: String
        get() = getInfo("os.version")

    actual val deviceModel: String
        get() = "${getInfo("os.arch")} - ${getInfo("java.vendor.version")}"

    actual val density: Int
        get() {
            return try {
                Toolkit.getDefaultToolkit().screenResolution
            } catch (e: HeadlessException) {
                // Return a default or indicative value for headless environments
                -1 // Or 0, or a specific constant like `java.awt.GraphicsEnvironment.isHeadless()` check
            } catch (e: Exception) {
                // Catch any other potential exceptions
                -1
            }
        }

    actual fun logSystemInfo() {
        println("($osName, $osVersion, $deviceModel, $density)")
    }

    actual fun getHttEngine(): HttpClientEngine = CIO.create()
}

actual fun getPlatform(): Platform = Platform()