package com.lokile.applibraries.utils.viewBindings

import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.lokile.applibraries.base.AppBaseFragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


class FragmentViewBindingDelegate<T : ViewBinding>(
    val fragment: AppBaseFragment,
    val viewBindingFactory: (LayoutInflater) -> T
) : ReadOnlyProperty<AppBaseFragment, T> {
    private var value: T? = null

    init {
        fragment.viewBindingProvider = {
            value ?: viewBindingFactory(it).also { value = it }
        }

        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerLiveDataObserver =
                Observer<LifecycleOwner?> {
                    val viewLifecycleOwner = it ?: return@Observer

                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            value = null
                        }
                    })
                }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(
                    viewLifecycleOwnerLiveDataObserver
                )
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(
                    viewLifecycleOwnerLiveDataObserver
                )
            }
        })
    }

    override fun getValue(thisRef: AppBaseFragment, property: KProperty<*>): T {
        if (value != null) {
            return value!!
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }
        return viewBindingFactory(thisRef.layoutInflater).also { this.value = it }
    }
}

fun <T : ViewBinding> AppBaseFragment.viewBinding(viewBindingFactory: LayoutInflater.() -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)