package com.google.soul.base

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**************************
作者：FYX
日期：2018/8/24 0024
 **************************/
abstract class BaseObserver<T> : Observer<T> {

    override fun onSubscribe(d: Disposable) {
        disposable(d)
    }

    override fun onNext(t: T) {
        success(t)
    }

    override fun onComplete() {}

    override fun onError(e: Throwable) {}

    abstract fun success(data: T)

//    abstract fun failure(msg: String)

    abstract fun disposable(d: Disposable)
}