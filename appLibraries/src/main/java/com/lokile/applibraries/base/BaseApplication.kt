package com.lokile.applibraries.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.lokile.applibraries.managers.*
import com.lokile.applibraries.utils.convert
import com.lokile.applibraries.utils.getPackageInfo

abstract class BaseApplication : Application() {
    abstract fun allowLoggingEventTracking(): Boolean
    abstract fun loadRemoteConfig(): List<RemoteConfigValue>
    override fun onCreate() {
        super.onCreate()
        app = this

        EventTrackingManager.init(this, Firebase.analytics, allowLoggingEventTracking())
        setCurrentAppVersion(getPackageInfo()?.versionName.orEmpty())
        RemoteConfigManager.reloadConfig(
            Firebase.remoteConfig,
            loadRemoteConfig().plus(
                DefaultRemoteConfigValues.USER_SEGMENT_NAME
            )
        ) { fromPrevious, isUpdated, isSuccess ->
            if (isSuccess) {
                setUserSegmentName(DefaultRemoteConfigValues.USER_SEGMENT_NAME.value.convert())
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        lateinit var app: BaseApplication
    }
}