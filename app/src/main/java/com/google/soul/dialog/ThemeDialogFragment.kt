package com.google.soul.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.GridLayoutManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.soul.R
import com.google.soul.adapter.ThemeColorAdapter
import com.google.soul.base.BaseAdapter
import com.google.soul.utils.AppUtil
import com.google.soul.utils.SharePreferenceManager
import kotlinx.android.synthetic.main.fragment_dialog_theme.*


/**************************
作者：FYX
日期：2018/9/3 0003
 **************************/
class ThemeDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_theme, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView.layoutManager = GridLayoutManager(activity, 5)
        ThemeColorAdapter(activity!!, AppUtil.getThemeColors(), R.layout.item_colors).let {
            mRecyclerView.adapter = it
            it.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(obj: Any?, position: Int) {
                    SharePreferenceManager.setTheme(position)
                    //重启Activity
                    activity?.finish()
                    startActivity(activity?.intent)
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm)
        val window = dialog.window
        window.setLayout(dm.widthPixels, window.attributes.height)
    }
}