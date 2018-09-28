package com.google.soul.mvp.model

/**************************
 *作者：FYX
 *日期：2018/9/25 16:20
 **************************/
data class VO<T>(val error: Int, val msg: String, val data: T)