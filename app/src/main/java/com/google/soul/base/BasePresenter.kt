package com.google.soul.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**************************
 *作者：FYX
 *日期：2018/9/25 14:53
 **************************/
open class BasePresenter<T : IBaseView> : IBasePresenter<T> {

    var mView: T? = null
    private var compositeDisposable = CompositeDisposable()
    private val isViewAttached: Boolean
        get() = mView != null

    override fun attachView(mView: T) {
        this.mView = mView
    }

    override fun detachView() {
        mView = null
        //保证activity结束时取消所有正在执行的订阅
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }

    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
}