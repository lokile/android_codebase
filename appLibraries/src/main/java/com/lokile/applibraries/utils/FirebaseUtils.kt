package com.lokile.applibraries.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

internal var isLogException = false
fun handleException(throwable: Throwable) {
    throwable.printStackTrace()
    if (isLogException) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}