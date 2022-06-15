package com.lokile.applibraries.services

import android.content.Context
import androidx.core.content.edit

open class BaseSharedPreferences(val preferencesName: String, context: Context) {
    private val pref = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    protected fun setData(key: String, data: Any) {
        when (data) {
            is String -> pref.edit { putString(key, data) }
            is Boolean -> pref.edit { putBoolean(key, data) }
            is Float -> pref.edit { putFloat(key, data) }
            is Int -> pref.edit { putInt(key, data) }
            is Long -> pref.edit { putLong(key, data) }
            else -> throw Exception("Unsupported data type")
        }
    }

    protected fun getString(key: String, default: String? = null) = pref.getString(key, default)
    protected fun getBoolean(key: String, default: Boolean = false) = pref.getBoolean(key, default)
    protected fun getFloat(key: String, default: Float) = pref.getFloat(key, default)
    protected fun getInt(key: String, default: Int) = pref.getInt(key, default)
    protected fun getLong(key: String, default: Long) = pref.getLong(key, default)
}