package com.google.soul.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.google.soul.R
import com.google.soul.base.BaseAdapter
import com.google.soul.mvp.model.PersonnelEntity

/**************************
作者：FYX
日期：2018/8/27 0027
 **************************/
class MovieCharacterAdapter(mContext: Context, bean: ArrayList<PersonnelEntity>, layoutId: Int) : BaseAdapter<PersonnelEntity>(mContext, bean, layoutId) {

    override fun bindData(holder: ViewHolder, data: PersonnelEntity, position: Int) {
        if (data.avatars != null) {
            Glide.with(mContext).load(data.avatars.large).into(holder.getView(R.id.iv_head))
        }
        holder.setText(R.id.tv_name, data.name)
    }
}