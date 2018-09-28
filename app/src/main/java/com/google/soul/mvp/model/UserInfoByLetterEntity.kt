package com.google.soul.mvp.model

import cn.jpush.im.android.api.model.UserInfo
import com.google.soul.utils.AppUtil

/**************************
 *作者：FYX
 *日期：2018/9/17 17:59
 **************************/
data class UserInfoByLetterEntity(val userInfo: UserInfo) : Comparable<UserInfoByLetterEntity> {

    //展示名字段
    var showName: String = ""
    //字母
    var letter: String = ""

    init {
        showName =
                if (userInfo.notename.isNullOrEmpty()) {
                    if (userInfo.nickname.isNullOrEmpty()) {
                        userInfo.userName
                    } else {
                        userInfo.nickname
                    }
                } else {
                    userInfo.notename
                }
        letter = AppUtil.getLetter(showName)
    }

    override fun compareTo(other: UserInfoByLetterEntity): Int {
        if ("#" == this.letter) {
            return 1
        } else if ("#" == other.letter) {
            return -1
        }
        return this.letter.compareTo(other.letter)
    }
}