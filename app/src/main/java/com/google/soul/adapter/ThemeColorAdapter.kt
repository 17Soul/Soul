package com.google.soul.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.google.soul.R
import com.google.soul.base.BaseAdapter
import com.google.soul.utils.SharePreferenceManager
import com.google.soul.widget.CircleImageView

/**************************
作者：FYX
日期：2018/8/28 0028
 **************************/
class ThemeColorAdapter(var context: Context, var bean: ArrayList<Int>, var layoutId: Int) : BaseAdapter<Int>(context, bean, layoutId) {

    override fun bindData(holder: ViewHolder, data: Int, position: Int) {
        if (position == SharePreferenceManager.getTheme()) {
            holder.getView<ImageView>(R.id.iv_selected).visibility = View.VISIBLE
        } else {
            holder.getView<ImageView>(R.id.iv_selected).visibility = View.GONE
        }
        holder.getView<CircleImageView>(R.id.ci_color).setImageResource(data)
    }
}