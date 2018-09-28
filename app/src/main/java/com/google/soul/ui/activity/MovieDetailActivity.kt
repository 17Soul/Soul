package com.google.soul.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.soul.R
import com.google.soul.adapter.MovieCharacterAdapter
import com.google.soul.base.BaseActivity
import com.google.soul.base.BaseAdapter
import com.google.soul.dialog.CharacterDialogFragment
import com.google.soul.mvp.contract.MovieDetailContract
import com.google.soul.mvp.model.*
import com.google.soul.mvp.presenter.MovieDetailPresenter
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_movie_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**************************
作者：FYX
日期：2018/8/29 0029
 **************************/
class MovieDetailActivity : BaseActivity(), MovieDetailContract.View {

    private val movieDetailPresenter = MovieDetailPresenter()

    override fun layoutId(): Int = R.layout.activity_movie_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //EventBus绑定
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun load(messageEvent: MessageEvent) {
        if (messageEvent.type == 1) {
            movieDetailPresenter.getDetailMovie(messageEvent.data)
        }
    }

    override fun initData() {
        movieDetailPresenter.attachView(this)
        rv_directors.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        rv_casts.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

        val id = intent.extras.getString("id")
        movieDetailPresenter.getDetailMovie(id)

        //设置ToolBar返回键
        setSupportActionBar(mToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mToolBar.setNavigationOnClickListener { finish() }
    }

    override fun showLoading() {
        mLoadingDialog.show(supportFragmentManager, "")
    }

    override fun hideLoading() {
        mLoadingDialog.dismiss()
    }

    @SuppressLint("SetTextI18n")
    override fun updateDetailMovie(data: MovieDetailEntity) {
        //释放内存
        Glide.get(mContext).clearMemory()
        //回到顶部
        goTop()
        //实现毛玻璃
        Glide.with(mContext).load(data.images.large).apply(RequestOptions.bitmapTransform(BlurTransformation(20))).into(iv_background)
        Glide.with(mContext).load(data.images.large).into(iv_pic)
        iv_pic.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", 0)
            bundle.putString("key", data.images.large)
            startActivity(ImagePreviewActivity::class.java, bundle)
        }
        mCollapsingToolbarLayout.title = data.title
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)//设置展开标题颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)//设置合上标题颜色
        mRatingBar.starProgress = data.rating.average.toFloat()
        tv_rating.text = "评分:${data.rating.average}"
        tv_ratings_count.text = "评分人数:${data.ratings_count}人"
        tv_collect_count.text = "看过人数:${data.collect_count}人"
        tv_wish_count.text = "想看人数:${data.wish_count}人"
        tv_comments_count.text = "短评数量:${data.comments_count}条"
        tv_reviews_count.text = "影评数量:${data.reviews_count}条"
        tv_year.text = data.year
        tv_countries.text = getStringToListString(data.countries, "/")
        tv_types.text = getStringToListString(data.genres, "/")
        tv_aka.text = getStringToListString(data.aka, "/")
        tv_summary.text = data.summary
        MovieCharacterAdapter(mContext, data.directors, R.layout.item_movie_character).let {
            rv_directors.adapter = it
            it.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(obj: Any?, position: Int) {
                    val bean = obj as PersonnelEntity
                    if (null != bean.id) {
                        movieDetailPresenter.getCharacter(bean.id)
                    }
                }
            })
        }
        MovieCharacterAdapter(mContext, data.casts, R.layout.item_movie_character).let {
            rv_casts.adapter = it
            it.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(obj: Any?, position: Int) {
                    val bean = obj as PersonnelEntity
                    if (null != bean.id) {
                        movieDetailPresenter.getCharacter(bean.id)
                    }
                }
            })
        }
    }

    override fun updateCharacter(data: CharacterEntity) {
        CharacterDialogFragment().let {
            val bundle = Bundle()
            bundle.putSerializable("data", data)
            it.arguments = bundle
            it.show(supportFragmentManager, "")
        }
    }

    /**
     * 回到顶部
     */
    private fun goTop() {
        val behavior = (mAppBarLayout.layoutParams as CoordinatorLayout.LayoutParams).behavior
        if (behavior is AppBarLayout.Behavior) {
            val topAndBottomOffset = behavior.topAndBottomOffset;
            if (topAndBottomOffset != 0) {
                behavior.topAndBottomOffset = 0
            }
        }
        mNestedScrollView.scrollTo(0, 0)
    }

    /**
     * 将List<String>转换成String
     */
    private fun getStringToListString(list: List<String>, dex: String): String {
        val stringBuffer = StringBuffer()
        for (i in list.indices) {
            if (i == list.size - 1)
                stringBuffer.append(list[i])
            else
                stringBuffer.append(list[i] + dex)
        }
        return stringBuffer.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        //释放内存
        Glide.get(mContext).clearMemory()
        movieDetailPresenter.detachView()
        //EventBus解除绑定
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}