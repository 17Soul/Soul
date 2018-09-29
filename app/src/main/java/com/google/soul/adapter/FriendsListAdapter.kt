package com.google.soul.adapter

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import com.google.soul.R
import com.google.soul.base.BaseAdapter
import com.google.soul.mvp.model.UserInfoByLetterEntity

/**************************
 *作者：FYX
 *日期：2018/9/19 11:08
 **************************/
class FriendsListAdapter(val context: Context, val bean: ArrayList<UserInfoByLetterEntity>, val layoutId: Int) : BaseAdapter<UserInfoByLetterEntity>(context, bean, layoutId) {

    override fun bindData(holder: ViewHolder, data: UserInfoByLetterEntity, position: Int) {
        holder.setText(R.id.tv_name, data.showName)
        holder.setText(R.id.tv_signature_friend, data.userInfo.signature)
        data.userInfo.getAvatarBitmap(object : GetAvatarBitmapCallback() {
            override fun gotResult(p0: Int, p1: String?, p2: Bitmap?) {
                if (p2 == null) {
                    holder.getView<ImageView>(R.id.iv_avatar).setImageResource(R.mipmap.ic_launcher)
                } else {
                    holder.getView<ImageView>(R.id.iv_avatar).setImageBitmap(p2)
                }
            }
        })
    }

    //获取当前字母第一次出现的位置
    fun getLetterPosition(letter: String): Int {
        for (i in 0 until bean.size) {
            if (bean[i].letter == letter) {
                return i
            }
        }
        return -1
    }
}