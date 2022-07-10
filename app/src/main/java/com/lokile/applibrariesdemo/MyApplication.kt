package com.lokile.applibrariesdemo

import com.lokile.applibraries.base.BaseApplication

class MyApplication: BaseApplication() {

    override fun hasCrashlytics(): Boolean {
        return false
    }

}