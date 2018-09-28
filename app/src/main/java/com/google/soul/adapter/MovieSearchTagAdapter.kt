package com.google.soul.adapter

import android.content.Context
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.soul.R
import com.google.soul.base.BaseAdapter

/**************************
作者：FYX
日期：2018/8/27 0027
 **************************/
class MovieSearchTagAdapter(mContext: Context, data: ArrayList<String>, layoutId: Int) : BaseAdapter<String>(mContext, data, layoutId) {

    override fun bindData(holder: ViewHolder, data: String, position: Int) {
        val params = holder.getView<TextView>(R.id.tv_tag).layoutParams
        if (params is FlexboxLayoutManager.LayoutParams) {
            params.flexGrow = 0.5f
        }
        holder.getView<TextView>(R.id.tv_tag).text = data
    }
}