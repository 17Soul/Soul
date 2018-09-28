package com.google.soul.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.soul.R
import com.google.soul.adapter.AmericanBoxAdapter
import com.google.soul.base.BaseAdapter
import com.google.soul.base.BaseFragment
import com.google.soul.mvp.contract.AmericanBoxContract
import com.google.soul.mvp.model.*
import com.google.soul.mvp.presenter.AmericanBoxPresenter
import com.google.soul.ui.activity.MovieDetailActivity
import kotlinx.android.synthetic.main.fragment_us_box.*

/**************************
作者：FYX
日期：2018/8/28 0028
北美票房榜
 **************************/
class AmericanBoxFragment : BaseFragment(), AmericanBoxContract.View {

    private var list = ArrayList<SubjectsEntity>()
    private var adapter: AmericanBoxAdapter? = null
    private val americanBoxPresenter = AmericanBoxPresenter()

    override fun lazyLoad() {
        americanBoxPresenter.attachView(this)
        mSmartRefreshLayout.autoRefresh()
    }

    override fun layoutId(): Int {
        return R.layout.fragment_us_box
    }

    override fun init() {
        mRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        adapter = AmericanBoxAdapter(mContext, list, R.layout.item_movie_us_box)
        mRecyclerView.adapter = adapter
        adapter?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                val item = obj as SubjectsEntity
                val bundle = Bundle()
                bundle.putString("id", item.subject.id)
                startActivity(MovieDetailActivity::class.java, bundle)
            }
        })
        mSmartRefreshLayout.setOnRefreshListener {
            list.clear()
            americanBoxPresenter.getMovie()
        }
    }

    override fun updateMovie(data: USBoxEntity) {
        list.addAll(data.subjects)
        adapter?.notifyDataSetChanged()
        mSmartRefreshLayout.finishRefresh()
    }
}