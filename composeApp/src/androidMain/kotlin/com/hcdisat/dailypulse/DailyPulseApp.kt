package com.hcdisat.dailypulse

import android.app.Application
import com.hcdisat.dailypulse.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class DailyPulseApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@DailyPulseApp)
        }
    }
}