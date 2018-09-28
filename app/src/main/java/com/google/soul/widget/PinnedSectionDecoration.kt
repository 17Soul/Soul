package com.google.soul.widget

import android.content.Context
import android.graphics.*
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.soul.R
import android.text.TextPaint
import com.google.soul.utils.AppUtil


/**************************
 *作者：FYX
 *日期：2018/9/19 14:19
 **************************/
class PinnedSectionDecoration(context: Context, pinnedHeaderCreator: PinnedHeaderCreator) : RecyclerView.ItemDecoration() {

    private var dividerPaint: Paint = Paint()
    //底部分割线高度
    private var dividerHeight: Int
    //顶部字母高度
    private val dividerHeightTop: Int
    private val pinnedHeaderCallback = pinnedHeaderCreator
    private val textPaint: TextPaint = TextPaint()

    init {
        //设置分割线颜色，高度
        dividerPaint.color = context.resources.getColor(R.color.colorGrayWhite)
        dividerHeight = context.resources.getDimensionPixelSize(R.dimen.divider_height)

        //设置字母
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.isAntiAlias = true
        textPaint.textSize = 35f
        textPaint.color = AppUtil.getThemeColor()
        dividerHeightTop = context.resources.getDimensionPixelSize(R.dimen.divider_height_top)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //预留底部
        outRect.bottom = dividerHeight

        //预留顶部
        val pos = parent.getChildAdapterPosition(view)
        if (pos == 0 || isFirstInGroup(pos)) {//同组的第一个才添加padding
            outRect.top = dividerHeightTop
        } else {
            outRect.top = 0
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val left = parent.paddingLeft.toFloat()
        val right = (parent.width - parent.paddingRight).toFloat()

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)

            //绘制底部分割线
            val top = view.bottom.toFloat()
            val bottom = (view.bottom + dividerHeight).toFloat()
            c.drawRect(left, top, right, bottom, dividerPaint)

            //绘制顶部字母
            val position = parent.getChildAdapterPosition(view)
            val groupIdString = pinnedHeaderCallback.getGroupString(position)
            if (position == 0 || isFirstInGroup(position)) {
                val top = (view.top - dividerHeightTop).toFloat()
                val bottom = (view.top).toFloat()
                //绘制背景
                c.drawRect(left, top, right, bottom, dividerPaint)
                //绘制文本
                c.drawText(groupIdString, left + 20, bottom - 20, textPaint)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val itemCount = state.itemCount
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        var preGroupIdString = ""
        var groupIdString = ""
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            preGroupIdString = groupIdString
            groupIdString = pinnedHeaderCallback.getGroupString(position)
            if (groupIdString != preGroupIdString) {
                val viewBottom = view.bottom
                var textY = Math.max(dividerHeightTop, view.top)
                if (position + 1 < itemCount) { //下一个和当前不一样移动当前
                    val nextGroupId = pinnedHeaderCallback.getGroupString(position + 1)
                    if (nextGroupId != groupIdString && viewBottom < textY) {//组内最后一个view进入了header
                        textY = viewBottom
                    }
                }
                c.drawRect(left.toFloat(), (textY - dividerHeightTop).toFloat(), right.toFloat(), textY.toFloat(), dividerPaint)
                c.drawText(groupIdString, left.toFloat() + 20, textY.toFloat() - 20, textPaint)
            }
        }
    }

    private fun isFirstInGroup(pos: Int): Boolean {
        return if (pos == 0) {
            true
        } else {
            val prevGroupString = pinnedHeaderCallback.getGroupString(pos - 1)
            val groupIdString = pinnedHeaderCallback.getGroupString(pos)
            prevGroupString != groupIdString
        }
    }

    interface PinnedHeaderCreator {
        fun getGroupString(position: Int): String
    }
}