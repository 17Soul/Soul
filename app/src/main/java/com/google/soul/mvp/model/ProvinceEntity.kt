package com.google.soul.mvp.model

/**************************
 * 作者：FYX
 * 日期：2018/9/12 16:15
 **************************/

data class ProvinceEntity(val name: String, val code: String, val city: ArrayList<City>) {

    data class City(val name: String, val code: String, val county: ArrayList<County>)

    data class County(val name: String, val code: String)
}
