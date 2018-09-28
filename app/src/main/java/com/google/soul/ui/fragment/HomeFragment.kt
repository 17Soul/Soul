package com.google.soul.ui.fragment

import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.soul.R
import com.google.soul.base.BaseFragment
import com.google.soul.mvp.contract.HomeContract
import com.google.soul.mvp.model.BannerEntity
import com.google.soul.mvp.presenter.HomePresenter
import com.google.soul.ui.activity.ImagePreviewActivity
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

/**************************
作者：FYX
日期：2018/8/24 0024
 **************************/
class HomeFragment : BaseFragment(), HomeContract.View {

    private val homePresenter = HomePresenter()

    override fun layoutId(): Int = R.layout.fragment_home

    override fun init() {

    }

    override fun lazyLoad() {
        homePresenter.attachView(this)
        homePresenter.getBanner()
    }

    override fun updateBanner(data: BannerEntity) {
        val bannerUrl = ArrayList<String>()
        val bannerTitle = ArrayList<String>()
        for (i in 0 until data.images.size) {
            bannerUrl.add("http://www.bing.com" + data.images[i].url)
            bannerTitle.add(data.images[i].copyright)
        }
        mBGABanner.setData(bannerUrl, bannerTitle)
        mBGABanner.setAdapter { _, itemView, model, _ ->
            Glide.with(mContext).load(model).transition(DrawableTransitionOptions().crossFade()).into(itemView as ImageView)
        }
        mBGABanner.setDelegate { _, _, model, _ ->
            val bundle = Bundle()
            bundle.putInt("type", 0)
            bundle.putString("key", model as String)
            startActivity(ImagePreviewActivity::class.java, bundle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homePresenter.detachView()
    }
}