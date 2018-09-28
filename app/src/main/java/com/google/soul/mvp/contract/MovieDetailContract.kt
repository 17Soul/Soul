package com.google.soul.mvp.contract

import com.google.soul.base.IBasePresenter
import com.google.soul.base.IBaseView
import com.google.soul.mvp.model.CharacterEntity
import com.google.soul.mvp.model.MovieDetailEntity

/**************************
 *作者：FYX
 *日期：2018/9/26 11:23
 **************************/
interface MovieDetailContract {

    interface View : IBaseView {
        fun updateDetailMovie(data: MovieDetailEntity)
        fun updateCharacter(data: CharacterEntity)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter : IBasePresenter<View> {
        fun getDetailMovie(id: String)
        fun getCharacter(id: String)
    }
}