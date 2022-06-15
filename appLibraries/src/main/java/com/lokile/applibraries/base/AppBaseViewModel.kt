package com.lokile.applibraries.base

import androidx.lifecycle.ViewModel
import com.lokile.applibraries.managers.RxBus
import com.lokile.applibraries.managers.RxBusListener
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class AppBaseViewModel : ViewModel(), RxBusListener {
    private var compositeDisposable: CompositeDisposable? = null
    override val uuid: Int by lazy { RxBus.INSTANCE.newUUID() }

    fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null || compositeDisposable?.isDisposed == true) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.clear()
        RxBus.INSTANCE.unregister(this)
    }
}