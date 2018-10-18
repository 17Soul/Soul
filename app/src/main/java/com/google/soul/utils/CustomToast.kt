package com.google.soul.utils

import android.view.LayoutInflater
import android.widget.Toast
import com.google.soul.App
import com.google.soul.R
import kotlinx.android.synthetic.main.toast_view.view.*

/**************************
作者：FYX
日期：2018/9/6 0006
 **************************/
object CustomToast {

    private val toast = Toast(App.appInst)

    init {
        toast.duration = Toast.LENGTH_SHORT
    }

    fun show(message: String) {
        val view = LayoutInflater.from(App.appInst).inflate(R.layout.toast_view, null)
        toast.view = view
        view.tv_message.text = message
        view.iv_logo.setImageResource(R.mipmap.ic_launcher)
        toast.show()
    }

    fun showSuccess(message: String) {
        val view = LayoutInflater.from(App.appInst).inflate(R.layout.toast_view, null)
        toast.view = view
        view.tv_message.text = message
        view.iv_logo.setImageResource(R.mipmap.ic_success)
        toast.show()
    }

    fun showError(message: String) {
        val view = LayoutInflater.from(App.appInst).inflate(R.layout.toast_view, null)
        toast.view = view
        view.tv_message.text = message
        view.iv_logo.setImageResource(R.mipmap.ic_error)
        toast.show()
    }
}