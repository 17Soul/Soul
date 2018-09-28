package com.google.soul.ui.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.google.android.flexbox.*
import com.google.soul.R
import com.google.soul.adapter.MovieSearchAdapter
import com.google.soul.adapter.MovieSearchTagAdapter
import com.google.soul.base.BaseActivity
import com.google.soul.base.BaseAdapter
import com.google.soul.mvp.contract.MovieSearchContract
import com.google.soul.mvp.model.*
import com.google.soul.mvp.presenter.MovieSearchPresenter
import com.google.soul.utils.CustomToast
import kotlinx.android.synthetic.main.activity_movie_search.*

class MovieSearchActivity : BaseActivity(), MovieSearchContract.View {

    var result = ArrayList<SubjectEntity>()
    var adapter: MovieSearchAdapter? = null
    private val movieSearchPresenter = MovieSearchPresenter()

    override fun layoutId(): Int = R.layout.activity_movie_search

    override fun initData() {
        movieSearchPresenter.attachView(this)
        mTitleBar.setLeftLayoutClickListener(View.OnClickListener {
            closeKeyBord()
            finish()
        })
        mEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                closeKeyBord()
                val keyWords = mEditText.text.toString().trim()
                if (keyWords.isEmpty()) {
                    CustomToast.showError("请输入你的关键词")
                } else {
                    movieSearchPresenter.searchMovieByQ(keyWords)
                }
            }
            false
        }
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MovieSearchAdapter(this, result, R.layout.item_movie_character_dialog)
        mRecyclerView.adapter = adapter
        adapter?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                val item = obj as SubjectEntity
                val bundle = Bundle()
                bundle.putString("id", item.id)
                startActivity(MovieDetailActivity::class.java, bundle)
            }
        })
        setTagData()
    }

    override fun updateSearchMovie(data: MovieEntity) {
        result.addAll(data.subjects)
        adapter?.notifyDataSetChanged()
    }

    override fun showLoading() {
        mLoadingDialog.show(supportFragmentManager, "")
        result.clear()
        removeTag()
    }

    override fun hideLoading() {
        mLoadingDialog.dismiss()
    }

    private fun removeTag() {
        val animatorSet = AnimatorSet()
        animatorSet.play(ObjectAnimator.ofFloat(tag_layout, "alpha", 1f, 0f)).with(ObjectAnimator.ofFloat(tag_layout, "translationX", 0f, 200f))
        animatorSet.duration = 1000
        animatorSet.start()
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                tag_layout.visibility = View.GONE
            }
        })
    }

    private fun setTagData() {
        val flexBoxLayoutManager = FlexboxLayoutManager(this)
        flexBoxLayoutManager.flexWrap = FlexWrap.WRAP      //按正常方向换行
        flexBoxLayoutManager.flexDirection = FlexDirection.ROW   //主轴为水平方向，起点在左端
        flexBoxLayoutManager.alignItems = AlignItems.CENTER    //定义项目在副轴轴上如何对齐
        flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START  //多个轴对齐方式
        tagRecyclerView.layoutManager = flexBoxLayoutManager

        val tag = ArrayList<String>()
        tag.add("喜剧")
        tag.add("剧情")
        tag.add("爱情")
        tag.add("动作")
        tag.add("科幻")
        tag.add("惊悚")
        tag.add("犯罪")
        tag.add("冒险")
        tag.add("传记")
        tag.add("纪录片")
        tag.add("玄幻")
        tag.add("动画")
        tag.add("历史")
        tag.add("家庭")
        tag.add("运动")
        tag.add("悬疑")
        tag.add("脱口秀")

        MovieSearchTagAdapter(this, tag, R.layout.item_flow_text).let {
            tagRecyclerView.adapter = it
            it.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(obj: Any?, position: Int) {
                    val tag = obj as String
                    closeKeyBord()
                    movieSearchPresenter.searchMovieByTag(tag)
                }
            })
        }
    }

    /**
     * 打卡软键盘
     */
    private fun openKeyBord() {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 关闭软键盘
     */
    private fun closeKeyBord() {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        movieSearchPresenter.detachView()
        closeKeyBord()
    }
}
