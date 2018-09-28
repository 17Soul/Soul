package com.google.soul.mvp.model

import java.io.Serializable

/**************************
作者：FYX
日期：2018/8/30 0030
 **************************/
class PersonnelEntity(val avatars: ImagesEntity,
                      val name_en: String?,
                      val name: String,
                      val alt: String,
                      val id: String): Serializable