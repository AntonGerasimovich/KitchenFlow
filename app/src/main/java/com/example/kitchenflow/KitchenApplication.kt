package com.example.kitchenflow

import android.app.Application
import android.content.Context
import com.example.kitchenflow.di.appModule
import com.example.kitchenflow.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KitchenApplication : Application() {

    companion object {
        lateinit var app: KitchenApplication
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        app = this
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KitchenApplication)
            modules(appModule, viewModelModule)
        }
    }
}