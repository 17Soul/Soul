package com.google.soul.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.soul.R
import com.google.soul.base.BaseAdapter
import com.google.soul.mvp.model.SubjectEntity
import com.google.soul.widget.ratingbar.RatingBar

/**************************
作者：FYX
日期：2018/8/26 0026
 **************************/
class MovieOverviewAdapter(mContext: Context, movieEntity: ArrayList<SubjectEntity>, layoutId: Int) : BaseAdapter<SubjectEntity>(mContext, movieEntity, layoutId) {

    override fun bindData(holder: ViewHolder, data: SubjectEntity, position: Int) {
        Glide.with(mContext).load(data.images.large).transition(DrawableTransitionOptions().crossFade()).into(holder.getView(R.id.iv_pic))
        holder.setText(R.id.tv_title, data.title)
        holder.setText(R.id.tv_rating, data.rating.average.toString())
        holder.getView<RatingBar>(R.id.mRatingBar).starProgress = data.rating.average.toFloat()
    }
}