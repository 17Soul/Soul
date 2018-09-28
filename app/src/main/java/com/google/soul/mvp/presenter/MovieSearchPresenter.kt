package com.google.soul.mvp.presenter

import com.google.soul.base.BaseObserver
import com.google.soul.base.BasePresenter
import com.google.soul.mvp.contract.MovieDetailContract
import com.google.soul.mvp.contract.MovieSearchContract
import com.google.soul.mvp.model.CharacterEntity
import com.google.soul.mvp.model.MovieDetailEntity
import com.google.soul.mvp.model.MovieEntity
import com.google.soul.net.RetrofitManager
import io.reactivex.disposables.Disposable

/**************************
 *作者：FYX
 *日期：2018/9/26 11:26
 **************************/
class MovieSearchPresenter : BasePresenter<MovieSearchContract.View>(), MovieSearchContract.Presenter {

    override fun searchMovieByTag(keyword: String) {
        mView?.showLoading()
        RetrofitManager.searchMovieByTag(keyword, object : BaseObserver<MovieEntity>() {

            override fun success(data: MovieEntity) {
                mView?.hideLoading()
                mView?.updateSearchMovie(data)
            }

            override fun disposable(d: Disposable) {
                addSubscription(d)
            }
        })
    }

    override fun searchMovieByQ(keyword: String) {
        mView?.showLoading()
        RetrofitManager.searchMovieByQ(keyword, object : BaseObserver<MovieEntity>() {

            override fun success(data: MovieEntity) {
                mView?.hideLoading()
                mView?.updateSearchMovie(data)
            }

            override fun disposable(d: Disposable) {
                addSubscription(d)
            }
        })
    }
}