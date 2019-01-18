package com.ukyoo.v2client.ui.detail

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.BR
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityDetailBinding
import com.ukyoo.v2client.entity.ReplyModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.entity.V2EXModel
import com.ukyoo.v2client.inter.ItemClickPresenter
import com.ukyoo.v2client.util.InputUtils
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.adapter.MultiTypeAdapter
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter
import com.ukyoo.v2client.util.bindLifeCycle
import com.ukyoo.v2client.viewmodels.DetailViewModel
import com.ukyoo.v2client.widget.EnterLayout

/**
 * 主题详情页
 */
class DetailActivity : BaseActivity<ActivityDetailBinding>(), ItemClickPresenter<ReplyModel> {
    private var mTopicId: Int = 0
    private lateinit var mTopic: TopicModel
    private var page = 1

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<DetailViewModel>()
    }

    override fun restoreArgs(savedInstanceState: Bundle?) {
        super.restoreArgs(savedInstanceState)
        mTopicId = savedInstanceState?.getInt("topic_id", -1) ?: -1
    }

    override fun saveArgs(outState: Bundle?) {
        super.saveArgs(outState)
        mTopicId.let { outState?.putInt("topic_id", it) }
    }

    var isJsonApi: Boolean = false

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        if (intent.hasExtra("topic_id")) {
            mTopicId = intent.getIntExtra("topic_id", -1)

            if (isJsonApi) {
                getTopicByTopicId()
            } else {
                getTopicAndRepliesByTopicId()
            }
        } else if (intent.hasExtra("model")) {
            mTopic = intent.getParcelableExtra("model")
            mTopicId = mTopic.id

            initHeaderView()
            if (isJsonApi) {
                getRepliesByTopicId()
            } else {
                getTopicAndRepliesByTopicId()
            }
        }
    }

    private fun initHeaderView() {
    }

    /**
     * 查看话题内容
     */
    private fun getTopicByTopicId() {
        viewModel.getTopicsByTopicId(mTopicId, true)
            .bindLifeCycle(this)
            .subscribe({
                mTopic = it[0]
                mTopicId = mTopic.id

                initHeaderView()
                getRepliesByTopicId()
            }, {
                toastFailure(it)
            })
    }

    /**
     * 查询主题下的回复
     */
    private fun getRepliesByTopicId() {
        viewModel.getRepliesByTopicId(mTopicId, true)
            .bindLifeCycle(this)
            .subscribe({
            }, {
                toastFailure(it)
            })
    }

    /**
     * 查看主题和回复
     */
    private fun getTopicAndRepliesByTopicId() {
        viewModel.page = page
        viewModel.getTopicAndRepliesByTopicId(mTopicId, true)
    }

    private lateinit var mEnterLayout: EnterLayout

    override fun initView() {
        getComponent().inject(this)
        mBinding.setVariable(BR.vm, viewModel)

        //回复列表
        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = SingleTypeAdapter(mContext, R.layout.item_reply, viewModel.replyList).apply {
                itemPresenter = this@DetailActivity
            }

            adapter = MultiTypeAdapter(mContext, viewModel.multiDataList, object : MultiTypeAdapter.MultiViewTyper {
                override fun getViewType(item: Any): Int {
                    return if (item is TopicModel) ITEM_TOPIC else ITEM_REPLY
                }

            }).apply {
                addViewTypeToLayoutMap(ITEM_TOPIC, R.layout.item_topic_header)
                addViewTypeToLayoutMap(ITEM_REPLY, R.layout.item_reply)
            }
        }

        mEnterLayout = EnterLayout(mContext, mBinding.root, onClickSend)
        mEnterLayout.setDefaultHint("评论主题")
//        mEnterLayout.hide()
    }

    private var onClickSend: View.OnClickListener = View.OnClickListener {
        val content = mEnterLayout.getContent()
        if (content.isEmpty()) {
            ToastUtil.shortShow("回复内容不能为空")
            return@OnClickListener
        }
        InputUtils.popSoftkeyboard(mContext, mEnterLayout.content, false)

        viewModel.getReplyOnce(mTopicId)
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }

    override fun onItemClick(v: View?, item: ReplyModel) {
        //回复
        prepareAddComment(item, true)
    }

    private fun prepareAddComment(data: V2EXModel, popKeyboard: Boolean) {
        val content = mEnterLayout.content
        var replyToWho = ""
        mEnterLayout.clearContent()
        if (data is ReplyModel) {
            val replyObj = data as ReplyModel
            content.hint = "回复 " + replyObj.member.username
            replyToWho = "@" + replyObj.member.username + " "
        }

        if (popKeyboard) {
            InputUtils.popSoftkeyboard(mContext, content, true)
            val replyToWho_ = replyToWho
            Handler().postDelayed({
                content.setText(replyToWho_)
                content.setSelection(content.text.length)
            }, 500)
        } else {
            InputUtils.popSoftkeyboard(mContext, content, false)
        }
    }

    companion object {
        const val ITEM_TOPIC = 1
        const val ITEM_REPLY = 2
    }
}