package com.lokile.applibraries.services

import android.content.Context
import androidx.core.content.edit
import com.lokile.dataencrypter.encrypters.imp.Encrypter

open class BaseSharedPreferences(
    val preferencesName: String,
    context: Context,
    val isEncrypt: Boolean = false
) {
    private val keyMap = hashMapOf<String, String>()
    private val enctypter by lazy { Encrypter(context, "alias" + preferencesName) }
    private val pref = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    fun convertKey(key: String): String {
        return if (isEncrypt) {
            keyMap.get(key)
                ?: enctypter.encrypt(key, true)?.also {
                    keyMap[key] = it
                } ?: key
        } else {
            key
        }
    }

    fun encryptString(data: String): String {
        return if (isEncrypt) {
            enctypter.encrypt(data) ?: data
        } else {
            data
        }
    }

    fun decryptString(data: String?): String? {
        return if (isEncrypt && !data.isNullOrEmpty()) {
            enctypter.decrypt(data) ?: data
        } else {
            data
        }
    }


    protected fun setData(inputkey: String, data: Any) {
        val key = convertKey(inputkey)
        when (data) {
            is String -> pref.edit { putString(key, encryptString(data)) }
            is Boolean -> pref.edit { putBoolean(key, data) }
            is Float -> pref.edit { putFloat(key, data) }
            is Int -> pref.edit { putInt(key, data) }
            is Long -> pref.edit { putLong(key, data) }
            else -> throw Exception("Unsupported data type")
        }
    }

    protected fun getString(key: String, default: String? = null) =
        decryptString(pref.getString(convertKey(key), default))

    protected fun getBoolean(key: String, default: Boolean = false) =
        pref.getBoolean(convertKey(key), default)

    protected fun getFloat(key: String, default: Float) = pref.getFloat(convertKey(key), default)
    protected fun getInt(key: String, default: Int) = pref.getInt(convertKey(key), default)
    protected fun getLong(key: String, default: Long) = pref.getLong(convertKey(key), default)
}