package com.google.soul.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**************************
作者：FYX
日期：2018/8/24 0024
 **************************/
abstract class BaseFragment : Fragment() {

    lateinit var mContext: Context
    var isViewInitiated: Boolean = false//懒加载视图是否完成
    var isDataInitiated: Boolean = false//懒加载数据是否加载

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewInitiated = true//View完成
        init()
        lazy()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser)
            lazy()
    }

    private fun lazy() {
        //当可见，视图初始化完毕，数据没有被加载时调用lazyInitData
        if (userVisibleHint && isViewInitiated && !isDataInitiated) {
            lazyLoad()
            isDataInitiated = true
        }
    }

    /**
     * 懒加载子类实现这个方法
     */
    abstract fun lazyLoad()

    abstract fun layoutId(): Int

    abstract fun init()

    fun startActivity(clazz: Class<*>) {
        startActivity(clazz, null)
    }

    fun startActivity(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(activity, clazz)
        bundle?.let { intent.putExtras(it) }
        startActivity(intent)
        activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}