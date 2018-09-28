package com.google.soul.mvp.contract

import com.google.soul.base.IBasePresenter
import com.google.soul.base.IBaseView
import com.google.soul.mvp.model.USBoxEntity

/**************************
 *作者：FYX
 *日期：2018/9/26 11:15
 **************************/
interface AmericanBoxContract {

    interface View : IBaseView {
        fun updateMovie(data: USBoxEntity)
    }

    interface Presenter : IBasePresenter<View> {
        fun getMovie()
    }
}