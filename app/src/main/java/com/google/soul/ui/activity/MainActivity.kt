package com.google.soul.ui.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import cn.jpush.im.android.api.JMessageClient
import com.google.soul.R
import com.google.soul.adapter.HomeViewPagerAdapter
import com.google.soul.base.BaseActivity
import com.google.soul.ui.fragment.HomeFragment
import com.google.soul.ui.fragment.ConversationFragment
import com.google.soul.ui.fragment.MeFragment
import com.google.soul.ui.fragment.FriendsFragment
import kotlinx.android.synthetic.main.activity_main.*
import cn.jpush.im.android.api.event.LoginStateChangeEvent
import android.view.LayoutInflater
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.view.View
import android.widget.TextView

class MainActivity : BaseActivity() {

    var badgeView: TextView? = null

    override fun layoutId(): Int = R.layout.activity_main

    override fun initData() {
        JMessageClient.registerEventReceiver(this)
//        mViewPager.setOnTouchListener { _, _ -> true }//禁止ViewPager滑动,Kotlin中没有用到的参数用_标识 v, event
        mViewPager.offscreenPageLimit = 4
        HomeViewPagerAdapter(supportFragmentManager).run {
            addFragment(HomeFragment())
            addFragment(ConversationFragment())
            addFragment(FriendsFragment())
            addFragment(MeFragment())
            mViewPager.adapter = this
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> mViewPager.currentItem = 0
                R.id.item_conversation -> mViewPager.currentItem = 1
                R.id.item_friend -> mViewPager.currentItem = 2
                R.id.item_me -> mViewPager.currentItem = 3
            }
            false
        }

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mBottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
        initMsgCount()
    }

    private fun initMsgCount() {
        val menuView = mBottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(1) as BottomNavigationItemView
        val view = LayoutInflater.from(mContext).inflate(R.layout.badge, menuView, false)
        itemView.addView(view)
        badgeView = view.findViewById(R.id.tv_msg_count) as TextView
    }

    override fun onResume() {
        super.onResume()
        val count = JMessageClient.getAllUnReadMsgCount()
        if (count > 0) {
            badgeView?.visibility = View.VISIBLE
            if (count > 99) {
                badgeView?.text = "99+"
            } else {
                badgeView?.text = count.toString()
            }
        } else {
            badgeView?.visibility = View.GONE
            badgeView?.text = "0"
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //退回到桌面，应用程序不结束
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        JMessageClient.unRegisterEventReceiver(this)
        super.onDestroy()
    }

    fun onEvent(event: LoginStateChangeEvent) {
        val reason = event.reason//获取变更的原因
        val myInfo = event.myInfo//获取当前被登出账号的信息
        val message = when (reason) {
            //用户密码在服务器端被修改
            LoginStateChangeEvent.Reason.user_password_change -> "您当前账号密码被修改,请重新登录！"
            //用户换设备登录
            LoginStateChangeEvent.Reason.user_logout -> "您的当前账号在其他地方登录,如果不是本人操作,请及时修改密码！"
            //用户被删除
            LoginStateChangeEvent.Reason.user_deleted -> "您的当前账号已经被删除！"
            LoginStateChangeEvent.Reason.user_disabled -> "账户不可用！"
            LoginStateChangeEvent.Reason.user_login_status_unexpected -> "登录状态异常！"
        }
        val bundle = Bundle()
        bundle.putString("message", message)
        startActivity(LaunchActivity::class.java, bundle)
    }
}