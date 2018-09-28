package com.google.soul.utils

import android.text.TextUtils
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
}