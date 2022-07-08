package com.lokile.applibraries.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.lokile.applibraries.managers.RemoteConfigValue
import com.lokile.applibraries.utils.isLogException
import com.lokile.firebase_analytics_support.initFirebase

abstract class BaseApplication : Application() {
    abstract fun allowLogException():Boolean
    override fun onCreate() {
        super.onCreate()
        app = this
        isLogException = allowLogException()
    }

    fun setupFirebase(
        remoteConfigList: List<RemoteConfigValue>,
        logEventTracking: Boolean
    ) {
        initFirebase(this, remoteConfigList, logEventTracking)
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        lateinit var app: BaseApplication
    }
}