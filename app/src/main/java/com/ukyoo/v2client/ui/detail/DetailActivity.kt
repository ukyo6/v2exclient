package com.ukyoo.v2client.ui.detail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.App
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityDetailBinding
import com.ukyoo.v2client.entity.ReplyModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.inter.ItemClickPresenter
import com.ukyoo.v2client.ui.viewmodels.DetailViewModel
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter
import com.ukyoo.v2client.util.bindLifeCycle
import com.ukyoo.v2client.widget.CustomDialog


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
            .bindLifeCycle(this)
            .subscribe({
                if(page == 1){
                    initHeaderView()
                }
            }, {
                toastFailure(it)
            })
    }


    private fun showShareDialog() {
        val shareView = LayoutInflater.from(mContext).inflate(R.layout.dialog_share, null)

        if (dialog == null) {
            dialog = CustomDialog.Builder(mContext)
                .setNotitle(true)
                .setCancelable(true)
                .setContentView(shareView)
                .setBottomDialog(true)
                .setWidth(App.SCREEN_WIDTH)
                .create()
        }
        dialog?.show()
    }

    override fun initView() {
        getComponent().inject(this)

        mBinding.vm = viewModel
        //回复列表
        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = SingleTypeAdapter(mContext, R.layout.item_reply, viewModel.replyList).apply {
                itemPresenter = this@DetailActivity
            }
        }
    }

    //初始化主题内容
    private fun initHeaderView() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }

    override fun onItemClick(v: View?, item: ReplyModel) {
    }
}