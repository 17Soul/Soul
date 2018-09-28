package com.google.soul.mvp.model

import java.io.Serializable

/**************************
 * 作者：FYX
 * 日期：2018/8/30 0030
 *************************/

data class MovieDetailEntity(val rating: RatingEntity, val reviews_count: Int, val wish_count: Int, val douban_site: String, val year: String, val images: ImagesEntity, val alt: String, val id: String, val mobile_url: String, val title: String, val do_count: Any, val share_url: String, val seasons_count: Any, val schedule_url: String, val episodes_count: Any, val collect_count: Int, val current_season: Any, val original_title: String, val summary: String, val subtype: String, val comments_count: Int, val ratings_count: Int, val countries: List<String>, val genres: List<String>, val casts: ArrayList<PersonnelEntity>, val directors: ArrayList<PersonnelEntity>, val aka: List<String>):Serializable
