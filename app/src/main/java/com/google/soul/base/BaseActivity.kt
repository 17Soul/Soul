package com.google.soul.base

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.soul.R
import com.google.soul.dialog.LoadingDialogFragment
import com.google.soul.utils.SharePreferenceManager

/**************************
作者：FYX
日期：2018/8/23 0023
 **************************/
abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext: Context
    lateinit var mSavedInstanceState: Bundle//保存Activity状态

    val mLoadingDialog = LoadingDialogFragment()//等待

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()//设置主题颜色,在setContentView之前
        setContentView(layoutId())
        setTranslucentStatus()//设置全屏和状态栏透明
        mContext = this
        savedInstanceState?.let { mSavedInstanceState = it }
        initData()
    }

    private fun setTranslucentStatus() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
    }

    abstract fun layoutId(): Int

    abstract fun initData()

    fun startActivity(clazz: Class<*>) {
        startActivity(clazz, null)
    }

    fun startActivity(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clazz)
        bundle?.let { intent.putExtras(it) }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun setTheme() {
        val themeId = SharePreferenceManager.getTheme()
        when (themeId) {
            0 -> setTheme(R.style.BlueTheme)
            1 -> setTheme(R.style.RedTheme)
            2 -> setTheme(R.style.BrownTheme)
            3 -> setTheme(R.style.GreenTheme)
            4 -> setTheme(R.style.PurpleTheme)
            5 -> setTheme(R.style.TealTheme)
            6 -> setTheme(R.style.PinkTheme)
            7 -> setTheme(R.style.DeepPurpleTheme)
            8 -> setTheme(R.style.OrangeTheme)
            9 -> setTheme(R.style.IndigoTheme)
            10 -> setTheme(R.style.CyanTheme)
            11 -> setTheme(R.style.LightGreenTheme)
            12 -> setTheme(R.style.LimeTheme)
            13 -> setTheme(R.style.DeepOrangeTheme)
            14 -> setTheme(R.style.BlueGreyTheme)
        }
    }
}