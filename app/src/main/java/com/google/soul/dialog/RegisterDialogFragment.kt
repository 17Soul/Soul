package com.google.soul.dialog

import android.animation.Animator
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.google.soul.R
import com.google.soul.utils.CustomToast
import kotlinx.android.synthetic.main.fragment_dialog_register.*
import java.util.regex.Pattern

/**************************
作者：FYX
日期：2018/9/3 0003
 **************************/
class RegisterDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_register, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_close.setOnClickListener { dismiss() }
        btn_register.setOnClickListener {
            register()
        }
        tv_to_login.setOnClickListener {
            toLogin()
        }
        et_phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val phone = StringBuffer(s.toString().replace(" ", ""))
                if (phone.length > 3) {
                    phone.insert(3, " ")
                }
                if (phone.length > 8) {
                    phone.insert(8, " ")
                }
                et_phone.removeTextChangedListener(this)
                et_phone.setText(phone.toString())
                et_phone.setSelection(et_phone.text.toString().length)
                et_phone.addTextChangedListener(this)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        val window = dialog.window
        window.setLayout(dm.widthPixels, window.attributes.height)
        window.setWindowAnimations(R.style.bottom_exist_anim_style)
    }

    private fun register() {
        val phone = et_phone.text.toString().replace(" ", "")
        val password = et_password.text.toString()
        val password_ = et_password_.text.toString()
        if (phone.isEmpty()) {
            CustomToast.showError("请输入手机号")
            shakeAnimation(et_phone)
            return
        } else if (!Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$").matcher(phone).matches()) {
            CustomToast.showError("手机号格式不正确")
            return
        } else if (password.isEmpty()) {
            CustomToast.showError("请输入密码")
            shakeAnimation(et_password)
            return
        } else if (password != password_) {
            CustomToast.showError("两次密码不一致")
            shakeAnimation(et_password_)
            return
        }
        et_phone.isEnabled = false
        et_password.isEnabled = false
        et_password_.isEnabled = false
        tv_to_login.isEnabled = false
        iv_close.isEnabled = false
        ViewAnimationUtils.createCircularReveal(btn_register, btn_register.width / 2, btn_register.height / 2, (btn_register.width / 2).toFloat(), (mProgressBar.width / 2).toFloat()).let {
            it.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    mProgressBar.visibility = View.VISIBLE
                    btn_register.visibility = View.INVISIBLE
                    JMessageClient.register(phone, password, object : BasicCallback() {
                        override fun gotResult(p0: Int, p1: String?) {
                            if (p0 == 0) {
                                CustomToast.showSuccess("注册成功")
                                toLogin()
                            } else {
                                CustomToast.showError("注册失败：$p1")
                                et_phone.isEnabled = true
                                et_password.isEnabled = true
                                et_password_.isEnabled = true
                                tv_to_login.isEnabled = true
                                iv_close.isEnabled = true
                                hideProgress()
                            }
                        }
                    })
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })
            it.start()
        }
    }

    private fun hideProgress() {
        ViewAnimationUtils.createCircularReveal(btn_register, btn_register.width / 2, btn_register.height / 2, (mProgressBar.width / 2).toFloat(), (btn_register.width / 2).toFloat()).let {
            it.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {

                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    btn_register.visibility = View.VISIBLE
                    mProgressBar.visibility = View.INVISIBLE
                }
            })
            it.start()
        }
    }

    //晃动动画
    private fun shakeAnimation(view: View) {
        TranslateAnimation(0f, 10f, 0f, 0f).let {
            it.interpolator = CycleInterpolator(5f)
            it.duration = 500
            view.startAnimation(it)
        }
    }

    private fun toLogin() {
        dismiss()
        LoginDialogFragment().show(activity?.supportFragmentManager, "")
    }
}