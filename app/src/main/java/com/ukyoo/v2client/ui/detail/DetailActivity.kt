package com.ukyoo.v2client.ui.detail

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.BR
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityDetailBinding
import com.ukyoo.v2client.entity.ReplyModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.inter.ItemClickPresenter
import com.ukyoo.v2client.ui.viewmodels.DetailViewModel
import com.ukyoo.v2client.util.adapter.MultiTypeAdapter
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter
import com.ukyoo.v2client.util.bindLifeCycle

/**
 * 主题详情页
 */
class DetailActivity : BaseActivity<ActivityDetailBinding>(), ItemClickPresenter<ReplyModel> {
    private var mTopicId: Int = 0
    private lateinit var mTopic: TopicModel
    private var page = 1

    private var dialog: Dialog? = null

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

    override fun loadData(isRefresh: Boolean) {
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
        viewModel.topicId = mTopicId
        viewModel.getTopicsByTopicId(true)
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
        viewModel.topicId = mTopicId
        viewModel.getRepliesByTopicId(true)
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
        viewModel.topicId = mTopicId
        viewModel.page = page
        viewModel.getTopicAndRepliesByTopicId(true)
    }

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
                    return if(item is TopicModel) ITEM_TOPIC else ITEM_REPLY
                }

            }).apply {
                addViewTypeToLayoutMap(ITEM_TOPIC, R.layout.item_topic_header)
                addViewTypeToLayoutMap(ITEM_REPLY, R.layout.item_reply)
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }

    override fun onItemClick(v: View?, item: ReplyModel) {
    }

    companion object {
        const val ITEM_TOPIC = 1
        const val ITEM_REPLY = 2
    }
}