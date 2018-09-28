package com.google.soul.mvp.model

import java.io.Serializable

/**************************
作者：FYX
日期：2018/8/30 0030
 **************************/
data class SubjectEntity(val rating: RatingEntity,
                         val title: String,
                         val collect_count: Int,
                         val mainland_pubdate: String,
                         val isHas_video: Boolean,
                         val original_title: String,
                         val subtype: String,
                         val year: String,
                         val images: ImagesEntity,
                         val alt: String,
                         val id: String,
                         val genres: List<String>,
                         val casts: List<PersonnelEntity>,
                         val durations: List<String>,
                         val directors: List<PersonnelEntity>,
                         val pubdates: List<String>) : Serializable