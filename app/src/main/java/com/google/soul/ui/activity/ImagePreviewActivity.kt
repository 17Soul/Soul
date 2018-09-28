package com.google.soul.ui.activity

import android.graphics.Bitmap
import android.view.View
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.model.UserInfo
import com.bumptech.glide.Glide
import com.google.soul.R
import com.google.soul.base.BaseActivity
import kotlinx.android.synthetic.main.activity_image_preview.*

class ImagePreviewActivity : BaseActivity() {

    private var type = 0
    private var key = ""

    override fun layoutId(): Int = R.layout.activity_image_preview

    override fun initData() {
        type = intent.extras.getInt("type")
        key = intent.extras.getString("key")
        //0-URL 1-用户userName
        if (type == 0) {
            Glide.with(mContext).load(key).into(mPhotoView)
            mProgressBar.visibility = View.GONE
        } else if (type == 1) {
            JMessageClient.getUserInfo(key, object : GetUserInfoCallback() {
                override fun gotResult(p0: Int, p1: String, p2: UserInfo) {
                    p2.getBigAvatarBitmap(object : GetAvatarBitmapCallback() {
                        override fun gotResult(p0: Int, p1: String?, p2: Bitmap?) {
                            mPhotoView.setImageBitmap(p2)
                            mProgressBar.visibility = View.GONE
                        }
                    })
                }
            })
        }
        mPhotoView.setOnPhotoTapListener { _, _, _ ->
            finish()
        }
    }
}
