package com.google.soul.widget

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.soul.R
import kotlinx.android.synthetic.main.dialog_hint.*

/**************************
 *作者：FYX
 *日期：2018/9/27 10:15
 **************************/
class HintDialog : DialogFragment() {

    private var title = "提示"
    private var message = ""
    private var leftText = "取消"
    private var rightText = "确定"
    private var leftClick: View.OnClickListener? = null
    private var rightClick: View.OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_hint, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        isCancelable = false
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        dialog.window?.let {
            it.attributes.dimAmount = 0.3f
            it.setWindowAnimations(R.style.HintDialogAnim)
            it.setLayout((dm.widthPixels * 0.8).toInt(), it.attributes.height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title.text = title
        tv_message.text = message
        if (leftClick == null) {
            btn_left.visibility = View.GONE
        } else {
            btn_left.visibility = View.VISIBLE
            btn_left.text = leftText
            btn_left.setOnClickListener {
                dismiss()
                leftClick?.onClick(it)
            }
        }
        btn_right.text = rightText
        btn_right.setOnClickListener {
            dismiss()
            rightClick?.onClick(it)
        }
    }

    fun title(title: String): HintDialog {
        this.title = title
        return this
    }

    fun message(message: String): HintDialog {
        this.message = message
        return this
    }

    fun leftText(leftText: String): HintDialog {
        this.leftText = leftText
        return this
    }

    fun rightText(rightText: String): HintDialog {
        this.rightText = rightText
        return this
    }

    fun setLeftClick(listener: View.OnClickListener): HintDialog {
        this.leftClick = listener
        return this
    }

    fun setRightClick(listener: View.OnClickListener): HintDialog {
        this.rightClick = listener
        return this
    }
}