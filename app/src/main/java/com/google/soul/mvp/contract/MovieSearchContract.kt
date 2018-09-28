package com.google.soul.mvp.contract

import com.google.soul.base.IBasePresenter
import com.google.soul.base.IBaseView
import com.google.soul.mvp.model.MovieEntity

/**************************
 *作者：FYX
 *日期：2018/9/26 11:23
 **************************/
interface MovieSearchContract {

    interface View : IBaseView {
        fun updateSearchMovie(data: MovieEntity)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter : IBasePresenter<View> {
        fun searchMovieByTag(keyword: String)
        fun searchMovieByQ(keyword: String)
    }
}