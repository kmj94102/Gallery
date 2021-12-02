package com.example.gallery

import android.app.Application
import com.example.gallery.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Gallery : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG){
                    Level.ERROR
                } else {
                    Level.NONE
                }
            )
            androidContext(this@Gallery)
            modules(appModule)
        }

    }

}