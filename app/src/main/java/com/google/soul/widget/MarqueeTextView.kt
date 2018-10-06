package com.google.soul.widget

import android.content.Context
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView

/**************************
 *作者：FYX
 *日期：2018/9/29 14:07
 * 跑马灯
 **************************/
class MarqueeTextView(context: Context, attrs: AttributeSet) : TextView(context, attrs) {

    init {
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.MARQUEE
        isFocusable = true
        marqueeRepeatLimit = -1
        isFocusableInTouchMode = true
    }

    override fun isFocused(): Boolean {
        return true
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect)
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus)
        }
    }
}