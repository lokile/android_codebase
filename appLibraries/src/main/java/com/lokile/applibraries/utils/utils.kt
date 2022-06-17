package com.lokile.applibraries.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.ViewConfiguration
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lokile.applibraries.BuildConfig
import java.io.File
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
fun String.showDebugLog(tag: String = "AppLibraries"): String {
    if (BuildConfig.DEBUG) {
        Log.d(tag, this)
    }
    return this
}

fun Context.getNavigationBarHeight(): Int {
    val hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey()
    val resourceId: Int = getResources().getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0 && !hasMenuKey) {
        getResources().getDimensionPixelSize(resourceId)
    } else 0
}

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId: Int = getResources().getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = getResources().getDimensionPixelSize(resourceId)
    }
    return result
}

fun Context.getColorRessource(colorRes: Int) =
    ContextCompat.getColor(this, colorRes)

fun Context.checkAppPermission(list: List<String>): Boolean {
    return list.map { checkCallingOrSelfPermission(it) }
        .all { it == PackageManager.PERMISSION_GRANTED }
}

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()

fun ensureBackgroundThread(callback: () -> Unit) {
    if (isOnMainThread()) {
        Thread {
            callback()
        }.start()
    } else {
        callback()
    }
}

fun <T> Application.queryContentProvider(
    queryColumn: Array<String>, uri: Uri, queryCur: (Cursor) -> T
): List<T> {
    val resultList = arrayListOf<T>()
    val cur = contentResolver.query(uri, queryColumn, null, null, null)
    if (cur != null && cur.count > 0) {
        if (cur.moveToFirst()) {
            do {
                resultList.add(queryCur(cur))
            } while (cur.moveToNext())
        }
    }
    return resultList
}

fun AppCompatImageView.changeColor(context: Context, colorId: Int, parse: Boolean = true) {
    if (parse) {
        setColorFilter(
            context.getColorRessource(colorId), PorterDuff.Mode.SRC_ATOP
        )
    } else {
        setColorFilter(
            colorId, PorterDuff.Mode.SRC_ATOP
        )
    }
}

inline fun <reified T : Activity> Activity.openActivity(vararg params: Pair<String, Any>) {
    startActivity(
        Intent(this, T::class.java).apply {
            params.forEach {
                when (it.second) {
                    is String -> putExtra(it.first, it.second as String)
                    is Int -> putExtra(it.first, it.second as Int)
                    is Byte -> putExtra(it.first, it.second as Byte)
                    is Char -> putExtra(it.first, it.second as Char)
                    is Long -> putExtra(it.first, it.second as Long)
                    is Float -> putExtra(it.first, it.second as Float)
                    is Short -> putExtra(it.first, it.second as Short)
                    is Double -> putExtra(it.first, it.second as Double)
                    is Boolean -> putExtra(it.first, it.second as Boolean)
                    is IntArray -> putExtra(it.first, it.second as IntArray)
                    is ByteArray -> putExtra(it.first, it.second as ByteArray)
                    is CharArray -> putExtra(it.first, it.second as CharArray)
                    is LongArray -> putExtra(it.first, it.second as LongArray)
                    is FloatArray -> putExtra(it.first, it.second as FloatArray)
                    is Parcelable -> putExtra(it.first, it.second as Parcelable)
                    is Bundle -> putExtra(it.first, it.second as Bundle)
                    is ShortArray -> putExtra(it.first, it.second as ShortArray)
                    is DoubleArray -> putExtra(it.first, it.second as DoubleArray)
                    is BooleanArray -> putExtra(it.first, it.second as BooleanArray)
                    is CharSequence -> putExtra(it.first, it.second as CharSequence)
                    is Serializable -> putExtra(it.first, it.second as Serializable)
                }
            }
        }
    )
}

fun getLastModifiedAsLong(path: String): Long {
    var file = File(path)
    if (file.exists()) {
        return file.lastModified()
    }
    return 0
}

fun String.getFileKey(lastModified: Long? = null): String {
    val file = File(this)
    val modified = if (lastModified != null && lastModified > 0) {
        lastModified
    } else {
        file.lastModified()
    }

    return "${file.absolutePath}$modified"
}

fun <T> Any.convert() = this as T

fun handleException(throwable: Throwable) {
    FirebaseCrashlytics.getInstance().recordException(throwable)
    throwable.printStackTrace()
}

fun Context.getPackageInfo(): PackageInfo? {
    try {
        return packageManager.getPackageInfo(packageName, 0)
    } catch (e: Exception) {
        handleException(e)
        return null
    }
}
