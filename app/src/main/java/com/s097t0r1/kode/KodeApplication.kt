package com.s097t0r1.kode

import android.app.Application
import com.s097t0r1.data.di.networkModule
import com.s097t0r1.data.di.userModule
import com.s097t0r1.kode.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class KodeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@KodeApplication)
            modules(networkModule, userModule, viewModelsModule)
        }
    }
}