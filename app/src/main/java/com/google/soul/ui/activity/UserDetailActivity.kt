package com.google.soul.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.google.soul.App
import com.google.soul.R
import com.google.soul.base.BaseActivity
import com.google.soul.utils.AppUtil
import com.google.soul.utils.CustomToast
import com.google.soul.widget.InputDialog
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : BaseActivity() {

    var username = ""

    override fun layoutId(): Int = R.layout.activity_user_detail

    override fun initData() {
        mTitleBar.setLeftLayoutClickListener(View.OnClickListener {
            finishAfterTransition()
        })
        username = intent.extras.getString("username")
        JMessageClient.getUserInfo(username, object : GetUserInfoCallback() {
            override fun gotResult(p0: Int, p1: String?, p2: UserInfo?) {
                p2?.let {
                    it.getAvatarBitmap(object : GetAvatarBitmapCallback() {
                        override fun gotResult(p0: Int, p1: String?, p2: Bitmap?) {
                            if (p2 == null) {
                                iv_avatar.setImageResource(R.mipmap.ic_launcher)
                            } else {
                                iv_avatar.setImageBitmap(p2)
                            }
                        }
                    })
                    if (it.notename.isNullOrEmpty()) {
                        tv_noteName.text = it.nickname
                    } else {
                        tv_noteName.text = it.notename
                    }
                    when (it.gender) {
                        UserInfo.Gender.female -> iv_gender.setImageResource(R.mipmap.ic_women)
                        UserInfo.Gender.male -> iv_gender.setImageResource(R.mipmap.ic_man)
                    }
                    tv_username.text = "账号:${it.userName}"
                    tv_nickname.text = "昵称:${it.nickname}"
                    tv_signature.text = it.signature
                    tv_birthday.text = AppUtil.unixTimestampToTime(it.birthday, "yyyy-MM-dd")
                    tv_region.text = it.region
                    if (it.isFriend) {
                        btn_send.visibility = View.VISIBLE
                    } else {
                        btn_add.visibility = View.VISIBLE
                    }
                }
            }
        })
        iv_avatar.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", 1)
            bundle.putString("key", username)
            startActivity(ImagePreviewActivity::class.java, bundle)
        }
        btn_add.setOnClickListener {
            InputDialog().title("验证信息").defaultText("你好！").rightText("发送").setOkClick(object : InputDialog.OnOkClickListener {
                override fun onOkClick(string: String) {
                    ContactManager.sendInvitationRequest(username, AppUtil.appKey, string, object : BasicCallback() {
                        override fun gotResult(p0: Int, p1: String?) {
                            if (p0 == 0) {
                                CustomToast.showSuccess("申请成功")
                            } else {
                                CustomToast.showError("申请失败:$p1")
                            }
                        }
                    })
                }
            }).show(supportFragmentManager, "")
        }
    }
}
