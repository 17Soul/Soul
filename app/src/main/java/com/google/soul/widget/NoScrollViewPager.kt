package com.google.soul.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet

/**************************
作者：FYX
日期：2018/8/26 0026
解决外部布局有ScrollView不能自适应高度
 **************************/
class NoScrollViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = 0
        for (i in 0 until childCount) {
            getChildAt(i).run {
                measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
                if (measuredHeight > height)
                    height = measuredHeight
            }
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    }
}