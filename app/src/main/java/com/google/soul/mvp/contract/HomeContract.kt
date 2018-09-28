package com.google.soul.mvp.contract

import com.google.soul.base.IBasePresenter
import com.google.soul.base.IBaseView
import com.google.soul.mvp.model.BannerEntity

/**************************
 *作者：FYX
 *日期：2018/9/26 10:14
 **************************/
interface HomeContract {

    interface View : IBaseView {
        fun updateBanner(data: BannerEntity)
    }

    interface Presenter : IBasePresenter<View> {
        fun getBanner()
    }
}