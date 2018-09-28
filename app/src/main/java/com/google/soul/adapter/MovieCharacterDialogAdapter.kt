package com.google.soul.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.google.soul.R
import com.google.soul.base.BaseAdapter
import com.google.soul.mvp.model.CharacterEntity
import com.google.soul.widget.ratingbar.RatingBar

/**************************
作者：FYX
日期：2018/8/27 0027
 **************************/
class MovieCharacterDialogAdapter(mContext: Context, data: ArrayList<CharacterEntity.WorksBean>, layoutId: Int) : BaseAdapter<CharacterEntity.WorksBean>(mContext, data, layoutId) {

    override fun bindData(holder: ViewHolder, data: CharacterEntity.WorksBean, position: Int) {
        Glide.with(mContext).load(data.subject.images.large).into(holder.getView(R.id.iv_pic))
        holder.setText(R.id.tv_name, data.subject.title)
        holder.setText(R.id.tv_score, data.subject.rating.average.toString())
        holder.getView<RatingBar>(R.id.mRatingBar).starProgress = data.subject.rating.average.toFloat()
        holder.setText(R.id.tv_types, getStringToListString(data.subject.genres, "/"))
        if (data.subject.directors.isNotEmpty()) {
            holder.setText(R.id.tv_director, "导演:${data.subject.directors[0].name}")
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