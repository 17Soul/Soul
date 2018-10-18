package com.google.soul.ui.fragment

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.Conversation
import com.google.soul.R
import com.google.soul.adapter.ConversationListAdapter
import com.google.soul.base.BaseAdapter
import com.google.soul.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_conversation.*

/**************************
作者：FYX
日期：2018/8/24 0024
 **************************/
class ConversationFragment : BaseFragment() {

    private var conversationList = ArrayList<Conversation>()
    private var conversationListAdapter: ConversationListAdapter? = null

    override fun layoutId(): Int = R.layout.fragment_conversation

    override fun init() {
        conversationListAdapter = ConversationListAdapter(mContext, conversationList, R.layout.item_conversation)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.addItemDecoration(DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL))
        mRecyclerView.adapter = conversationListAdapter
        mSmartRefreshLayout.setOnRefreshListener {
            getConversationList()
        }
        conversationListAdapter?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                val conversation = obj as Conversation
            }
        })
    }

    override fun lazyLoad() {
        mSmartRefreshLayout.autoRefresh()
    }

    private fun getConversationList() {
        conversationList.clear()
        conversationList.addAll(JMessageClient.getConversationList() as ArrayList<Conversation>)
        mSmartRefreshLayout.finishRefresh()
        conversationListAdapter?.notifyDataSetChanged()
    }
}