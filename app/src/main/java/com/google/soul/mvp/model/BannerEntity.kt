package com.google.soul.mvp.model

/**************************
作者：FYX
日期：2018/8/24 0024
 **************************/
data class BannerEntity(val tooltips: TooltipsBean, val images: List<ImagesBean>) {

    data class TooltipsBean(val loading: String, val previous: String, val next: String, val walle: String, val walls: String)

    data class ImagesBean(val startdate: String, val fullstartdate: String, val enddate: String, val url: String, val urlbase: String, val copyright: String, val copyrightlink: String, val quiz: String, val isWp: Boolean, val hsh: String, val drk: Int, val top: Int, val bot: Int, val hs: List<*>)
}