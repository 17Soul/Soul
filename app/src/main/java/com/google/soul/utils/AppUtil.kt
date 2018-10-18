package com.google.soul.utils

import android.content.Context
import android.graphics.PointF
import android.text.TextUtils
import android.view.WindowManager
import com.google.soul.App
import com.google.soul.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**************************
作者：FYX
日期：2018/8/27 0027
 **************************/
object AppUtil {

    val appKey = "4a3247924fa4115f6e749530"

    /**
     * 获取手机状态栏高度
     */
    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = App.appInst.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = App.appInst.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 获取当前时间
     */
    fun getCurrentTime(format: String): String {
        return SimpleDateFormat(format, Locale.CHINA).format(Date(System.currentTimeMillis()))
    }

    /**
     * 时间戳转换普通时间
     */
    fun unixTimestampToTime(unixTimestamp: Long, format: String): String {
        return SimpleDateFormat(format, Locale.CHINA).format(Date(unixTimestamp))
    }

    /**
     * 判断是否是今天
     */
    fun isToday(unixTimestamp: Long): Boolean {
        val calendar1 = Calendar.getInstance()
        calendar1.time = Date(unixTimestamp)
        val calendar2 = Calendar.getInstance()
        calendar2.time = Date(System.currentTimeMillis())
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
            if (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {
                if (calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 获取当前所有主题颜色资源ID
     */
    fun getThemeColors(): ArrayList<Int> {
        val colors = App.appInst.resources.obtainTypedArray(R.array.colors)
        val colorsId = ArrayList<Int>()
        for (i in 0 until colors.length()) {
            colorsId.add(colors.getResourceId(i, 0))
        }
        colors.recycle()
        return colorsId
    }

    /**
     * 获取当前主题颜色ID
     */
    fun getThemeColor(): Int {
        val colors = App.appInst.resources.obtainTypedArray(R.array.colors)
        val colorId = colors.getColor(SharePreferenceManager.getTheme(), 0)
        colors.recycle()
        return colorId
    }

    /**
     * 获取本地Json
     */
    fun getJson(fileName: String): String {
        val stringBuilder = StringBuilder()
        //获得assets资源管理器
        val assetManager = App.appInst.assets
        //使用IO流读取json文件内容
        try {
            val bufferedReader = BufferedReader(InputStreamReader(assetManager.open(fileName), "utf-8"))
            var line: String?
            do {
                line = bufferedReader.readLine()
                if (line != null) {
                    stringBuilder.append(line)
                } else {
                    break
                }
            } while (true)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    /**
     * 根据字符串获取当前首字母
     */
    fun getLetter(name: String): String {
        val defaultLetter = "#"
        if (TextUtils.isEmpty(name)) {
            return defaultLetter
        }
        val char0 = name.toUpperCase()[0]
        if (Character.isDigit(char0)) {
            return defaultLetter
        }
        val l = HanziToPinyin.getInstance().get(name.substring(0, 1))
        if (l != null && l.size > 0 && l[0].target.isNotEmpty()) {
            val token = l[0]
            val letter = token.target.substring(0, 1).toUpperCase()
            val c = letter[0]
            return if (c < 'A' || c > 'Z') {
                defaultLetter
            } else letter
        }
        return defaultLetter
    }

    /**
     * 二阶贝塞尔曲线
     * B(t) = Po*(1-t)^2 + 2*p1*t*(1-t)+t^2*p2
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点
     * @param p2 终止点
     * @return t对应的点
     */
    fun CalculateBezierPointForQuadratic(t: Float, p0: PointF, p1: PointF, p2: PointF): PointF {
        val point = PointF()
        val temp = 1 - t
        //      point.x = temp * temp * p0.x + 2 * t * temp * p1.x + t * t * p2.x;
        //      point.y = temp * temp * p0.y + 2 * t * temp * p1.y + t * t * p2.y;
        point.x = (Math.pow(temp.toDouble(), 2.0) * p0.x + (2f * t * temp * p1.x).toDouble() + Math.pow(t.toDouble(), 2.0) * p2.x).toFloat()
        point.y = (Math.pow(temp.toDouble(), 2.0) * p0.y + (2f * t * temp * p1.y).toDouble() + Math.pow(t.toDouble(), 2.0) * p2.y).toFloat()
        return point
    }

    /**
     * 获取屏幕分辨率
     * @param context
     * @return
     */
    fun getScreenDispaly(context: Context): IntArray {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = windowManager.defaultDisplay.width// 手机屏幕的宽度
        val height = windowManager.defaultDisplay.height// 手机屏幕的高度
        return intArrayOf(width, height)
    }

    /** 获取屏幕宽度  */
    fun getDisplayWidth(context: Context?): Int {
        if (context != null) {
            val dm = context.resources.displayMetrics
            // int h_screen = dm.heightPixels;
            return dm.widthPixels
        }
        return 720
    }

    /** 获取屏幕高度  */
    fun getDisplayHight(context: Context?): Int {
        if (context != null) {
            val dm = context.resources.displayMetrics
            // int w_screen = dm.widthPixels;
            return dm.heightPixels
        }
        return 1280
    }

    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 获取以前或者以后的时间
     */
    fun getBeforeAndAfterTime(format: String, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date(System.currentTimeMillis())
        calendar.add(Calendar.DAY_OF_MONTH, day)
        return SimpleDateFormat(format, Locale.CHINA).format(calendar.time)
    }

    fun getChatTime(unixTimestamp: Long): String {
        var result = unixTimestampToTime(unixTimestamp, "MM月dd日")
        val todayCalendar = Calendar.getInstance()
        val otherCalendar = Calendar.getInstance()
        otherCalendar.timeInMillis = unixTimestamp
        if (todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR)) {
            if (todayCalendar.get(Calendar.MONTH) == otherCalendar.get(Calendar.MONTH)) {
                when (todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE)) {
                    0 -> result = unixTimestampToTime(unixTimestamp, "HH:mm")
                    1 -> result = "昨天 ${unixTimestampToTime(unixTimestamp, "HH:mm")}"
                    2, 3, 4, 5, 6 -> {
                        if (todayCalendar.get(Calendar.WEEK_OF_MONTH) == otherCalendar.get(Calendar.WEEK_OF_MONTH)) {
                            when (otherCalendar.get(Calendar.DAY_OF_WEEK)) {
                                1 -> result = "周日 ${unixTimestampToTime(unixTimestamp, "HH:mm")}"
                                2 -> result = "周一 ${unixTimestampToTime(unixTimestamp, "HH:mm")}"
                                3 -> result = "周二 ${unixTimestampToTime(unixTimestamp, "HH:mm")}"
                                4 -> result = "周三 ${unixTimestampToTime(unixTimestamp, "HH:mm")}"
                                5 -> result = "周四 ${unixTimestampToTime(unixTimestamp, "HH:mm")}"
                                6 -> result = "周五 ${unixTimestampToTime(unixTimestamp, "HH:mm")}"
                                7 -> result = "周六 ${unixTimestampToTime(unixTimestamp, "HH:mm")}"
                            }
                        }
                    }
                }
            }
        } else {
            result = unixTimestampToTime(unixTimestamp, "yyyy年MM月dd日")
        }
        return result
    }
}