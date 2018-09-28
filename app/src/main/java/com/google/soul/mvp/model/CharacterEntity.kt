package com.google.soul.mvp.model

import java.io.Serializable

/**************************
 * 作者：FYX
 * 日期：2018/8/29 0029
 *************************/
data class CharacterEntity(val mobile_url: String, val name: String, val gender: String, val avatars: ImagesEntity, val id: String, val name_en: String, val born_place: String, val alt: String, val aka_en: List<String>, val works: ArrayList<WorksBean>, val aka: List<String>) : Serializable {

    data class WorksBean(val subject: SubjectEntity, val roles: List<String>) : Serializable
}
