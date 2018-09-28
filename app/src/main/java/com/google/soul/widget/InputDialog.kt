package com.google.soul.widget

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.InputType
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.soul.R
import kotlinx.android.synthetic.main.dialog_input.*

/**************************
 *作者：FYX
 *日期：2018/9/27 14:39
 **************************/
class InputDialog : DialogFragment() {

    private var title = "输入"
    private var leftText = "取消"
    private var rightText = "确定"
    private var defaultText = ""
    private var okClick: OnOkClickListener? = null
    private var type: Int = InputType.TYPE_CLASS_TEXT//输入类型

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_input, container, false)
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
        btn_left.text = leftText
        btn_right.text = rightText
        et_input.inputType = type
        et_input.setText(defaultText)
        btn_left.setOnClickListener {
            dismiss()
        }
        btn_right.setOnClickListener {
            dismiss()
            okClick?.onOkClick(et_input.text.toString())
        }
    }

    fun title(title: String): InputDialog {
        this.title = title
        return this
    }

    fun leftText(leftText: String): InputDialog {
        this.leftText = leftText
        return this
    }

    fun rightText(rightText: String): InputDialog {
        this.rightText = rightText
        return this
    }

    fun defaultText(defaultText: String): InputDialog {
        this.defaultText = defaultText
        return this
    }

    fun setInputType(type: Int): InputDialog {
        this.type = type
        return this
    }

    fun setOkClick(listener: OnOkClickListener): InputDialog {
        this.okClick = listener
        return this
    }

    interface OnOkClickListener {
        fun onOkClick(string: String)
    }
}