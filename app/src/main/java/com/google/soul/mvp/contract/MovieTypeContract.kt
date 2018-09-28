package com.google.soul.mvp.contract

import com.google.soul.base.IBasePresenter
import com.google.soul.base.IBaseView
import com.google.soul.mvp.model.MovieEntity

/**************************
 *作者：FYX
 *日期：2018/9/26 10:47
 **************************/
interface MovieTypeContract {

    interface View : IBaseView {
        fun updateMovie(data: MovieEntity)
    }

    interface Presenter : IBasePresenter<View> {
        fun getMovie(type: String, start: Int, count: Int)
    }
}