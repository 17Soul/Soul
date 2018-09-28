package com.google.soul

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import cn.jpush.im.android.api.JMessageClient
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import java.util.*

/**************************
作者：FYX
日期：2018/8/23 0023
 **************************/

class App : Application(), Application.ActivityLifecycleCallbacks {

    private var activityStack: Stack<Activity> = Stack()//当前activity集合
    private val tag = "Application"

    companion object {
        lateinit var appInst: App
    }

    override fun onCreate() {
        super.onCreate()
        appInst = this//实现Application单例
        registerActivityLifecycleCallbacks(this)//注册Activity全局监听
        Logger.addLogAdapter(AndroidLogAdapter())//设置Logger适配器
        JMessageClient.init(this, true)
        JMessageClient.setDebugMode(true)
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(tag, "onPaused:" + activity.componentName.className)
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(tag, "onResumed:" + activity.componentName.className)
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(tag, "onStarted:" + activity.componentName.className)
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(tag, "onDestroyed:" + activity.componentName.className)
        finishActivity(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
    override fun onActivityStopped(activity: Activity) {
        Log.d(tag, "onStopped:" + activity.componentName.className)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d(tag, "onCreated:" + activity.componentName.className)
        addActivity(activity)
    }

    /**
     * 获取当前Activity数量
     */
    fun getActivityNum(): Int {
        return activityStack.size
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack.lastElement()
    }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity) {
        activityStack.remove(activity)
        activity.finish()
    }

    /**
     * 结束堆栈中最后一个压入以外的Activity
     */
    fun finishOtherActivity() {
        val activityDelete: Stack<Activity> = Stack()
        for (activity in activityStack) {
            if (currentActivity() != activity) {
                activity?.finish()
                activityDelete.add(activity)
            }
        }
        activityStack.removeAll(activityDelete)
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        for (activity in activityStack) {
            activity?.finish()
        }
        activityStack.clear()
    }
}