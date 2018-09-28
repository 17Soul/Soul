package com.google.soul.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.google.soul.R
import com.google.soul.utils.AppUtil
import kotlinx.android.synthetic.main.widget_title_bar.view.*

/**************************
作者：FYX
日期：2018/8/31 0031
自定义标题栏，设置状态栏全屏模式下实现状态栏沉浸式效果
 **************************/
class TitleBar(content: Context, attrs: AttributeSet) : RelativeLayout(content, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_title_bar, this)
        val ta = content.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        title.text = ta.getString(R.styleable.TitleBar_titleBarTitle)
        val leftDrawable = ta.getDrawable(R.styleable.TitleBar_titleBarLeftImage)
        if (leftDrawable != null)
            left_image.setImageDrawable(leftDrawable)
        else
            left_image.visibility = View.GONE
        left_txt.text = ta.getString(R.styleable.TitleBar_titleBarLeftTxt)
        val rightDrawable = ta.getDrawable(R.styleable.TitleBar_titleBarRightImage)
        if (rightDrawable != null)
            right_image.setImageDrawable(rightDrawable)
        else
            right_image.visibility = View.GONE
        right_txt.text = ta.getString(R.styleable.TitleBar_titleBarRightTxt)
        title_layout.setBackgroundColor(ta.getColor(R.styleable.TitleBar_titleBarBackground, Color.parseColor("#000000")))
        ta.recycle()
        title_layout.setPadding(0, AppUtil.getStatusBarHeight(), 0, 0)
    }

    fun setLeftImageResource(resId: Int) {
        left_image.setImageResource(resId)
    }

    fun setRightImageResource(resId: Int) {
        right_image.setImageResource(resId)
    }

    fun setLeftLayoutClickListener(listener: View.OnClickListener) {
        left_layout.setOnClickListener(listener)
    }

    fun setRightLayoutClickListener(listener: View.OnClickListener) {
        right_layout.setOnClickListener(listener)
    }

    fun setTitle(string: String) {
        title.text = string
    }

    fun setLeftTxt(string: String) {
        left_txt.text = string
    }

    fun setRightTxt(string: String) {
        right_txt.text = string
    }

    fun setBackground(color: Int) {
        title_layout.setBackgroundColor(color)
    }
}