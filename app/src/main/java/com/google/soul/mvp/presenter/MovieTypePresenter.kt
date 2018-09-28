package com.google.soul.mvp.presenter

import com.google.soul.base.BaseObserver
import com.google.soul.base.BasePresenter
import com.google.soul.mvp.contract.MovieTypeContract
import com.google.soul.mvp.model.MovieEntity
import com.google.soul.net.RetrofitManager
import io.reactivex.disposables.Disposable

/**************************
 *作者：FYX
 *日期：2018/9/26 10:50
 **************************/
class MovieTypePresenter : BasePresenter<MovieTypeContract.View>(), MovieTypeContract.Presenter {

    override fun getMovie(type: String, start: Int, count: Int) {
        RetrofitManager.getMovie(type, start, count, object : BaseObserver<MovieEntity>() {
            override fun success(data: MovieEntity) {
                mView?.updateMovie(data)
            }

            override fun disposable(d: Disposable) {
                addSubscription(d)
            }
        })
    }
}