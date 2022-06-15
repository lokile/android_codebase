package com.lokile.applibraries.managers

import com.lokile.applibraries.utils.showDebugLog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject

interface RxBusListener {
    val uuid: Int
}

enum class RxBus {
    INSTANCE;

    var tracking = HashMap<Any, CompositeDisposable>()

    val publisher = PublishSubject.create<Any>()

    fun post(event: Any) {
        publisher.onNext(event)
    }

    private var allocatedUUID: Int = 0
    fun newUUID() = ++allocatedUUID

    inline fun <reified T> register(listener: RxBusListener, crossinline callBack: (T) -> Unit) {
        if (tracking[listener.uuid] == null) {
            tracking[listener.uuid] = CompositeDisposable()
        }
        "RxBus, new event listener: ${listener.uuid}".showDebugLog()
        tracking[listener.uuid]?.add(
            publisher.ofType(T::class.java).observeOn(AndroidSchedulers.mainThread())
                .subscribe { callBack(it) }
        )
    }

    fun unregister(listener: RxBusListener) {
        tracking[listener.uuid]?.dispose()
        tracking.remove(listener.uuid)?.let {
            "RxBus, removed event listener: ${listener.uuid}".showDebugLog()
        }
    }
}