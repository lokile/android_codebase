package com.lokile.applibraries.utils

internal var isLogException = false
fun handleException(throwable: Throwable) {
    throwable.printStackTrace()
    if (isLogException){
        com.lokile.firebase_analytics_support.handleException(throwable)
    }
}