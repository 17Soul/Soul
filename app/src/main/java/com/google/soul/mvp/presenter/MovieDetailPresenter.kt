package com.google.soul.mvp.presenter

import com.google.soul.base.BaseObserver
import com.google.soul.base.BasePresenter
import com.google.soul.mvp.contract.MovieDetailContract
import com.google.soul.mvp.model.CharacterEntity
import com.google.soul.mvp.model.MovieDetailEntity
import com.google.soul.net.RetrofitManager
import io.reactivex.disposables.Disposable

/**************************
 *作者：FYX
 *日期：2018/9/26 11:26
 **************************/
class MovieDetailPresenter : BasePresenter<MovieDetailContract.View>(), MovieDetailContract.Presenter {

    override fun getDetailMovie(id: String) {
        mView?.showLoading()
        RetrofitManager.getDetailMovie(id, object : BaseObserver<MovieDetailEntity>() {
            override fun success(data: MovieDetailEntity) {
                mView?.hideLoading()
                mView?.updateDetailMovie(data)
            }

            override fun disposable(d: Disposable) {
                addSubscription(d)
            }
        })
    }

    override fun getCharacter(id: String) {
        mView?.showLoading()
        RetrofitManager.getCharacter(id, object : BaseObserver<CharacterEntity>() {
            override fun success(data: CharacterEntity) {
                mView?.hideLoading()
                mView?.updateCharacter(data)
            }

            override fun disposable(d: Disposable) {
                addSubscription(d)
            }
        })
    }
}