package com.google.soul.ui.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.google.soul.R
import com.google.soul.base.BaseFragment
import com.google.soul.dialog.ThemeDialogFragment
import com.google.soul.mvp.model.MessageEvent
import com.google.soul.ui.activity.LaunchActivity
import com.google.soul.ui.activity.MovieActivity
import com.google.soul.ui.activity.RacingCarsActivity
import com.google.soul.ui.activity.UserInfoActivity
import com.google.soul.utils.AppUtil
import com.google.soul.utils.CustomToast
import com.google.soul.widget.HintDialog
import kotlinx.android.synthetic.main.fragment_me.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**************************
作者：FYX
日期：2018/8/24 0024
 **************************/
class MeFragment : BaseFragment() {

    private var userInfo: UserInfo? = null

    override fun layoutId(): Int = R.layout.fragment_me

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateInfo(messageEvent: MessageEvent) {
        if (messageEvent.type == 2) {
            userInfo = JMessageClient.getMyInfo()?.apply {
                if (!this.nickname.isNullOrEmpty()) {
                    tv_nickname.text = this.nickname
                } else {
                    tv_nickname.text = this.userName
                }
                this.getAvatarBitmap(object : GetAvatarBitmapCallback() {
                    override fun gotResult(p0: Int, p1: String?, p2: Bitmap?) {
                        if (p2 != null) {
                            ci_pic.setImageBitmap(p2)
                        }
                    }
                })
                tv_signature.text = this.signature
                this.getExtra("last_sign_time").let {
                    if (it.isNullOrEmpty() || !AppUtil.isToday(it.toLong())) {
                        tv_sign.visibility = View.VISIBLE
                    } else {
                        tv_sign.visibility = View.INVISIBLE
                    }
                }
                mSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun init() {
        mSwipeRefreshLayout.setColorSchemeColors(AppUtil.getThemeColor())
        mSwipeRefreshLayout.setOnRefreshListener {
            EventBus.getDefault().post(MessageEvent(2, "更新资料"))
        }
        ll_info.setOnClickListener {
            startActivity(UserInfoActivity::class.java)
        }
        //签到
        tv_sign.setOnClickListener {
            var money = 0
            userInfo?.getExtra("money")?.let {
                money = it.toInt()
            }
            money += 10
            userInfo?.setUserExtras("money", money.toString())
            userInfo?.setUserExtras("last_sign_time", System.currentTimeMillis().toString())
            JMessageClient.updateMyInfo(UserInfo.Field.extras, userInfo, object : BasicCallback() {
                override fun gotResult(p0: Int, p1: String?) {
                    if (p0 == 0) {
                        CustomToast.showSuccess("签到成功")
                        val animatorSet = AnimatorSet()
                        animatorSet.play(ObjectAnimator.ofFloat(tv_sign, "alpha", 1f, 0f)).with(ObjectAnimator.ofFloat(tv_sign, "translationY", 0f, -50f))
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
                                tv_sign.visibility = View.INVISIBLE
                            }
                        })
                    } else {
                        CustomToast.showSuccess("签到失败")
                    }
                }
            })
        }

        tl_movie.setOnClickListener {
            startActivity(MovieActivity::class.java)
        }

        tl_racing_car.setOnClickListener {
            startActivity(RacingCarsActivity::class.java)
        }
        //设置主题
        tl_skin.setOnClickListener {
            ThemeDialogFragment().show(activity?.supportFragmentManager, "")
        }
        //退出当前账号
        tl_exit_login.setOnClickListener {
            HintDialog().message("是否退出登录？").setLeftClick(View.OnClickListener {}).setRightClick(View.OnClickListener {
                JMessageClient.logout()
                startActivity(LaunchActivity::class.java)
                activity?.finish()
            }).show(activity?.supportFragmentManager, "")
        }
    }

    override fun lazyLoad() {
        EventBus.getDefault().post(MessageEvent(2, "更新资料"))
    }
}