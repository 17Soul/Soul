package com.google.soul.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.soul.R
import com.google.soul.base.BaseAdapter
import com.google.soul.mvp.model.SubjectsEntity
import com.google.soul.widget.ratingbar.RatingBar

/**************************
作者：FYX
日期：2018/8/28 0028
 **************************/
class AmericanBoxAdapter(var context: Context, var bean: ArrayList<SubjectsEntity>, var layoutId: Int) : BaseAdapter<SubjectsEntity>(context, bean, layoutId) {

    override fun bindData(holder: ViewHolder, data: SubjectsEntity, position: Int) {
        Glide.with(context).load(data.subject.images.large).into(holder.getView(R.id.iv_pic))
        holder.setText(R.id.tv_name, "${data.subject.title}(${data.box / 10000}万/美元)")
        holder.setText(R.id.tv_score, data.subject.rating.average.toString())
        holder.getView<RatingBar>(R.id.mRatingBar).starProgress = data.subject.rating.average.toFloat()
        holder.setText(R.id.tv_types, getStringToListString(data.subject.genres, "/"))
        holder.setText(R.id.tv_director, "导演:${data.subject.directors[0].name}")
        if (data.new) {
            holder.getView<ImageView>(R.id.iv_new).visibility = View.VISIBLE
        } else {
            holder.getView<ImageView>(R.id.iv_new).visibility = View.GONE
        }
    }

    /**
     * 将List<String>转换成String
     */
    private fun getStringToListString(list: List<String>, dex: String): String {
        val stringBuffer = StringBuffer()
        for (i in list.indices) {
            if (i == list.size - 1)
                stringBuffer.append(list[i])
            else
                stringBuffer.append(list[i] + dex)
        }
        return stringBuffer.toString()
    }
}