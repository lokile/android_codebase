package com.lokile.applibraries.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.lokile.applibraries.managers.RxBus
import com.lokile.applibraries.managers.RxBusListener
import com.lokile.applibraries.utils.isLogException

abstract class BaseApplication : Application(), RxBusListener {
    override val uuid: Int by lazy { RxBus.INSTANCE.newUUID() }
    abstract fun hasCrashlytics(): Boolean
    override fun onCreate() {
        super.onCreate()
        app = this
        isLogException = hasCrashlytics()
    }

    inline fun <reified T> registerEventListener(crossinline callBack: (T) -> Unit) {
        RxBus.INSTANCE.register<T>(this, callBack)
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        lateinit var app: BaseApplication
    }
}