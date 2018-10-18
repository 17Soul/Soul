package com.google.soul.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.view.View
import android.view.animation.DecelerateInterpolator
import java.lang.Math.abs

/**************************
 *作者：FYX
 *日期：2018/10/16 09:47
 **************************/
class LotteryView(context: Context) : View(context) {

    private val data = ArrayList<Any>()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mHeight = 90
    private var maxHeight = mHeight * (data.size - 1)
    private val mDuration = 5000L

    init {
        mPaint.color = Color.WHITE
        mPaint.textSize = 60f
        mPaint.typeface = Typeface.DEFAULT_BOLD
        mPaint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val x = (width / 2).toFloat()
        val fontMetrics = mPaint.fontMetrics
        for (i in 0 until data.size) {
            val y = (height / 2 + (abs(fontMetrics.ascent) - fontMetrics.descent) / 2) - (height * i)
            canvas?.drawText(data[i].toString(), x, y, mPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mHeight, mHeight)
    }

    fun run(end: Int) {
        run(0, end)
    }

    fun run(startIndex: Int, endIndex: Int) {
        run(startIndex, endIndex, 0)
    }

    fun run(startIndex: Int, endIndex: Int, delay: Long) {
        if (startIndex < data.size - 1 && endIndex < data.size - 1) {
            val startValue = mHeight * startIndex
            val endValue = if (startIndex <= endIndex) {
                mHeight * abs(endIndex - startIndex)
            } else {
                mHeight * abs(data.size - 1 - startIndex + endIndex)
            }
            scrollTo(0, -startValue)
            ValueAnimator.ofInt(startValue, maxHeight + startValue + endValue).run {
                addUpdateListener {
                    if (it.animatedValue as Int >= maxHeight) {
                        scrollTo(0, -(it.animatedValue as Int % maxHeight))
                    } else {
                        scrollTo(0, -(it.animatedValue as Int))
                    }
                }
                interpolator = DecelerateInterpolator()
                duration = mDuration
                startDelay = delay
                start()
            }
        } else {
            scrollTo(0, 0)
        }
    }

    fun setNumberList(maxNumber: Int) {
        setNumberList(0, maxNumber)
    }

    fun setNumberList(minNumber: Int, maxNumber: Int) {
        data.clear()
        if (maxNumber > minNumber) {
            for (i in minNumber..maxNumber) {
                data.add(i)
            }
            data.add(minNumber)
            maxHeight = mHeight * (data.size - 1)
        }
    }

    fun setStringList(stringList: ArrayList<String>) {
        data.clear()
        data.addAll(stringList)
        data.add(stringList[0])
        maxHeight = mHeight * (data.size - 1)
    }
}