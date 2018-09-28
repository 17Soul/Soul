package com.google.soul.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.callback.GetUserInfoListCallback
import cn.jpush.im.android.api.model.UserInfo
import com.google.soul.R
import com.google.soul.adapter.FriendsListAdapter
import com.google.soul.base.BaseAdapter
import com.google.soul.base.BaseFragment
import com.google.soul.mvp.model.UserInfoByLetterEntity
import com.google.soul.ui.activity.UserDetailActivity
import com.google.soul.utils.CustomToast
import com.google.soul.widget.InputDialog
import com.google.soul.widget.PinnedSectionDecoration
import kotlinx.android.synthetic.main.fragment_friends.*
import java.util.*

/**************************
作者：FYX
日期：2018/8/24 0024
 **************************/
class FriendsFragment : BaseFragment() {

    private val userInfoByLetterList = ArrayList<UserInfoByLetterEntity>()
    private var friendsListAdapter: FriendsListAdapter? = null
    private var popupWindow: PopupWindow = PopupWindow()

    override fun layoutId(): Int = R.layout.fragment_friends

    override fun init() {
        friendsListAdapter = FriendsListAdapter(mContext, userInfoByLetterList, R.layout.item_friends)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.addItemDecoration(PinnedSectionDecoration(mContext, object : PinnedSectionDecoration.PinnedHeaderCreator {
            override fun getGroupString(position: Int): String {
                return userInfoByLetterList[position].letter
            }
        }))
        mRecyclerView.adapter = friendsListAdapter
        mWaveSideBarView.setOnTouchLetterChangeListener {
            val pos = friendsListAdapter?.getLetterPosition(it)
            pos?.let {
                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos)
                    val mLayoutManager = mRecyclerView.layoutManager as LinearLayoutManager
                    mLayoutManager.scrollToPositionWithOffset(pos, 0)
                }
            }
        }
        mSmartRefreshLayout.setOnRefreshListener {
            loadFriends()
        }
        friendsListAdapter?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                val userInfo = (obj as UserInfoByLetterEntity).userInfo
                val bundle = Bundle()
                bundle.putString("username", userInfo.userName)
                startActivity(UserDetailActivity::class.java, bundle)
            }
        })
        mTitleBar.setRightLayoutClickListener(View.OnClickListener {
            showFriendsMenu()
        })
    }

    private fun loadFriends() {
        userInfoByLetterList.clear()
        ContactManager.getFriendList(object : GetUserInfoListCallback() {
            override fun gotResult(p0: Int, p1: String?, p2: MutableList<UserInfo>) {
                if (p0 == 0) {
                    for (i in 0 until p2.size) {
                        userInfoByLetterList.add(UserInfoByLetterEntity(p2[i]))
                    }
                    userInfoByLetterList.sort()
                    friendsListAdapter?.notifyDataSetChanged()
                    mSmartRefreshLayout.finishRefresh()
                }
            }
        })
    }

    override fun lazyLoad() {
        mSmartRefreshLayout.autoRefresh()
    }

    private fun showFriendsMenu() {
        // 设置布局文件
        popupWindow.contentView = LayoutInflater.from(mContext).inflate(R.layout.menu_add_friend, null)
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        popupWindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置pop透明效果
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 设置pop出入动画
        popupWindow.animationStyle = R.style.pop_add
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        popupWindow.isFocusable = true
        // 设置pop可点击，为false点击事件无效，默认为true
        popupWindow.isTouchable = true
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        popupWindow.isOutsideTouchable = true
        // 相对于 + 号正下面，同时可以设置偏移量
        popupWindow.showAsDropDown(mTitleBar, 730, 0)
        // 设置pop关闭监听，用于改变背景透明度
//        popupWindow.setOnDismissListener {
//            backgroundAlpha(1f)
//        }
//        backgroundAlpha(0.7f)
        popupWindow.contentView.findViewById<TextView>(R.id.tv_add).setOnClickListener {
            popupWindow.dismiss()
            InputDialog().title("通过账号查找好友").rightText("查找").setInputType(InputType.TYPE_CLASS_PHONE).setOkClick(object : InputDialog.OnOkClickListener {
                override fun onOkClick(string: String) {
                    searchFriend(string)
                }
            }).show(activity?.supportFragmentManager, "")
        }
    }

    //设置背景
    private fun backgroundAlpha(bgAlpha: Float) {
        activity?.window?.let {
            it.attributes.alpha = bgAlpha
            activity?.window?.attributes = it.attributes
        }
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    private fun searchFriend(string: String) {
        JMessageClient.getUserInfo(string, object : GetUserInfoCallback() {
            override fun gotResult(p0: Int, p1: String?, p2: UserInfo?) {
                if (p0 == 0) {
                    val bundle = Bundle()
                    bundle.putString("username", p2?.userName)
                    startActivity(UserDetailActivity::class.java, bundle)
                } else {
                    CustomToast.showError("账户不存在")
                }
            }
        })
    }
}