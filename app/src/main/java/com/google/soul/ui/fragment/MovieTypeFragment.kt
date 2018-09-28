package com.google.soul.ui.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.google.soul.R
import com.google.soul.adapter.MovieOverviewAdapter
import com.google.soul.base.BaseAdapter
import com.google.soul.base.BaseFragment
import com.google.soul.mvp.contract.MovieTypeContract
import com.google.soul.mvp.model.*
import com.google.soul.mvp.presenter.MovieTypePresenter
import com.google.soul.ui.activity.MovieDetailActivity
import kotlinx.android.synthetic.main.fragment_movie_type.*

/**************************
作者：FYX
日期：2018/8/26 0026
 **************************/
class MovieTypeFragment : BaseFragment(), MovieTypeContract.View {

    private var type = ""//in_theaters正在热映//coming_soon即将上映//top250Top250
    private val count = 15
    private var start = 0
    private var list = ArrayList<SubjectEntity>()
    private var adapter: MovieOverviewAdapter? = null
    private val movieTypePresenter = MovieTypePresenter()

    companion object {
        fun newInstance(type: String): MovieTypeFragment {
            val movieFragment = MovieTypeFragment()
            val bundle = Bundle()
            bundle.putString("MovieType", type)
            movieFragment.arguments = bundle
            return movieFragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_movie_type

    override fun init() {
        arguments?.let {
            type = it.getString("MovieType")
        }
        mRecyclerView.layoutManager = GridLayoutManager(mContext, 3)
        adapter = MovieOverviewAdapter(mContext, list, R.layout.item_movie_overview)
        mRecyclerView.adapter = adapter
        adapter?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                val item = obj as SubjectEntity
                val bundle = Bundle()
                bundle.putString("id", item.id)
                startActivity(MovieDetailActivity::class.java, bundle)
            }
        })
        mSmartRefreshLayout.setOnLoadMoreListener {
            start += count
            movieTypePresenter.getMovie(type, start, count)
        }
        mSmartRefreshLayout.setOnRefreshListener {
            start = 0
            list.clear()
            movieTypePresenter.getMovie(type, start, count)
        }
    }

    override fun lazyLoad() {
        movieTypePresenter.attachView(this)
        mSmartRefreshLayout.autoRefresh()
    }

    override fun updateMovie(data: MovieEntity) {
        list.addAll(data.subjects)
        adapter?.notifyDataSetChanged()
        if (data.total <= list.size)//没有数据了
            mSmartRefreshLayout.finishLoadMoreWithNoMoreData()
        else
            mSmartRefreshLayout.finishLoadMore()
        mSmartRefreshLayout.finishRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        movieTypePresenter.detachView()
    }
}