package com.lokile.applibraries.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.lokile.applibraries.managers.RxBus
import com.lokile.applibraries.managers.RxBusListener
import com.lokile.applibraries.utils.handleException

abstract class AppBaseDialogFragment : Fragment(), IView, RxBusListener {
    override val uuid: Int by lazy { RxBus.INSTANCE.newUUID() }
    internal var viewBindingProvider: ((LayoutInflater) -> ViewBinding)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return viewBindingProvider?.invoke(inflater)?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            setupView(savedInstanceState)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        RxBus.INSTANCE.unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.INSTANCE.unregister(this)
    }

    inline fun <reified T> registerEventListener(crossinline callBack: (T) -> Unit) {
        RxBus.INSTANCE.register<T>(this, callBack)
    }

    fun getBaseActivity() = activity as? AppBaseActivity
}