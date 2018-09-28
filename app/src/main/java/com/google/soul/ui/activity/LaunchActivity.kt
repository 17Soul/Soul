package com.google.soul.ui.activity

import android.animation.Animator
import android.animation.ObjectAnimator
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
import com.google.soul.widget.HintDialog
import kotlinx.android.synthetic.main.activity_launch.*

class LaunchActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_launch

    override fun initData() {
        Glide.with(mContext).load(R.drawable.launch_back).into(launch_background)
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
}
