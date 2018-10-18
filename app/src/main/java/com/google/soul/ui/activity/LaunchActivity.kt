package com.google.soul.ui.activity

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.PointF
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateInterpolator
import cn.jpush.im.android.api.JMessageClient
import com.bumptech.glide.Glide
import com.google.soul.App
import com.google.soul.R
import com.google.soul.base.BaseActivity
import com.google.soul.dialog.LoginDialogFragment
import com.google.soul.dialog.RegisterDialogFragment
import com.google.soul.mvp.model.CircleBean
import com.google.soul.utils.AppUtil
import com.google.soul.widget.HintDialog
import kotlinx.android.synthetic.main.activity_launch.*

class LaunchActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_launch

    override fun initData() {
        Glide.with(mContext).load("https://api.berryapi.net/bing/image/?768/1280/-1").into(iv_bg)
        mBubbleView.circleBeen = initPoint()
        mBubbleView.openAnimation()
        intent?.extras?.getString("message")?.let {
            HintDialog().message(it).show(supportFragmentManager, "")
            App.appInst.finishOtherActivity()
        }

        Handler().postDelayed({
            if (JMessageClient.getMyInfo() != null) {
                startActivity(MainActivity::class.java)
                finish()
            } else {
                ObjectAnimator.ofFloat(ll_loginOrRegister, "translationX", ll_loginOrRegister.width.toFloat(), 0f).let {
                    it.duration = 300
                    it.interpolator = AccelerateInterpolator()
                    it.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationStart(animation: Animator?) {
                            ll_loginOrRegister.visibility = View.VISIBLE
                        }
                    })
                    it.start()
                }
            }
        }, 2000)
        btn_login.setOnClickListener {
            LoginDialogFragment().show(supportFragmentManager, "")
        }
        btn_register.setOnClickListener {
            RegisterDialogFragment().show(supportFragmentManager, "")
        }
    }

    private fun initPoint(): ArrayList<CircleBean> {
        val height = AppUtil.getDisplayHight(this)
        val width = AppUtil.getDisplayWidth(this)
        val centerX = width / 2
        val centerY = height / 2
        val circleBean = CircleBean(
                PointF((-width / 5.1).toFloat(), (height / 1.5).toFloat()),
                PointF((centerX - 30).toFloat(), (height * 2 / 3).toFloat()),
                PointF((width / 2.4).toFloat(), (height / 3.4).toFloat()),
                PointF((width / 6).toFloat(), (centerY - 120).toFloat()),
                PointF((width / 7.2).toFloat(), (-height / 128).toFloat()),
                (width / 14.4).toFloat(), 60)
        val circleBean2 = CircleBean(
                PointF((-width / 4).toFloat(), (height / 1.3).toFloat()),
                PointF((centerX - 20).toFloat(), (height * 3 / 5).toFloat()),
                PointF((width / 2.1).toFloat(), (height / 2.5).toFloat()),
                PointF((width / 3).toFloat(), (centerY - 10).toFloat()),
                PointF((width / 4).toFloat(), (-height / 5.3).toFloat()),
                (width / 4).toFloat(), 60)
        val circleBean3 = CircleBean(
                PointF((-width / 12).toFloat(), (height / 1.1).toFloat()),
                PointF((centerX - 100).toFloat(), (height * 2 / 3).toFloat()),
                PointF((width / 3.4).toFloat(), (height / 2).toFloat()),
                PointF(0f, (centerY + 100).toFloat()),
                PointF(0f, 0f),
                (width / 24).toFloat(), 60)
        val circleBean4 = CircleBean(
                PointF((-width / 9).toFloat(), (height / 0.9).toFloat()),
                PointF(centerX.toFloat(), (height * 3 / 4).toFloat()),
                PointF((width / 2.1).toFloat(), (height / 2.3).toFloat()),
                PointF((width / 2).toFloat(), centerY.toFloat()),
                PointF((width / 1.5).toFloat(), (-height / 5.6).toFloat()),
                (width / 4).toFloat(), 60)
        val circleBean5 = CircleBean(
                PointF((width / 1.4).toFloat(), (height / 0.9).toFloat()),
                PointF(centerX.toFloat(), (height * 3 / 4).toFloat()),
                PointF((width / 2).toFloat(), (height / 2.37).toFloat()),
                PointF((width * 10 / 13).toFloat(), (centerY - 20).toFloat()),
                PointF((width / 2).toFloat(), (-height / 7.1).toFloat()),
                (width / 4).toFloat(), 60)
        val circleBean6 = CircleBean(
                PointF((width / 0.8).toFloat(), height.toFloat()),
                PointF((centerX + 20).toFloat(), (height * 2 / 3).toFloat()),
                PointF((width / 1.9).toFloat(), (height / 2.3).toFloat()),
                PointF((width * 11 / 14).toFloat(), (centerY + 10).toFloat()),
                PointF((width / 1.1).toFloat(), (-height / 6.4).toFloat()),
                (width / 4).toFloat(), 60)
        val circleBean7 = CircleBean(
                PointF((width / 0.9).toFloat(), (height / 1.2).toFloat()),
                PointF((centerX + 20).toFloat(), (height * 4 / 7).toFloat()),
                PointF((width / 1.6).toFloat(), (height / 1.9).toFloat()),
                PointF(width.toFloat(), (centerY + 10).toFloat()),
                PointF(width.toFloat(), 0f),
                (width / 9.6).toFloat(), 60)
        val circleBeanList = ArrayList<CircleBean>()
        circleBeanList.add(circleBean)
        circleBeanList.add(circleBean2)
        circleBeanList.add(circleBean3)
        circleBeanList.add(circleBean4)
        circleBeanList.add(circleBean5)
        circleBeanList.add(circleBean6)
        circleBeanList.add(circleBean7)
        return circleBeanList
    }
}
