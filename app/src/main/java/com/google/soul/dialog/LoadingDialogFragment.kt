package com.google.soul.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.soul.R
import com.google.soul.utils.CustomToast

/**************************
作者：FYX
日期：2018/8/29 0029
 **************************/
class LoadingDialogFragment : DialogFragment() {

    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_loading, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
//        isCancelable = false
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm)
        val window = dialog.window
        window.setLayout((dm.widthPixels * 0.7).toInt(), window.attributes.height)
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(7000, 7000) {
            override fun onFinish() {
                CustomToast.showError("加载超时，请稍后重试")
                dismiss()
            }

            override fun onTick(millisUntilFinished: Long) {
            }
        }.start()
        super.show(manager, tag)
    }

    override fun dismiss() {
        countDownTimer?.cancel()
        super.dismiss()
    }

    override fun onCancel(dialog: DialogInterface?) {
        countDownTimer?.cancel()
        super.onCancel(dialog)
    }
}