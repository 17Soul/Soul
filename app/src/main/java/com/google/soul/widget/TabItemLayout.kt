package com.google.soul.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.google.soul.R
import kotlinx.android.synthetic.main.widget_tab_layout.view.*

/**************************
 *作者：FYX
 *日期：2018/9/9 17:05
 **************************/
class TabItemLayout(content: Context, attrs: AttributeSet) : RelativeLayout(content, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_tab_layout, this)
        val ta = content.obtainStyledAttributes(attrs, R.styleable.TabItemLayout)
        tv_text.text = ta.getString(R.styleable.TabItemLayout_tab_text)
        ta.getDrawable(R.styleable.TabItemLayout_tab_image)?.let {
            iv_image.setImageDrawable(it)
        }
        ta.recycle()
    }
}