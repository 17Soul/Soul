package com.google.soul.base

/**************************
 *作者：FYX
 *日期：2018/9/25 14:55
 **************************/
interface IBasePresenter<V : IBaseView> {

    fun attachView(mView: V)

    fun detachView()
}