package com.google.soul.mvp.presenter

import com.google.soul.base.BaseObserver
import com.google.soul.base.BasePresenter
import com.google.soul.mvp.contract.HomeContract
import com.google.soul.mvp.model.BannerEntity
import com.google.soul.net.RetrofitManager
import io.reactivex.disposables.Disposable

/**************************
 *作者：FYX
 *日期：2018/9/26 10:19
 **************************/
class HomePresenter :BasePresenter<HomeContract.View>(),HomeContract.Presenter {

    override fun getBanner() {
        //获取Banner
        RetrofitManager.getBanner(object : BaseObserver<BannerEntity>(){
            override fun success(data: BannerEntity) {
                mView?.updateBanner(data)
            }

            override fun disposable(d: Disposable) {
                addSubscription(d)
            }
        })
    }
}