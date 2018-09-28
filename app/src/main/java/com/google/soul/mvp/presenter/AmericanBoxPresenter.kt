package com.google.soul.mvp.presenter

import com.google.soul.base.BaseObserver
import com.google.soul.base.BasePresenter
import com.google.soul.mvp.contract.AmericanBoxContract
import com.google.soul.mvp.model.USBoxEntity
import com.google.soul.net.RetrofitManager
import io.reactivex.disposables.Disposable

/**************************
 *作者：FYX
 *日期：2018/9/26 11:16
 **************************/
class AmericanBoxPresenter :BasePresenter<AmericanBoxContract.View>(),AmericanBoxContract.Presenter {

    override fun getMovie() {
        RetrofitManager.getUsBox(object : BaseObserver<USBoxEntity>() {

            override fun disposable(d: Disposable) {
                addSubscription(d)
            }

            override fun success(data: USBoxEntity) {
                mView?.updateMovie(data)
            }
        })
    }
}