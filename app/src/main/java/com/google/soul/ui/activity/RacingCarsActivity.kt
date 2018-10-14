package com.google.soul.ui.activity

import android.os.CountDownTimer
import android.view.View
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.google.soul.R
import com.google.soul.base.BaseActivity
import com.google.soul.mvp.model.CarEntity

import com.google.soul.widget.HintDialog
import kotlinx.android.synthetic.main.activity_racing_cars.*
import java.util.*

class RacingCarsActivity : BaseActivity(), View.OnClickListener {

    private var countDownTimer: CountDownTimer? = null
    private var duration: Long = 30000//动画时间
    private var list = ArrayList<CarEntity>()//存放当前赛车数据
    private var num: String? = null
    private var money = 0
    private var userInfo: UserInfo? = null

    override fun layoutId(): Int = R.layout.activity_racing_cars

    override fun initData() {
        mTitleBar.setLeftLayoutClickListener(View.OnClickListener {
            finish()
        })
        cv_car1.setOnClickListener(this)
        cv_car2.setOnClickListener(this)
        cv_car3.setOnClickListener(this)
        cv_car4.setOnClickListener(this)
        cv_car5.setOnClickListener(this)
        cv_car6.setOnClickListener(this)
        cv_car7.setOnClickListener(this)
        cv_car8.setOnClickListener(this)
        cv_car9.setOnClickListener(this)
        cv_car10.setOnClickListener(this)
        userInfo = JMessageClient.getMyInfo().apply {
            if (this.getExtra("money").isNullOrEmpty()) {
                updateMoney(0)
            } else {
                updateMoney(this.getExtra("money").toInt())
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cv_car1 -> {
                num = "①"
            }
            R.id.cv_car2 -> {
                num = "②"
            }
            R.id.cv_car3 -> {
                num = "③"
            }
            R.id.cv_car4 -> {
                num = "④"
            }
            R.id.cv_car5 -> {
                num = "⑤"
            }
            R.id.cv_car6 -> {
                num = "⑥"
            }
            R.id.cv_car7 -> {
                num = "⑦"
            }
            R.id.cv_car8 -> {
                num = "⑧"
            }
            R.id.cv_car9 -> {
                num = "⑨"
            }
            R.id.cv_car10 -> {
                num = "⑩"
            }
        }
        if (money > 0) {
            HintDialog()
                    .message("猜${num}号车获得冠军？")
                    .setLeftClick(View.OnClickListener {})
                    .setRightClick(View.OnClickListener {
                        updateMoney(-1)
                        begin()
                    })
                    .show(supportFragmentManager, "")
        } else {
            HintDialog()
                    .message("积分不足，赶紧去签到获取积分吧")
                    .setRightClick(View.OnClickListener {})
                    .show(supportFragmentManager, "")
        }
    }

    private fun begin() {
        countDownTimer?.cancel()
        mScrollPlayView.startPlay()
        cv_car1.startRun(duration)
        cv_car2.startRun(duration)
        cv_car3.startRun(duration)
        cv_car4.startRun(duration)
        cv_car5.startRun(duration)
        cv_car6.startRun(duration)
        cv_car7.startRun(duration)
        cv_car8.startRun(duration)
        cv_car9.startRun(duration)
        cv_car10.startRun(duration)
        countDownTimer = object : CountDownTimer(duration, 500) {

            override fun onTick(millisUntilFinished: Long) {
                list.clear()
                list.add(CarEntity("①", cv_car1.getCurrentValue()))
                list.add(CarEntity("②", cv_car2.getCurrentValue()))
                list.add(CarEntity("③", cv_car3.getCurrentValue()))
                list.add(CarEntity("④", cv_car4.getCurrentValue()))
                list.add(CarEntity("⑤", cv_car5.getCurrentValue()))
                list.add(CarEntity("⑥", cv_car6.getCurrentValue()))
                list.add(CarEntity("⑦", cv_car7.getCurrentValue()))
                list.add(CarEntity("⑧", cv_car8.getCurrentValue()))
                list.add(CarEntity("⑨", cv_car9.getCurrentValue()))
                list.add(CarEntity("⑩", cv_car10.getCurrentValue()))
                list.sort()
                val stringBuffer = StringBuffer()
                for (i in list.indices) {
                    stringBuffer.append(list[i].carNumber)
                }
                tv_no.text = "排名：" + stringBuffer.toString()
                mProgressBar.progress = (millisUntilFinished / 500).toInt()
                if (millisUntilFinished > duration - 2000) {
                    mScrollPlayView.setSpeed(((duration - millisUntilFinished) / 500).toInt())//模拟2秒起步加速过程
                } else {
                    mScrollPlayView.setSpeed(5)
                }
            }

            override fun onFinish() {
                mScrollPlayView.stopPlay()
                if (list[0].carNumber == num) {
                    showResult("(*^_^*)", "恭喜猜中获得15积分")
                    updateMoney(15)
                } else {
                    showResult("（＞人＜）", "很遗憾，你与大奖擦肩而过")
                }
            }
        }.start()
    }

    private fun updateMoney(size: Int) {
        money += size
        tv_money.text = money.toString()
    }

    override fun onDestroy() {
        userInfo?.setUserExtras("money", money.toString())
        JMessageClient.updateMyInfo(UserInfo.Field.extras, userInfo, object : BasicCallback() {
            override fun gotResult(p0: Int, p1: String?) {

            }
        })
        super.onDestroy()
    }

    private fun showResult(title: String, message: String) {
        HintDialog()
                .title(title)
                .message(message)
                .setRightClick(View.OnClickListener {})
                .show(supportFragmentManager, "")
    }
}
