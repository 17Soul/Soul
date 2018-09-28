package com.google.soul.mvp.model

import java.io.Serializable

/**************************
作者：FYX
日期：2018/8/30 0030
 **************************/
data class SubjectsEntity(val box: Int = 0,
                          val new: Boolean,
                          val rank: Int,
                          val subject: SubjectEntity) : Serializable