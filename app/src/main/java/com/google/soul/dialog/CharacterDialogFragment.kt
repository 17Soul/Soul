package com.google.soul.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.*
import com.bumptech.glide.Glide
import com.google.soul.R
import com.google.soul.adapter.MovieCharacterDialogAdapter
import com.google.soul.base.BaseAdapter
import com.google.soul.mvp.model.CharacterEntity
import com.google.soul.mvp.model.MessageEvent
import kotlinx.android.synthetic.main.fragment_dialog_character.*
import org.greenrobot.eventbus.EventBus

/**************************
作者：FYX
日期：2018/8/29 0029
 **************************/
class CharacterDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_character, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: CharacterEntity = arguments?.getSerializable("data") as CharacterEntity
        Glide.with(activity!!).load(data.avatars.large).into(iv_avatar)
        tv_name.text = data.name
        tv_name_en.text = data.name_en
        tv_gender.text = data.gender
        tv_born_place.text = data.born_place
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        MovieCharacterDialogAdapter(activity!!, data.works, R.layout.item_movie_character_dialog).let {
            mRecyclerView.adapter = it
            it.notifyDataSetChanged()
            it.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(obj: Any?, position: Int) {
                    val bean = obj as CharacterEntity.WorksBean
                    EventBus.getDefault().post(MessageEvent(1, bean.subject.id))
                    dismiss()
                }
            })
        }
        iv_close.setOnClickListener { this.dismiss() }
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm)
        val window = dialog.window
        //去掉边框
        window.setBackgroundDrawable(ColorDrawable(-0x1))
        window.setGravity(Gravity.BOTTOM)
        window.setLayout(dm.widthPixels, window.attributes.height)
        window.setWindowAnimations(R.style.bottom_exist_anim_style)
    }
}