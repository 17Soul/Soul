package com.google.soul.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**************************
作者：FYX
日期：2018/8/26 0026
 **************************/
class MovieViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mTitle = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    fun addTitle(title: String) {
        mTitle.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitle[position]
    }
}