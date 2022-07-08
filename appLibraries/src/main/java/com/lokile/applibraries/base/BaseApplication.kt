package com.lokile.applibraries.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.lokile.applibraries.managers.RemoteConfigValue
import com.lokile.firebase_analytics_support.initFirebase

abstract class BaseApplication : Application() {
    abstract fun allowLoggingEventTracking(): Boolean
    abstract fun loadRemoteConfig(): List<RemoteConfigValue>
    override fun onCreate() {
        super.onCreate()
        app = this
        initFirebase(this, loadRemoteConfig(), allowLoggingEventTracking())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        lateinit var app: BaseApplication
    }
}