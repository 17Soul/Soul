package com.google.soul.adapter

import android.content.Context
import android.widget.ImageView
import cn.jpush.im.android.api.model.Conversation
import com.bumptech.glide.Glide
import com.google.soul.R
import com.google.soul.base.BaseAdapter
import com.google.soul.utils.AppUtil
import com.google.soul.widget.DragBallView

/**************************
 *作者：FYX
 *日期：2018/9/19 11:08
 **************************/
class ConversationListAdapter(val context: Context, val bean: ArrayList<Conversation>, val layoutId: Int) : BaseAdapter<Conversation>(context, bean, layoutId) {

    override fun bindData(holder: ViewHolder, data: Conversation, position: Int) {
        holder.setText(R.id.tv_name, data.title)
        holder.setText(R.id.tv_message, data.latestText)
        holder.setText(R.id.tv_lastMsgDate, AppUtil.getChatTime(data.lastMsgDate))
        holder.getView<DragBallView>(R.id.mDragBallView).setMsgCount(data.unReadMsgCnt)
        if (data.avatarFile != null) {
            Glide.with(context).load(data.avatarFile).into(holder.getView(R.id.iv_avatar))
        } else {
            holder.getView<ImageView>(R.id.iv_avatar).setImageResource(R.mipmap.ic_launcher)
        }
    }
}