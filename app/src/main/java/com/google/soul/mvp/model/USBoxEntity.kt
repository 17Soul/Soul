package com.google.soul.mvp.model

import java.io.Serializable

/**************************
 * 作者：FYX
 * 日期：2018/8/30 0030
 **************************/
data class USBoxEntity(val date: String,
                       val title: String,
                       val subjects: List<SubjectsEntity>) : Serializable
