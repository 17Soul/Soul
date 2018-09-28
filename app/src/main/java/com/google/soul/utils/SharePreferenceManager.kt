package com.google.soul.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.soul.App

/**************************
作者：FYX
日期：2018/9/6 0006
 **************************/
object SharePreferenceManager {

    private const val sharePreferenceName = "Soul"
    private val sp: SharedPreferences = App.appInst.getSharedPreferences(sharePreferenceName, Context.MODE_PRIVATE)

    //本地主题
    private const val THEME = "theme"
    //上次登录手机号码
    private const val LAST_PHONE = "last_phone"

    fun setTheme(theme: Int) {
        sp.edit().putInt(THEME, theme).apply()
    }

    fun getTheme(): Int = sp.getInt(THEME, 0)

    fun setLastPhone(lastPhone: String) {
        sp.edit().putString(LAST_PHONE, lastPhone).apply()
    }

    fun getLastPhone(): String = sp.getString(LAST_PHONE, "")
}