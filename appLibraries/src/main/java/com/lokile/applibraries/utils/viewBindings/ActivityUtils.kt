package com.lokile.applibraries.utils.viewBindings

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.lokile.applibraries.base.AppBaseActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityViewBindingDelegate<T : ViewBinding>(
    val activity: AppBaseActivity,
    val viewBindingFactory: (LayoutInflater) -> T
) : ReadOnlyProperty<AppBaseActivity, T> {
    private var value: T? = null

    init {
        activity.viewBindingProvider = {
            value ?: viewBindingFactory(it).also { value = it }
        }
    }

    override fun getValue(thisRef: AppBaseActivity, property: KProperty<*>): T {
        return value ?: viewBindingFactory(thisRef.layoutInflater).also { value = it }
    }
}

fun <T : ViewBinding> AppBaseActivity.viewBinding(viewBindingFactory: LayoutInflater.() -> T) =
    ActivityViewBindingDelegate(this, viewBindingFactory)