package com.google.soul.mvp.model

import java.io.Serializable

/**************************
 * 作者：FYX
 * 日期：2018/8/30 0030
 **************************/
data class MovieEntity(val count: Int,
                       val start: Int,
                       val total: Int,
                       val title: String,
                       val subjects: List<SubjectEntity>) : Serializable
