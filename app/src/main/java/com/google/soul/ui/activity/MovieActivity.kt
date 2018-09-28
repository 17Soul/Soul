package com.google.soul.ui.activity

import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import com.google.soul.R
import com.google.soul.adapter.MovieViewPagerAdapter
import com.google.soul.base.BaseActivity
import com.google.soul.ui.fragment.AmericanBoxFragment
import com.google.soul.ui.fragment.MovieTypeFragment
import kotlinx.android.synthetic.main.activity_movie.*

class MovieActivity : BaseActivity() {

    private var movieViewPagerAdapter: MovieViewPagerAdapter? = null

    override fun layoutId(): Int = R.layout.activity_movie

    override fun initData() {
        mTitleBar.setLeftLayoutClickListener(View.OnClickListener {
            finish()
        })
        movieViewPagerAdapter = MovieViewPagerAdapter(supportFragmentManager)
        mViewPager.adapter = movieViewPagerAdapter
        mViewPager.offscreenPageLimit = 4
        mTabLayout.setupWithViewPager(mViewPager)
        mSearchButton.setOnClickListener {
            //元素共享动画
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, mSearchButton, mSearchButton.transitionName)
            startActivity(Intent(this, MovieSearchActivity::class.java), options.toBundle())
        }
        with(movieViewPagerAdapter) {
            this?.addTitle("正在热映")
            this?.addTitle("即将上映")
            this?.addTitle("Top250")
            this?.addTitle("北美票房榜")
            this?.addFragment(MovieTypeFragment.newInstance("in_theaters"))
            this?.addFragment(MovieTypeFragment.newInstance("coming_soon"))
            this?.addFragment(MovieTypeFragment.newInstance("top250"))
            this?.addFragment(AmericanBoxFragment())
            this?.notifyDataSetChanged()
        }
    }
}
